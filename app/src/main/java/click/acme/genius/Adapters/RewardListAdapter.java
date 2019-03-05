package click.acme.genius.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import click.acme.genius.Models.Reward;
import click.acme.genius.R;
import click.acme.genius.Views.RewardListItemViewHolder;

public class RewardListAdapter extends FirestoreRecyclerAdapter<Reward, RewardListItemViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private Listener callback;

    public RewardListAdapter(@NonNull FirestoreRecyclerOptions<Reward> options, Listener callback) {
        super(options);
        this.callback = callback;
    }

    @NonNull
    @Override
    public RewardListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_list_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull RewardListItemViewHolder holder, int position, @NonNull Reward model) {
        holder.updateWithData(model);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
