package click.acme.genius.controllers.activities;

import click.acme.genius.controllers.fragments.SearchFragment;
import click.acme.genius.R;
import android.content.Intent;
import android.os.Bundle;

/**
 * Affiche la vue servant de filtre pour les questions
 * //TODO implémenter le livesearch, et la recherche avancé avec Algolia ou ES.
 *  Aujourd'hui Firestore pose problème pour ce type de recherche et l'utilisation d'Algolia n'est
 *  pas facilité à cause de la difficulté d'effectuer une Query avec les ids à récuperer coté Firestore
 *  On pourrait se passer du Recycler view et utiliser un pull to refresh pour garder la possibilité d'obtenir
 *  les dernières informations
 *
 * @see click.acme.genius.controllers.fragments.SearchFragment SearchFragment
 */
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
