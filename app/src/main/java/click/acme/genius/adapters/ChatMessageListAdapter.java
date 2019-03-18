package click.acme.genius.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import click.acme.genius.models.ChatMessage;
import click.acme.genius.R;
import click.acme.genius.views.ChatMessageListItemViewHolder;

/**
 * Adapter pour connecter l'affichage des messages dans {@link click.acme.genius.controllers.activities.ChatDetailActivity#configureRecyclerView() configureRecyclerView}
 * avec le modele {@link click.acme.genius.models.ChatMessage ChatMessage}
 */
public class ChatMessageListAdapter extends FirestoreRecyclerAdapter<ChatMessage, ChatMessageListItemViewHolder> {

    private final RequestManager mGlide;
    private Listener mCallback;

    /**
     * Interface permettant de notifier à la classe implémentant l'Adapter que des données ont changé
     * en implémentant {@link ChatMessageListAdapter.Listener Listener}
     */
    public interface Listener {
        void onDataChanged();
    }

    /**
     * @param  options FirestoreRecyclerOptions<ChatMessage> Correspond au dataSet, encapsuler dans un FirestoreRecyclerOptions.
     * @param callback ChatMessageListAdapter.Listener callback qui sera appelé lors du déclenchement de {@link ChatMessageListAdapter#onDataChanged() onDataChanged}
     */
    public ChatMessageListAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, RequestManager glide, Listener callback) {
        super(options);
        mGlide = glide;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ChatMessageListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_layout_chat_detail_item, parent, false));
    }

    /**
     * Est appelé pour mettre à jour l'élément de la liste sur un appel de getView()
     * permet d'économiser des ressources (mémoire, batterie)
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html">Google RecyclerView.ViewHolder documentation</a>
     * @param holder ChatMessageListItemViewHolder viewHolder
     * @param position int position de l'élément en cours de dessin
     * @param model ChatMessage les données attachées à l'élément en cours d'affichage
     */
    @Override
    protected void onBindViewHolder(@NonNull ChatMessageListItemViewHolder holder, int position, @NonNull ChatMessage model) {
        holder.updateWithData(model, mGlide);
    }

    /**
     * Permet de faire remonter que le dataSet à changé au controller qui instancie cette classe.
     */
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        mCallback.onDataChanged();
    }
}
