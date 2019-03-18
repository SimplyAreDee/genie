package click.acme.genius.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import click.acme.genius.models.Answer;
import click.acme.genius.R;
import click.acme.genius.views.QuestionDetailtemViewHolder;

/**
 * Adapter pour connecter l'affichage des réponses dans {@link click.acme.genius.controllers.fragments.QuestionDetailFragment#configureRecyclerView(String) configureRecyclerView}
 * avec le modele {@link click.acme.genius.models.Answer Answer}
 */
public class QuestionDetailAdapter extends FirestoreRecyclerAdapter<Answer, QuestionDetailtemViewHolder> {

    /**
     * Interface permettant de notifier à la classe implémentant l'Adapter que des données ont changé
     * en implémentant {@link QuestionDetailAdapter.Listener Listener}
     */
    public interface Listener {
        void onDataChanged();
    }

    //callback qui sera appellé quand onDataChanged est déclenché
    private Listener callback;

    /**
     * @param  options FirestoreRecyclerOptions<Answer> Correspond au dataSet, encapsuler dans un FirestoreRecyclerOptions.
     * @param callback QuestionDetailAdapter.Listener callback qui sera appelé lors du déclenchement de {@link QuestionDetailAdapter#onDataChanged() onDataChanged}
     */
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

    /**
     * Est appelé pour mettre à jour l'élément de la liste sur un appel de getView()
     * permet d'économiser des ressources (mémoire, batterie)
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html">Google RecyclerView.ViewHolder documentation</a>
     * @param holder QuestionDetailtemViewHolder viewHolder
     * @param position int position de l'élément en cours de dessin
     * @param model Answer les données attachées à l'élément en cours d'affichage
     */
    @Override
    protected void onBindViewHolder(@NonNull QuestionDetailtemViewHolder holder, int position, @NonNull Answer model) {
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
