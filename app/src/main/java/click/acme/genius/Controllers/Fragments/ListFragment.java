package click.acme.genius.Controllers.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import click.acme.genius.Adapters.QuestionListAdapter;
import click.acme.genius.R;
import click.acme.genius.Utils.FakeQuestionList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {

    @BindView(R.id.fragment_layout_list_recycler_view)
    RecyclerView mRecyclerView;
    QuestionListAdapter mQuestionListAdapter;

    public ListFragment() {
        // Required empty public constructor
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
    }

    @Override
    protected void updateDesign() {

    }

    private void configureRecyclerView() {
        //Track current chat name
        //Configure Adapter & RecyclerView
        mQuestionListAdapter = new QuestionListAdapter(FakeQuestionList.getFakeQuestionList(20));
        mQuestionListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mQuestionListAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mQuestionListAdapter);
    }
}
