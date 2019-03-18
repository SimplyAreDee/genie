package click.acme.genius.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.adapters.QuestionListAdapter;
import click.acme.genius.controllers.activities.QuestionDetailActivity;
import click.acme.genius.controllers.activities.SearchActivity;
import click.acme.genius.helpers.QuestionHelper;
import click.acme.genius.models.Question;
import click.acme.genius.R;
import click.acme.genius.utils.ItemClickSupport;

import static android.app.Activity.RESULT_OK;

/**
 * Affiche la liste des questions
 */
public class QuestionListFragment extends BaseFragment implements QuestionListAdapter.Listener {

    private static final int RC_SEARCH_FILTER_VALUES = 100;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    @BindView(R.id.fragment_layout_list_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_layout_list_notification_textview)
    TextView mNotificationText;

    @BindView(R.id.fragment_layout_list_progressLayout)
    RelativeLayout mProgressLayout;

    private QuestionListAdapter mQuestionListAdapter;
    private static Bundle mBundleRecyclerViewState;

    public QuestionListFragment() {
    }

    @Override
    protected QuestionListFragment newInstance() {
        return new QuestionListFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout_list;
    }

    @Override
    protected void configureDesign() {
        configureRecyclerView();
        configureOnClickRecyclerView();
        if(mQuestionListAdapter != null) {
            mQuestionListAdapter.startListening();
        }
    }

    @Override
    protected void updateDesign() {
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void configureRecyclerView() {
        FirebaseFirestore.setLoggingEnabled(true);
        initialiseRecyclerView(new QuestionListAdapter(
                generateOptionsForAdapter(QuestionHelper.getQuestionsFromDatabase(50)),
                this
        ));
    }

    private void initialiseRecyclerView(QuestionListAdapter questionListAdapter){
        mQuestionListAdapter = questionListAdapter;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mQuestionListAdapter.notifyDataSetChanged();
    }

    private FirestoreRecyclerOptions<Question> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query, Question.class)
                .setLifecycleOwner(this)
                .build();
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

    /**
     * Affiche la recherche
     */
    @OnClick(R.id.fragment_layout_list_floatingbutton)
    public void OnClickSearchButton(){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, RC_SEARCH_FILTER_VALUES);
    }

    @Override
    public void onDataChanged() {
        mProgressLayout.setVisibility(View.GONE);
        mNotificationText.setText("Pas de résultat");
        mNotificationText.setVisibility(mQuestionListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * On traite le retour du filtre de recherche
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SEARCH_FILTER_VALUES) {
            if (resultCode == RESULT_OK) {
                mProgressLayout.setVisibility(View.VISIBLE);
                Bundle bundle = data.getBundleExtra("searchform");

                initialiseRecyclerView(new QuestionListAdapter(
                        generateOptionsForAdapter(QuestionHelper.getFilteredQuestionsFromDatabase(50, bundle)),
                        this
                ));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mQuestionListAdapter.stopListening();
    }
}
