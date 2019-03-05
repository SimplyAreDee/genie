package click.acme.genius.Views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import click.acme.genius.Helpers.AnswerHelper;
import click.acme.genius.Helpers.UserHelper;
import click.acme.genius.Models.Answer;
import click.acme.genius.R;

public class QuestionDetailtemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_answer_item_answer_textview)
    TextView mItemEntryAnswerTextview;
    @BindView(R.id.fragment_layout_answer_item_author_textview)
    TextView mItemEntryAuthorTextview;
    @BindView(R.id.fragment_layout_answer_item_date_textview)
    TextView mItemEntryDateTextview;
    @BindView(R.id.fragment_layout_answer_item_questionVote_textview)
    TextView mQuestionVote;
    @BindView(R.id.fragment_layout_answer_item_major_imageview)
    Button mPlusOneButton;
    @BindView(R.id.fragment_layout_answer_item_minor_imagevie)
    Button mMinorOneButton;

    Answer mAnswer;

    public QuestionDetailtemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.fragment_layout_answer_item_major_imageview)
    public void onClickPlusOneButton(View v) {
        if(mAnswer != null) {
            AnswerHelper.addCreditToAnswer(mAnswer);
        }
    }

    @OnClick(R.id.fragment_layout_answer_item_minor_imagevie)
    public void onClickMinorOneButton(View v) {
        if(mAnswer != null) {
            AnswerHelper.addDiscreditToAnswer(mAnswer);
        }
    }

    public void updateWithData(Answer answer) {
        mAnswer = answer;
        mQuestionVote.setText( String.valueOf(answer.getWeight()) );
        mItemEntryAnswerTextview.setText(answer.getExplanation());
        UserHelper.getUserById(answer.getAuthorReference()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mItemEntryAuthorTextview.setText(documentSnapshot.getString("userName"));
            }
        });

        mItemEntryDateTextview.setText( getStringValueOfElapsedTimeSince(answer.getDateCreated()) );
    }

    private String getStringValueOfElapsedTimeSince(Timestamp date){
        if(date != null){
            long diff = Timestamp.now().toDate().getTime() - date.toDate().getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if(days >= 1){
                return String.valueOf(days) + " jrs";
            }

            if(hours >= 1){
                return String.valueOf(hours) + " hrs";
            }

            return String.valueOf(minutes) + " min";
        }

        return "";
    }
}
