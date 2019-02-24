package click.acme.genius.Controllers.Fragments;


import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.Adapters.QuestionDetailAdapter;
import click.acme.genius.Helpers.AnswerHelper;
import click.acme.genius.Helpers.QuestionHelper;
import click.acme.genius.Models.Answer;
import click.acme.genius.Models.Question;
import click.acme.genius.Models.User;
import click.acme.genius.R;
import click.acme.genius.Views.QuestionListItemViewHolder;
import icepick.State;

public class QuestionDetailFragment extends BaseFragment implements QuestionDetailAdapter.Listener {

    private Question mQuestion;
    private QuestionDetailAdapter mQuestionListAdapter;
    private ListenerRegistration mListenerRegistration;
    @State
    public String mAnswerText;
    @State
    public boolean isShowingAnswerDialog;

    @BindView(R.id.fragment_layout_question_detail_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_layout_question_detail_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_layout_question_detail_notification_textview)
    TextView mNotificationText;
    @BindView(R.id.fragment_layout_question_detail_action_button)
    FloatingActionButton mActionButton;
    //Included question item layout
    @BindView(R.id.fragment_layout_list_item_title_textview)
    TextView mItemEntryTitle;
    @BindView(R.id.fragment_layout_list_item_subject_textview)
    TextView mItemEntrySubject;
    @BindView(R.id.fragment_layout_list_item_reference_textview)
    TextView mItemEntryReference;
    @BindView(R.id.fragment_layout_list_item_date_textview)
    TextView mItemEntryDate;
    @BindView(R.id.fragment_layout_list_item_certifiedAnswer_imageview)
    ImageView mPlusOneButton;
    @BindView(R.id.fragment_layout_list_item_communityAnswer_imagevie)
    ImageView mMinorOneButton;
    @BindView(R.id.fragment_layout_list_item_questionVote)
    TextView mQuestionVote;
    @BindView(R.id.fragment_layout_list_item_answerVote_textview)
    TextView mAnswerVote;
    
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
        //Changement pour downvote
        mMinorOneButton.setImageResource(R.drawable.outline_expand_more_white_18dp);

        mProgressBar.setVisibility(View.VISIBLE);

        String mQuestionReference = getArguments().getString("questionReference");

        getAndDisplayRelatedQuestion(mQuestionReference);

        addClickEventOnImageView();
    }

    private void addClickEventOnImageView() {
        mPlusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuestion != null) {
                    QuestionHelper.addCreditToQuestion(mQuestion);
                }
            }
        });
        mMinorOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuestion != null) {
                    QuestionHelper.addDiscreditToQuestion(mQuestion);
                }
            }
        });
    }

    @Override
    protected void updateDesign() {
        if(isShowingAnswerDialog)
        {
            showAnswerFragment();
        }
    }

    @OnClick(R.id.fragment_layout_question_detail_action_button)
    public void OnActionButtonClick(){
        showAnswerFragment();
    }

    private void getAndDisplayRelatedQuestion(String questionReference){
        DocumentReference documentReference = QuestionHelper.getQuestionByReference(questionReference);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    mQuestion = document.toObject(Question.class);
                    updateViewWithQuestionData(mQuestion);
                    configureRecyclerView( mQuestion.getId() );
                } else {
                    //INFO ne peut se rencontrer que si la question a été supprimée coté serveur entre temps
                    mNotificationText.setText(getString(R.string.error_occured));
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            } else {
                //TODO Supprimer l'erreur coté client et la faire remonter sur crashanalytics
                mNotificationText.setText(String.format(Locale.FRANCE,"%d : %s", R.string.error_occured, task.getException()));
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mListenerRegistration = documentReference.addSnapshotListener((snapshot, e) -> {
            if (snapshot != null && snapshot.exists()) {
                mQuestion = snapshot.toObject(Question.class);
                updateViewWithQuestionData(mQuestion);
            }
        });
    }

    private void updateViewWithQuestionData(Question question) {
        if(question != null) {
            mItemEntryTitle.setText(question.getTitle());
            mItemEntrySubject.setText(question.getSubject());
            mItemEntryReference.setText(question.getIban());
            mItemEntryDate.setText(QuestionListItemViewHolder.getStringValueOfElapsedTimeSince(question.getDateCreated()));
            mQuestionVote.setText(String.valueOf( question.getWeight() ));
            AnswerHelper.getAnswersFromDatabase(question.getId()).get().addOnSuccessListener(
                    queryDocumentSnapshots -> mAnswerVote.setText(String.valueOf( queryDocumentSnapshots.size() ))
            );
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
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setMessage("Inscris ta réponse");
        alert.setTitle("Répondre");

        final EditText edittext = new EditText(getContext());

        edittext.setText(mAnswerText);

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

        alert.setPositiveButton("Répondre", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String explanation = edittext.getText().toString();

                saveAnswer(explanation);

                isShowingAnswerDialog = false;
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mAnswerText = "";
                isShowingAnswerDialog = false;
            }
        });

        alert.show();
    }

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
