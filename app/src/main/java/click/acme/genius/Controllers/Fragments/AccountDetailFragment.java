package click.acme.genius.Controllers.Fragments;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import click.acme.genius.Models.User;
import click.acme.genius.R;
import click.acme.genius.Utils.ScholarLevel;

public class AccountDetailFragment extends BaseFragment {

    private User mUser;

    @BindView(R.id.fragment_account_detail_profilAvatar_imageView)
    ImageView mProfilPicturImageview;
    @BindView(R.id.fragment_account_detail_pseudonym_edittext)
    TextView mPseudonymTextview;
    @BindView(R.id.fragment_account_detail_firstname_edittext)
    TextView mFirstnameTextview;
    @BindView(R.id.fragment_account_detail_lastname_edittext)
    TextView mLastnameTextview;
    @BindView(R.id.fragment_account_detail_address_edittext)
    TextView mAddressTextview;
    @BindView(R.id.fragment_account_detail_scholarClass_edittext)
    TextView mScholarClassTextview;
    @BindView(R.id.fragment_account_detail_establishment_edittext)
    TextView mEstablishmentTextview;
    @BindView(R.id.fragment_account_detail_askedCount_edittext)
    TextView mAskedCountTextview;
    @BindView(R.id.fragment_account_detail_answerCount_edittext)
    TextView mAnswerCountTextview;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseFragment newInstance() {
        return new AccountDetailFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_account_detail;
    }

    @Override
    protected void configureDesign() {
        mUser = User.getCurrentUser();

        displayUserInformation();
    }

    @Override
    protected void updateDesign() {

    }

    private void displayUserInformation() {
        if(mUser != null) {
            mPseudonymTextview.setText(mUser.getUserName());
            mFirstnameTextview.setText(mUser.getUserFirstName());
            mLastnameTextview.setText(mUser.getUserLastName());
            mAddressTextview.setText(mUser.getEmailAddress());
            mScholarClassTextview.setText(ScholarLevel.FromIntToString(mUser.getScholarLevel()));
            mEstablishmentTextview.setText(mUser.getEstablishment());
            mAskedCountTextview.setText(String.valueOf(mUser.getAskedCount()));
            mAnswerCountTextview.setText(String.valueOf(mUser.getAnsweredCount()));

            Glide.with(getContext())
                    .load(mUser.getAvatarUrl())
                    .into(mProfilPicturImageview);
        }
    }
}
