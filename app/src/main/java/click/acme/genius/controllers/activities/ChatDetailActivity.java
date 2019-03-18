package click.acme.genius.controllers.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.adapters.ChatMessageListAdapter;
import click.acme.genius.helpers.ChatMessageHelper;
import click.acme.genius.models.ChatMessage;
import click.acme.genius.models.User;
import click.acme.genius.R;

/**
 * Affiche le detail d'une conversation avec l'ensemble des messages associés à cette conversation
 * permet également l'ajout des messages et (soon) des images
 * //TODO implémenter l'envoi des images
 */
public class ChatDetailActivity extends BaseActivity implements ChatMessageListAdapter.Listener {

    @BindView(R.id.activity_chat_detail_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_chat_detail_recycler_view_empty)
    TextView mNotificationText;
    @BindView(R.id.activity_chat_detail_progressLayout)
    RelativeLayout mProgressLayout;
    @BindView(R.id.activity_chat_detail_message_edit_text)
    TextInputEditText mEditTextMessage;
    @BindView(R.id.activity_chat_detail_image_chosen_preview)
    ImageView mImageViewPreview;
    @BindView(R.id.activity_chat_detail_adView)
    AdView mAdView;

    private String mChatReference;
    private ChatMessageListAdapter mChatMessageListAdapter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_chat_detail;
    }

    @Override
    protected void postCreateTreatment() {
        Intent intent = getIntent();
        mChatReference = intent.getStringExtra("chatReference");

        configureRecyclerView();

        if(mChatMessageListAdapter != null) {
            mChatMessageListAdapter.startListening();
        }
        //Initialise les publicités du bandeau en haut.
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("955122ADA1F93E05A0EFB6FA1C5868C5").build();
        mAdView.loadAd(adRequest);
    }

    private void configureRecyclerView() {
        FirebaseFirestore.setLoggingEnabled(true);
        //Récupère l'ensemble des messages de la conversation et les ajoutes à l'adapter
        initialiseRecyclerView(new ChatMessageListAdapter(
                generateOptionsForAdapter(ChatMessageHelper.getChatMessageFromDatabase(mChatReference)),
                Glide.with(this), //Pour l'affichage
                this
        ));
    }

    private void initialiseRecyclerView(ChatMessageListAdapter chatMessageListAdapter){
        mChatMessageListAdapter = chatMessageListAdapter;
        mChatMessageListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mChatMessageListAdapter.getItemCount()); // Scroll à la fin pour les nouveaux messages
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mChatMessageListAdapter);

        mChatMessageListAdapter.notifyDataSetChanged();
    }

    private FirestoreRecyclerOptions<ChatMessage> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLifecycleOwner(this)
                .build();
    }

    @OnClick(R.id.activity_chat_detail_send_button)
    public void onClickSendMessage() {
        if (!TextUtils.isEmpty(mEditTextMessage.getText()) && User.getCurrentUser() != null){
            if (this.mImageViewPreview.getDrawable() == null) {

                ChatMessage message = new ChatMessage();

                message.generateId();
                message.markStamp();
                message.setChatReference(mChatReference);
                message.setSenderReference(User.getCurrentUser().getId());
                message.setContent(mEditTextMessage.getText().toString());//TextUtils.isEmpty s'est chargé de ça

                ChatMessageHelper.saveState(message).addOnFailureListener(this.onFailureListener());

                this.mEditTextMessage.setText("");
            } else {
                // Comming soon
                //this.uploadImageInFirebaseAndSendMessage(mEditTextMessage.getText().toString());
                //this.mEditTextMessage.setText("");
                //this.mImageViewPreview.setImageDrawable(null);
            }
        }
    }

    @Override
    public void onDataChanged() {
        mProgressLayout.setVisibility(View.GONE);
        mNotificationText.setText("Pas de messages");
        mNotificationText.setVisibility(mChatMessageListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

}
