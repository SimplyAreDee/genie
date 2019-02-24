package click.acme.genius.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import click.acme.genius.Controllers.Fragments.QuestionDetailFragment;
import click.acme.genius.R;

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
            mQuestionReference = intent.getStringExtra("questionReference");

            Bundle bundle = new Bundle();
            bundle.putString("questionReference", mQuestionReference);

            mQuestionDetailFragment.setArguments(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
