package click.acme.genius.controllers.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import click.acme.genius.adapters.QuestionListAdapter;
import click.acme.genius.controllers.activities.QuestionDetailActivity;
import click.acme.genius.helpers.QuestionHelper;
import click.acme.genius.models.Question;
import click.acme.genius.models.User;
import click.acme.genius.R;
import click.acme.genius.utils.ItemClickSupport;

/**
 * Affiche la liste des questions que l'utilisateur a posé
 */
public class AccountQuestionsFragment extends BaseFragment  implements QuestionListAdapter.Listener {

    @BindView(R.id.fragment_account_questions_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_account_questions_notification_textview)
    TextView mNotificationText;

    private QuestionListAdapter mQuestionListAdapter;

    public AccountQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseFragment newInstance() {
        return new AccountQuestionsFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_account_questions;
    }

    @Override
    protected void configureDesign() {
        configureRecyclerView();
        configureOnClickRecyclerView();
    }

    @Override
    protected void updateDesign() {

    }

    private void configureRecyclerView() {
        FirebaseFirestore.setLoggingEnabled(true);

        User user = User.getCurrentUser();
        if(user != null) {
            //On affiche que les 50 dernières
            //TODO Ajouter le tri sur le timestamp
            mQuestionListAdapter = new QuestionListAdapter(
                    generateOptionsForAdapter(QuestionHelper.getQuestionsByUserReferenceFromDatabase(50, user.getId())),
                    this
            );
            mQuestionListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mRecyclerView.smoothScrollToPosition(mQuestionListAdapter.getItemCount());
                }
            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mQuestionListAdapter);
        }
    }

    private FirestoreRecyclerOptions<Question> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query, Question.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged() {
        mNotificationText.setText("Pas de résultat");
        mNotificationText.setVisibility(mQuestionListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_layout_list_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Question question = mQuestionListAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                    String questionReference = question.getId();
                    intent.putExtra("questionReference", questionReference);

                    startActivity(intent);
                });
    }

}
