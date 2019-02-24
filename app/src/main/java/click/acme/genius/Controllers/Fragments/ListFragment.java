package click.acme.genius.Controllers.Fragments;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import click.acme.genius.Adapters.QuestionListAdapter;
import click.acme.genius.Controllers.Activities.QuestionDetailActivity;
import click.acme.genius.Helpers.QuestionHelper;
import click.acme.genius.Models.Question;
import click.acme.genius.R;
import click.acme.genius.Utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment implements QuestionListAdapter.Listener {

    @BindView(R.id.fragment_layout_list_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_layout_list_notification_textview)
    TextView mNotificationText;

    private QuestionListAdapter mQuestionListAdapter;

    public ListFragment() {
    }

    @Override
    protected ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout_list;
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
        mQuestionListAdapter = new QuestionListAdapter(
                generateOptionsForAdapter(QuestionHelper.getQuestionsFromDatabase(50)),
                this
        );
        mQuestionListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mQuestionListAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mQuestionListAdapter);
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
