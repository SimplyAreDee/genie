package click.acme.genius.controllers.activities;

import android.content.Intent;
import click.acme.genius.controllers.fragments.QuestionListFragment;
import click.acme.genius.R;

/**
 * Affiche la liste de toutes les questions posées, la logique se trouve
 * {@link QuestionListFragment QuestionListFragment}
 */
public class QuestionListActivity extends BaseActivity {
    QuestionListFragment mListFragment;

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
            mListFragment = (QuestionListFragment) configureAndShowFragment(R.id.activity_list_frame_layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }
}
