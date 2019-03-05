package click.acme.genius.Views;

import android.view.View;
import android.widget.TextView;
import com.google.firebase.Timestamp;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import click.acme.genius.Helpers.AnswerHelper;
import click.acme.genius.Models.Question;
import click.acme.genius.R;

public class QuestionListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_list_item_title_textview)
    TextView mItemEntryTitle;
    @BindView(R.id.fragment_layout_list_item_subject_textview)
    TextView mItemEntrySubject;
    @BindView(R.id.fragment_layout_list_item_date_textview)
    TextView mItemEntryDate;
    @BindView(R.id.fragment_layout_list_item_questionVote)
    TextView mQuestionVote;
    @BindView(R.id.fragment_layout_list_item_answerVote_textview)
    TextView mAnswerVote;

    public QuestionListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Question question) {
        mItemEntryTitle.setText(question.getTitle());
        mItemEntrySubject.setText(question.getSubject());
        mItemEntryDate.setText(getStringValueOfElapsedTimeSince(question.getDateCreated()));
        mQuestionVote.setText(String.valueOf( question.getWeight() ));
        AnswerHelper.getAnswersFromDatabase(question.getId()).get().addOnSuccessListener(
                queryDocumentSnapshots -> mAnswerVote.setText(String.valueOf( queryDocumentSnapshots.size() ))
        );
    }

    public static String getStringValueOfElapsedTimeSince(Timestamp date){
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
