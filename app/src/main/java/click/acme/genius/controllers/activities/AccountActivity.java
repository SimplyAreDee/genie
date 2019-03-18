package click.acme.genius.controllers.activities;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import click.acme.genius.adapters.PageAdapter;
import click.acme.genius.R;

/**
 * Permet d'afficher les informations relatives au compte utilisateur à l'intérieur d'un ViewPager
 * et de fragment représentant les 3 parties de cette vue
 * {@link click.acme.genius.controllers.fragments.AccountDetailFragment AccountDetailFragment}
 * {@link click.acme.genius.controllers.fragments.AccountQuestionsFragment AccountQuestionsFragment}
 * {@link click.acme.genius.controllers.fragments.AccountRewardsFragment AccountRewardsFragment}
 */
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
