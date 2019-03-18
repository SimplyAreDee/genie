package click.acme.genius.controllers.fragments;

import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import click.acme.genius.R;

/**
 * Affiche la boite déroulante permettant de répondre à une question
 */
public class AnswerFragment extends BaseDialogFragment {

    @BindView(R.id.fragment_layout_answer_textview)
    TextView mAnswerTextView;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseDialogFragment newInstance() {
        return new AnswerFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_answer;
    }

    @Override
    protected void configureDesign() {
        String title = getArguments().getString("fragment_answer_title", "Inscris ta réponse");

        getDialog().setTitle(title);

        mAnswerTextView.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    protected void updateDesign() {

    }
}
