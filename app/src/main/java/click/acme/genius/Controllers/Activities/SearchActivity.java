package click.acme.genius.Controllers.Activities;

import androidx.fragment.app.Fragment;
import click.acme.genius.Controllers.Fragments.SearchFragment;
import click.acme.genius.R;
import android.content.Intent;
import android.os.Bundle;

public class SearchActivity extends BaseActivity implements SearchFragment.OnDataSearchValidate {

    SearchFragment mListFragment;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_search;
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
            mListFragment = (SearchFragment) configureAndShowFragment(R.id.activity_search_frame_layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSearchValidate(Bundle data) {
        Intent intent = new Intent();
        intent.putExtra("searchform", data);

        setResult(RESULT_OK,intent);

        finish();
    }
}
