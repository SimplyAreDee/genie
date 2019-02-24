package click.acme.genius.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import click.acme.genius.Models.Answer;
import click.acme.genius.Models.Question;
import click.acme.genius.R;
import click.acme.genius.Views.QuestionDetailtemViewHolder;
import click.acme.genius.Views.QuestionListItemViewHolder;

public class QuestionDetailAdapter extends FirestoreRecyclerAdapter<Answer, QuestionDetailtemViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Listener callback;

    public QuestionDetailAdapter(@NonNull FirestoreRecyclerOptions<Answer> options, Listener callback) {
        super(options);
        this.callback = callback;
    }

    @NonNull
    @Override
    public QuestionDetailtemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionDetailtemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_answer_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionDetailtemViewHolder holder, int position, @NonNull Answer model) {
        holder.updateWithData(model);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
