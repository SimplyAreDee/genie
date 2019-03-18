package click.acme.genius.controllers.fragments;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.adapters.QuestionDetailAdapter;
import click.acme.genius.controllers.activities.ChatDetailActivity;
import click.acme.genius.helpers.AnswerHelper;
import click.acme.genius.helpers.ChatHelper;
import click.acme.genius.helpers.QuestionHelper;
import click.acme.genius.helpers.RewardHelper;
import click.acme.genius.helpers.UserHelper;
import click.acme.genius.models.Answer;
import click.acme.genius.models.Chat;
import click.acme.genius.models.Question;
import click.acme.genius.models.User;
import click.acme.genius.R;
import click.acme.genius.views.QuestionListItemViewHolder;
import icepick.State;

/**
 * Affiche le détail d'une question ainsi que la liste des réponses
 */
public class QuestionDetailFragment extends BaseFragment implements QuestionDetailAdapter.Listener {
    @BindView(R.id.fragment_layout_question_detail_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_layout_question_detail_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_layout_question_detail_notification_textview)
    TextView mNotificationText;
    @BindView(R.id.fragment_layout_question_detail_action_button)
    FloatingActionButton mActionButton;
    //Included question item layout
    @BindView(R.id.fragment_layout_question_item_title_textview)
    TextView mQItemEntryTitleTextview;
    @BindView(R.id.fragment_layout_question_item_instruction_textview)
    TextView mQItemEntryInstructionTextview;
    @BindView(R.id.fragment_layout_question_item_subject_textview)
    TextView mQItemEntrySubjectTextview;
    @BindView(R.id.fragment_layout_question_item_reference_textview)
    TextView mQItemEntryReferenceTextview;
    @BindView(R.id.fragment_layout_question_item_date_textview)
    TextView mQItemEntryDateTextview;
    @BindView(R.id.fragment_layout_question_item_majorvote_button)
    Button mQPlusOneButton;
    @BindView(R.id.fragment_layout_question_item_minorvote_button)
    Button mQMinorOneButton;
    @BindView(R.id.fragment_layout_question_item_questionVote)
    TextView mQMajorVoteTextview;
    @BindView(R.id.fragment_layout_question_item_answerVote_textview)
    TextView mQMinorVoteTextview;
    @BindView(R.id.fragment_layout_question_item_pseudo)
    TextView mQNicknameTextview;
    @BindView(R.id.fragment_layout_question_item_totalpoint)
    TextView mQTotalpointTextview;
    @BindView(R.id.fragment_layout_question_item_totalbadge)
    TextView mQTotalbadgeTextview;
    @BindView(R.id.fragment_layout_question_item_user_image)
    ImageView mQUserImageview;
    @BindView(R.id.fragment_layout_question_item_capture_preview_imagebutton)
    ImageButton mCapturePreviewImageButton;

    private List<ImageView> mQDisplayedUserBadges;
    private Question mQuestion;
    private QuestionDetailAdapter mQuestionListAdapter;
    private ListenerRegistration mListenerRegistration;
    private User mAuthorInformation;

    @State
    public String mAnswerText;
    @State
    public boolean isShowingAnswerDialog;
    
    public QuestionDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseFragment newInstance() {
        return new QuestionDetailFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_question_detail;
    }

    @Override
    protected void configureDesign() {
        mProgressBar.setVisibility(View.VISIBLE);
        if(getArguments() != null) {
            String mQuestionReference = getArguments().getString("questionReference");

            getAndDisplayRelatedQuestion(mQuestionReference);

            populateDisplayedUserBadgesList();
        }/*else{
            //Quand sera implémenté le deeplink il faut pouvoir gérer le cas d'une question indéxée mais
            //inexistante dans la base de donnée.
        }*/
    }

    //Ajoute les 4 ImageViews pour les badges dans un tableau
    private void populateDisplayedUserBadgesList() {
        mQDisplayedUserBadges = new ArrayList<ImageView>();
        if(getActivity() != null) {
            mQDisplayedUserBadges.add(getActivity().findViewById(R.id.fragment_layout_question_item_badge0));
            mQDisplayedUserBadges.add(getActivity().findViewById(R.id.fragment_layout_question_item_badge1));
            mQDisplayedUserBadges.add(getActivity().findViewById(R.id.fragment_layout_question_item_badge2));
            mQDisplayedUserBadges.add(getActivity().findViewById(R.id.fragment_layout_question_item_badge3));
        }
    }

    //TODO Devrait être géré coté serveur
    @OnClick(R.id.fragment_layout_question_item_majorvote_button)
    public void onClickPlusOneButton(View v) {
        if(mQuestion != null) {
            QuestionHelper.addCreditToQuestion(mQuestion);
        }
    }

    //TODO Devrait être géré coté serveur
    @OnClick(R.id.fragment_layout_question_item_minorvote_button)
    public void onClickMinorOneButton(View v) {
        if(mQuestion != null) {
            QuestionHelper.addDiscreditToQuestion(mQuestion);
        }
    }

    @Override
    protected void updateDesign() {
        if(isShowingAnswerDialog)
        {
            showAnswerFragment();
        }
    }

    @OnClick(R.id.fragment_layout_question_detail_action_button)
    public void onActionButtonClick(){
        showAnswerFragment();
    }


    /**
     * Démarre une conversation, seulement si on a les informations de l'auteur
     */
    @OnClick(R.id.fragment_layout_question_item_user_image)
    public void onClickUserPortait(){
        if(mAuthorInformation != null) {
            startNewConversation();
        }
    }

    /**
     * Créer tout ce qu'il faut pour démarrer une nouvelle conversation
     */
    private void startNewConversation() {
        Chat chat = new Chat();
        chat.generateId();
        chat.addUsersReferences(User.getCurrentUser().getId());
        chat.addUsersReferences(mAuthorInformation.getId());

        chat.addUsersNames(User.getCurrentUser().getUserName());
        chat.addUsersNames(mAuthorInformation.getUserName());
        chat.setChatShortcutName(mAuthorInformation.getUserName().substring(0, 1));

        ChatHelper.saveState(chat);

        Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
        String chatReference = chat.getId();
        intent.putExtra("chatReference", chatReference);

        startActivity(intent);
    }

    /**
     *
     * @param questionReference String la référence de la question
     */
    private void getAndDisplayRelatedQuestion(String questionReference){
        DocumentReference documentReference = QuestionHelper.getQuestionByReference(questionReference);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    mQuestion = document.toObject(Question.class);

                    if(mQuestion != null) {
                        QuestionHelper.addViewToQuestion(mQuestion);

                        updateViewWithQuestionData(mQuestion);
                        configureRecyclerView(mQuestion.getId());
                    }
                } else {
                    mNotificationText.setText(getString(R.string.error_occured));
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            } else {
                //TODO En production Supprimer l'erreur coté client et la faire remonter sur crashanalytics
                // puis rediriger l'utilisateur sur l'accueil
                mNotificationText.setText(String.format(Locale.FRANCE,"%d : %s", R.string.error_occured, task.getException()));
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        //Permet d'avoir la mise à jour en temps réel des réponses
        mListenerRegistration = documentReference.addSnapshotListener((snapshot, e) -> {
            if (snapshot != null && snapshot.exists()) {
                mQuestion = snapshot.toObject(Question.class);
                updateViewWithQuestionData(mQuestion);
            }
        });
    }

    //Affiche les informations relatives à la question dans la partie du Layout reservé à cet affichage
    private void updateViewWithQuestionData(Question question) {
        if(question != null) {
            mQItemEntryTitleTextview.setText( question.getTitle() );
            mQItemEntrySubjectTextview.setText( question.getSubject() );
            mQItemEntryReferenceTextview.setText( question.getIban() );
            mQItemEntryDateTextview.setText(QuestionListItemViewHolder.getStringValueOfElapsedTimeSince(question.getDateCreated()));
            mQMajorVoteTextview.setText(String.valueOf( question.getMajor() ));
            mQMinorVoteTextview.setText(String.valueOf( question.getMinor() ));
            mQItemEntryInstructionTextview.setText( question.getInstruction() );
            if(question.getCapturedImage() != null && getContext() != null){
                Glide.with(getContext()).load(question.getCapturedImage()).into(mCapturePreviewImageButton);
            }
            UserHelper.getUserById(question.getAuthorReference()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        mAuthorInformation = documentSnapshot.toObject(User.class);
                        if(mAuthorInformation != null && getView() != null) {
                            mQNicknameTextview.setText(mAuthorInformation.getUserName());
                            mQTotalpointTextview.setText( String.valueOf( mAuthorInformation.getTotalScore() ) );
                            displayUserBadge(mAuthorInformation);
                            Glide.with(getView()).load(mAuthorInformation.getAvatarUrl()).into(mQUserImageview);
                        }
                    }
                }
            });
        }
    }

    /**
     * Affiche 4 badges que l'utilisateur à choisit de rendre public
     * //TODO pour le moment affiche les 4 premiers badges, implémenter la routine dans la classe AccountRewardFragment
     * @param user User l'utilisateur dont on doit afficher les badges
     */
    private void displayUserBadge(User user){
        if (user.getRewardBadges() != null) {
            mQTotalbadgeTextview.setText( String.valueOf( user.getRewardBadges().size() ) );
            int displayedBadgesCount = 0;
            for(String badgeReference : user.getRewardBadges()){
                if(displayedBadgesCount > 4){
                    continue;
                }
                mQDisplayedUserBadges.get(displayedBadgesCount).setImageResource(RewardHelper.getRessouceId(badgeReference));
                displayedBadgesCount++;
            }
        }else{
            mQTotalbadgeTextview.setText("0");
        }

    }

    private void configureRecyclerView(String questionReference) {
        mQuestionListAdapter = new QuestionDetailAdapter(
                generateOptionsForAdapter(AnswerHelper.getAnswersFromDatabase(questionReference)),
                this
        );
        mQuestionListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mQuestionListAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mQuestionListAdapter.startListening();
    }

    private FirestoreRecyclerOptions<Answer> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Answer>()
                .setQuery(query, Answer.class)
                .setLifecycleOwner(this)
                .build();
    }


    private void showAnswerFragment() {
        isShowingAnswerDialog = true;
        if(getContext() != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            alert.setMessage("Inscris ta réponse");
            alert.setTitle("Répondre");

            final EditText edittext = new EditText(getContext());

            edittext.setText(mAnswerText);

            //On utilise ce Listener pour garder en mémoire le contenu du champ de la réponse
            //pour le restitué en cas de changement d'orientation de l'écran.
            edittext.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            mAnswerText = s.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    }
            );

            alert.setView(edittext);

            alert.setPositiveButton("Répondre", (dialog, whichButton) -> {
                String explanation = edittext.getText().toString();

                saveAnswer(explanation);

                isShowingAnswerDialog = false;
            });

            alert.setNegativeButton("Annuler", (dialog, whichButton) -> {
                mAnswerText = "";
                isShowingAnswerDialog = false;
            });

            alert.show();
        }
    }

    /**
     * Sauvegarde la réponse sur le SGBD
     * @param explanation String la réponse
     */
    private void saveAnswer(String explanation){
        Answer answer = new Answer();

        answer.generateId();
        answer.setWeight(0);
        answer.setAuthorReference(User.getCurrentUser().getId());
        answer.setExplanation(explanation);
        answer.setQuestionReference(mQuestion.getId());

        AnswerHelper.saveState(answer);
    }

    @Override
    public void onDataChanged() {
        mNotificationText.setText(getString(R.string.no_answer));
        mNotificationText.setVisibility(mQuestionListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mQuestionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mListenerRegistration != null){
            mListenerRegistration.remove();
        }
        if (mQuestionListAdapter != null) {
            mQuestionListAdapter.stopListening();
        }
    }
}
