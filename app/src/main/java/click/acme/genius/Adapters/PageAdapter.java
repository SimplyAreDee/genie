package click.acme.genius.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import click.acme.genius.Controllers.Fragments.AccountDetailFragment;
import click.acme.genius.Controllers.Fragments.AccountQuestionsFragment;
import click.acme.genius.Controllers.Fragments.AccountRewardsFragment;

public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "Profile";
            case 1: //Page number 2
                return "Questions";
            case 2: //Page number 3
                return "Badges";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AccountDetailFragment();
            case 1:
                return new AccountQuestionsFragment();
            case 2:
                return new AccountRewardsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
