package click.acme.genius.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import click.acme.genius.models.Chat;
import click.acme.genius.R;
import click.acme.genius.views.ChatGroupListItemViewHolder;

/**
 * Adapter pour connecter l'affichage des bulles de chat dans {@link click.acme.genius.controllers.activities.ChatActivity#configureRecyclerView() configureRecyclerView}
 * avec le modele {@link click.acme.genius.models.Chat Chat}
 */
public class ChatListAdapter extends FirestoreRecyclerAdapter<Chat, ChatGroupListItemViewHolder> {

    /**
     * Interface permettant de notifier à la classe implémentant l'Adapter que des données ont changé
     * en implémentant {@link ChatListAdapter.Listener Listener}
     */
    public interface Listener {
        void onDataChanged();
    }

    //callback qui sera appellé quand onDataChanged est déclenché
    private Listener callback;

    /**
     * @param  options FirestoreRecyclerOptions<Chat> Correspond au dataSet, encapsuler dans un FirestoreRecyclerOptions.
     * @param callback ChatListAdapter.Listener callback qui sera appelé lors du déclenchement de {@link ChatListAdapter#onDataChanged() onDataChanged}
     */
    public ChatListAdapter(@NonNull FirestoreRecyclerOptions<Chat> options, Listener callback) {
        super(options);
        this.callback = callback;
    }

    @NonNull
    @Override
    public ChatGroupListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatGroupListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_chat_group_item, parent, false));
    }

    /**
     * Est appelé pour mettre à jour l'élément de la liste sur un appel de getView()
     * permet d'économiser des ressources (mémoire, batterie)
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html">Google RecyclerView.ViewHolder documentation</a>
     * @param holder ChatGroupListItemViewHolder viewHolder
     * @param position int position de l'élément en cours de dessin
     * @param model Chat les données attachées à l'élément en cours d'affichage
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatGroupListItemViewHolder holder, int position, @NonNull Chat model) {
        holder.updateWithData(model);
    }

    /**
     * Permet de faire remonter que le dataSet à changé au controller qui instancie cette classe.
     */
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
