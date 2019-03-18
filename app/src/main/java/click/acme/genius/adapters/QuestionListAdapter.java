package click.acme.genius.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import click.acme.genius.controllers.fragments.QuestionListFragment;
import click.acme.genius.models.Question;
import click.acme.genius.R;
import click.acme.genius.views.QuestionListItemViewHolder;

/**
 * Adapter pour connecter l'affichage des questions dans {@link QuestionListFragment#configureRecyclerView() configureRecyclerView}
 * ou encore dans  {@link click.acme.genius.controllers.fragments.AccountQuestionsFragment#configureRecyclerView() configureRecyclerView}
 * avec le modele {@link click.acme.genius.models.Question Question}
 */
public class QuestionListAdapter  extends FirestoreRecyclerAdapter<Question, QuestionListItemViewHolder> {

    /**
     * Interface permettant de notifier à la classe implémentant l'Adapter que des données ont changé
     * en implémentant {@link QuestionListAdapter.Listener Listener}
     */
    public interface Listener {
        void onDataChanged();
    }

    //callback qui sera appellé quand onDataChanged est déclenché
    private Listener callback;

    /**
     * @param  options FirestoreRecyclerOptions<Question> Correspond au dataSet, encapsuler dans un FirestoreRecyclerOptions.
     * @param callback QuestionListAdapter.Listener callback qui sera appelé lors du déclenchement de {@link QuestionListAdapter#onDataChanged() onDataChanged}
     */
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

    /**
     * Est appelé pour mettre à jour l'élément de la liste sur un appel de getView()
     * permet d'économiser des ressources (mémoire, batterie)
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html">Google RecyclerView.ViewHolder documentation</a>
     * @param holder QuestionListItemViewHolder viewHolder
     * @param position int position de l'élément en cours de dessin
     * @param model Question les données attachées à l'élément en cours d'affichage
     */
    @Override
    protected void onBindViewHolder(@NonNull QuestionListItemViewHolder holder, int position, @NonNull Question model) {
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
