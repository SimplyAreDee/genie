package click.acme.genius.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import click.acme.genius.controllers.fragments.QuestionDetailFragment;
import click.acme.genius.R;


/**
 * Affiche le détail d'une question, la logique se trouve dans le fragment
 * {@link click.acme.genius.controllers.fragments.QuestionDetailFragment QuestionDetailFragment}
 * //TODO ajouter le traitement deeplink
 */
public class QuestionDetailActivity extends BaseActivity {
    String mQuestionReference;
    QuestionDetailFragment mQuestionDetailFragment;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void postCreateTreatment() {
        configureAndShowListFragment();
    }

    private void configureAndShowListFragment() {
        try {
            mQuestionDetailFragment = (QuestionDetailFragment) configureAndShowFragment(R.id.activity_question_detail_frame_layout);

            Intent intent = getIntent();
            //On récupère la référence de la question pour la passer au fragment.
            mQuestionReference = intent.getStringExtra("questionReference");

            Bundle bundle = new Bundle();
            bundle.putString("questionReference", mQuestionReference);

            mQuestionDetailFragment.setArguments(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
