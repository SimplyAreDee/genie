package click.acme.genius.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import click.acme.genius.controllers.activities.AccountActivity;
import click.acme.genius.controllers.fragments.AccountDetailFragment;
import click.acme.genius.controllers.fragments.AccountQuestionsFragment;
import click.acme.genius.controllers.fragments.AccountRewardsFragment;

/**
 * Adapter pour le ViewPage utilisé {@link AccountActivity#configureViewPager() ici}
 */
public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position la position de la page dans le ViewPage
     * @return String le titre de la page
     */
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

    /**
     * Retourne le fragment correspondant à l'élément demandé dans le ViewPage
     * @param position la position de la page dans le menu du ViewPage
     * @return Fragment le fragment correspondant à la vue demandé
     */
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

    /**
     * Retourne le nombre de page de cet adapter
     * @return int le nombre de page
     */
    @Override
    public int getCount() {
        return 3;
    }
}
