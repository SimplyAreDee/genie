package click.acme.genius.Controllers.Activities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import click.acme.genius.Adapters.PageAdapter;
import click.acme.genius.R;

public class AccountActivity extends BaseActivity {

    @BindView(R.id.activity_account_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_account_tabs)
    TabLayout mTabLayout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_account;
    }

    @Override
    protected void postCreateTreatment() {
        configureViewPager();
    }

    private void configureViewPager(){
        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
