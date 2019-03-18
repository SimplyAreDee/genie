package click.acme.genius.controllers.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.adapters.ChatListAdapter;
import click.acme.genius.helpers.ChatHelper;
import click.acme.genius.models.Chat;
import click.acme.genius.models.parser.SearchResultsJsonParser;
import click.acme.genius.models.SearchUserName;
import click.acme.genius.models.User;
import click.acme.genius.R;
import click.acme.genius.utils.ItemClickSupport;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.collect.Collections2;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Activité présentant toutes les conversations actuellement déclenchées par l'utilisateur
 * //TODO améliorer la création d'une nouvelle conversation en vérifiant l'existance d'une précédente (voir avec Algolia/ES)
 * Les conversations sont représentées dans une gridview.
 */
public class ChatActivity extends BaseActivity implements ChatListAdapter.Listener{

    @BindView(R.id.chat_activity_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.chat_activity_notification_textview)
    TextView mNotificationText;
    @BindView(R.id.chat_activity__progressLayout)
    RelativeLayout mProgressLayout;
    @BindView(R.id.chat_activity_search_edittext)
    AutoCompleteTextView mSearchEditText;

    private Spinner mNicknameListSpinner;
    private ChatListAdapter mChatListAdapter;
    private Index mChatIndex;
    private Index mUsersIndex;
    private CompletionHandler mChatCompletionHandler;
    private CompletionHandler mUsersCompletionHandler;
    private List<SearchUserName> mNicknameList;
    private List<SearchUserName> mNicknameNonFilteredList;
    private ArrayAdapter mNicknameAdapter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void postCreateTreatment() {
        configureRecyclerView();
        configureOnClickRecyclerView();
        if(mChatListAdapter != null) {
            mChatListAdapter.startListening();
        }

        initialiseAlgoliaSearch();

        retrieveUserList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Affiche un loader le temps de demander les chats au SGBD
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Quand les données sont changées
     */
    @Override
    public void onDataChanged() {
        if(mProgressLayout.getVisibility() == View.VISIBLE) {
            //On fait disparaitre le loader
            mProgressLayout.setVisibility(View.GONE);
        }

        //Si nous avions précédemment pas de résultat le texte de notification est actuellement affiché
        //à l'utilisateur, on l'affiche et on regarde après si il faut le réafficher.
        if(mNotificationText.getVisibility() == View.VISIBLE){
            mNotificationText.setVisibility(View.GONE);
        }

        if(mChatListAdapter.getItemCount() == 0){
            mNotificationText.setVisibility(View.VISIBLE);
            mNotificationText.setText(getString(R.string.no_chat));
        }

        mChatListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatListAdapter.stopListening();
    }

    /**
     * On initialise la recherche Algolia, deux indexes sont utilisés
     * l'un pour les conversations {@see mChatIndex}
     * l'autre pour les utilisateurs, qui sert pour le filtre de conversation et la fenetre de nouvelle conversation {@see mUsersIndex}
     */
    private void initialiseAlgoliaSearch() {
        Client client = new Client("OWUCGTA5DD", "c546b1571ebc3902e483590d55763dfb");
        //Index des conversations
        mChatIndex = client.getIndex("chats");
        //index des utilisateurs pour le livesearch et la fenêtre de nouvelle conversation
        mUsersIndex = client.getIndex("users_name_ids");

        //Handler utilisé lorsqu'on effectue une recherche sur l'index conversation
        //on filtrera le recyclerView avec les ids retournés par la recherche
        mChatCompletionHandler = (content, error) -> {
            if(content != null && content.length() >= 0){
                SearchResultsJsonParser searchResultsJsonParser = new SearchResultsJsonParser();
                List<Chat> results = searchResultsJsonParser.parseChatResults(content);

                if(results.size() > 0) {
                    initialiseRecyclerView(
                            createNewChatListAdapter(
                                    ChatHelper.getChatsFilterByUsernameFromDatabase(
                                            results.get(0).getUsersReferences().get(0), 50
                                    )
                            )
                    );
                }
            }
        };

        //Handler utilisé lorsqu'on effectue une recherche sur les utilisateurs
        //En réalité il n'est utilisé qu'une seul fois lorsqu'on effectue une requête pour obtenir
        // la liste de tous les utilisateurs
        // todo : Surveiller l'utilisation, si la base d'utilisateur est trop importante, il faudra
        //  déclenché la recherche à partir d'un certains nombres de charactère.
        mUsersCompletionHandler = (content, error) -> {
            if(content != null && content.length() >= 0){
                SearchResultsJsonParser searchResultsJsonParser = new SearchResultsJsonParser();
                mNicknameList = searchResultsJsonParser.parseUserResults(content);
                mNicknameNonFilteredList = mNicknameList;

                configureLiveSearchEditText();
            }
        };
    }

    /**
     * Retourne toute la liste des utilisateurs de l'application
     */
    private void retrieveUserList() {
        mUsersIndex.searchAsync(new com.algolia.search.saas.Query(), mUsersCompletionHandler);
    }

    /**
     * Ajoute au SearchEditText le dataSet des utilisateurs de l'application
     */
    private void configureLiveSearchEditText() {
        mSearchEditText.setEnabled(true);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, mNicknameList);
        mSearchEditText.setThreshold(1);
        mSearchEditText.setAdapter(adapter);
    }

    /**
     * Initialise la recyclerView avec le dataSet de Firestore
     */
    private void configureRecyclerView() {
        FirebaseFirestore.setLoggingEnabled(true);
        initialiseRecyclerView(new ChatListAdapter(
                generateOptionsForAdapter(ChatHelper.getChatsFromDatabase(50)),
                this
        ));
    }

    /**
     * Permet de regénéré le dataSet de l'adapter en fonction d'une Query Firestore
     * Utilisé lors du filtrage par utilisateur des conversations
     * @param query Query la query servant à construire le dataSet
     * @return ChatListAdapter l'adapter contenant le dataSet retourné par a Query
     */
    private ChatListAdapter createNewChatListAdapter(Query query){
        return new ChatListAdapter(
                generateOptionsForAdapter(query),
                this
        );
    }

    private void initialiseRecyclerView(ChatListAdapter chatListAdapter){
        mChatListAdapter = chatListAdapter;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(mChatListAdapter);
        mChatListAdapter.notifyDataSetChanged();
    }

    private FirestoreRecyclerOptions<Chat> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_layout_list_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Chat chat = mChatListAdapter.getItem(position);

                    startNewChatDetailActivity(chat);
                });
    }

    /**
     * Permet de démarrer une nouvelle conversation avec un utilisateur
     * @param chat Chat la conversation à laquelle rattacher les utilisateurs
     */
    private void startNewChatDetailActivity(Chat chat) {
        Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
        String chatReference = chat.getId();
        intent.putExtra("chatReference", chatReference);

        startActivity(intent);
    }

    /**
     * Effectue une recherche dans les conversations
     * @param view View la vue déclanchant l'évenement
     */
    @OnClick(R.id.chat_activity_search_imagebutton)
    public void onSearchButton(View view){
        String searchText = mSearchEditText.getText().toString();
        mChatIndex.searchAsync(new com.algolia.search.saas.Query(searchText.toString())
                                                .setFilters("usersReferences:"+ User.getCurrentUser().getId())
                                                .setAttributesToRetrieve("usersReferences"), mChatCompletionHandler);
    }

    /**
     * Démarre une nouvelle conversation en proposant la liste des utilisateurs et un filtre de recherche
     * sur les utilisateurs
     * @param view View la vue déclanchant l'évenement
     */
    @OnClick(R.id.chat_activity_newmessage_imagebutton)
    public void onNewMessageButton(View view)
    {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_chat_directory,null);

        mNicknameListSpinner = layout.findViewById(R.id.activity_chat_directory_spinner);

        addNewAdatperToNicknameSpinner();

        EditText searchEditText = layout.findViewById(R.id.activity_chat_directory_editText);
        searchEditText.addTextChangedListener( newTextEditTextWatcher() );

        ImageButton startNewConversationButton = layout.findViewById(R.id.activity_chat_directory_imageButton);
        startNewConversationButton.setOnClickListener(v -> {
            if(mNicknameListSpinner.getSelectedItem() != null){
                SearchUserName searchUserName = (SearchUserName) mNicknameListSpinner.getSelectedItem();
                Chat chat = new Chat();
                chat.generateId();
                chat.addUsersReferences(User.getCurrentUser().getId());
                chat.addUsersReferences(searchUserName.getId());

                chat.addUsersNames(User.getCurrentUser().getUserName());
                chat.addUsersNames(searchUserName.getUserName());
                chat.setChatShortcutName(searchUserName.getUserName().substring(0,1));

                ChatHelper.saveState(chat);

                startNewChatDetailActivity(chat);
            }
        });

        builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setView(layout);

        alertDialog = builder.create();

        alertDialog.show();
    }

    /**
     * Permet de filtrer le dataSet du EditText
     * @return TextWatcher le listener qui ecoutera les changements sur l'EditText {@see onNewMessageButton}
     */
    private TextWatcher newTextEditTextWatcher(){
        return new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!_ignore) {
                    if(s.length() >= 3){
                        _ignore = true;
                        Collection<SearchUserName> filtered = Collections2.filter(mNicknameNonFilteredList,
                                input -> input.getUserName().contains(s.toString()));
                        mNicknameList = new ArrayList<>(filtered);

                        addNewAdatperToNicknameSpinner();

                        _ignore = false;
                    }
                    if(s.length() < 3){
                        _ignore = true;

                        mNicknameList = mNicknameNonFilteredList;

                        addNewAdatperToNicknameSpinner();

                        _ignore = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * Ajoute un adapter avec le dataSet de la liste des utilisateurs
     * La liste des utilisateurs à pu être triée et sera donc affichée triée.
     */
    private void addNewAdatperToNicknameSpinner() {

        mNicknameAdapter = new ArrayAdapter<SearchUserName>(ChatActivity.this ,
                android.R.layout.simple_spinner_item, mNicknameList);

        mNicknameListSpinner.setAdapter(mNicknameAdapter);

        mNicknameAdapter.notifyDataSetChanged();
    }
}
