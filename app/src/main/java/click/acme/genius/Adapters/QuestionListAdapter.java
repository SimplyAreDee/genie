package click.acme.genius.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.util.Listener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import click.acme.genius.Models.Question;
import click.acme.genius.R;
import click.acme.genius.Views.QuestionListItemViewHolder;

public class QuestionListAdapter  extends FirestoreRecyclerAdapter<Question, QuestionListItemViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Listener callback;

    public QuestionListAdapter(@NonNull FirestoreRecyclerOptions<Question> options, Listener callback) {
        super(options);
        this.callback = callback;
    }

    @NonNull
    @Override
    public QuestionListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_list_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionListItemViewHolder holder, int position, @NonNull Question model) {
        holder.updateWithData(model);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
