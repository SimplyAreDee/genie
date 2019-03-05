package click.acme.genius.Controllers.Fragments;

import android.view.View;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import click.acme.genius.Adapters.RewardListAdapter;
import click.acme.genius.Helpers.RewardHelper;
import click.acme.genius.Models.Reward;
import click.acme.genius.Models.User;
import click.acme.genius.R;

public class AccountRewardsFragment extends BaseFragment implements RewardListAdapter.Listener {

    @BindView(R.id.fragment_account_rewards_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_account_rewards_notification_textview)
    TextView mNotificationText;

    private RewardListAdapter mRewardListAdapter;

    public AccountRewardsFragment() {}

    @Override
    protected BaseFragment newInstance() {
        return new AccountRewardsFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_account_rewards;
    }

    @Override
    protected void configureDesign() {
        configureRecyclerView();
    }

    @Override
    protected void updateDesign() {

    }

    private void configureRecyclerView() {
        FirebaseFirestore.setLoggingEnabled(true);

        User user = User.getCurrentUser();

        mRewardListAdapter = new RewardListAdapter(
                generateOptionsForAdapter(RewardHelper.getUserRewardsFromDatabase(50, user.getId())),
                this
        );
        mRewardListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mRewardListAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRewardListAdapter);
    }

    private FirestoreRecyclerOptions<Reward> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Reward>()
                .setQuery(query, Reward.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged() {
        mNotificationText.setText("Pas de résultat");
        mNotificationText.setVisibility(mRewardListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
