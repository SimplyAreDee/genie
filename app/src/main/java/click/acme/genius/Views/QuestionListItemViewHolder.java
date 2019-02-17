package click.acme.genius.Views;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import click.acme.genius.Models.Question;
import click.acme.genius.R;

public class QuestionListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_layout_list_item_entry_title)
    TextView mItemEntryTitle;
    @BindView(R.id.fragment_layout_list_item_entry_page)
    TextView mItemEntryPage;
    @BindView(R.id.fragment_layout_list_item_entry_number)
    TextView mItemEntryNumber;
    @BindView(R.id.fragment_layout_list_item_entry_subject)
    TextView mItemEntrySubject;

    public QuestionListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Question question) {
        mItemEntryTitle.setText(question.getTitle());
        mItemEntryPage.setText(question.getPage());
        mItemEntryNumber.setText(question.getNumber());
        mItemEntrySubject.setText(question.getSubject());
    }
}
