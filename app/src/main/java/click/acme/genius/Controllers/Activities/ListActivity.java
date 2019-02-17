package click.acme.genius.Controllers.Activities;

import click.acme.genius.Controllers.Fragments.ListFragment;
import click.acme.genius.R;

public class ListActivity extends BaseActivity {
    ListFragment mListFragment;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected void preBindTreatment() {
        configureAndShowListFragment();
    }

    @Override
    protected void postCreateTreatment() {

    }

    private void configureAndShowListFragment() {
        try {
            mListFragment = (ListFragment) configureAndShowFragment(R.id.activity_list_frame_layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
