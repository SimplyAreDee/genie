package click.acme.genius.Controllers.Activities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.Helpers.UserHelper;
import click.acme.genius.Models.User;
import click.acme.genius.R;

public class AccountActivity extends BaseActivity {

    private User mUser;

    @BindView(R.id.activity_account_coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_account_profilAvatar_imageView)
    ImageView mProfilPicturImageview;
    @BindView(R.id.activity_account_update_button)
    ImageView mUpdateButtonImageView;
    @BindView(R.id.activity_account_pseudonym_edittext)
    TextView mPseudonymTextview;
    @BindView(R.id.activity_account_firstname_edittext)
    TextView mFirstnameTextview;
    @BindView(R.id.activity_account_lastname_edittext)
    TextView mLastnameTextview;
    @BindView(R.id.activity_account_address_edittext)
    TextView mAddressTextview;
    @BindView(R.id.activity_account_scholarClass_edittext)
    TextView mScholarClassTextview;
    @BindView(R.id.activity_account_establishment_edittext)
    TextView mEstablishmentTextview;
    @BindView(R.id.activity_account_askedCount_edittext)
    TextView mAskedCountTextview;
    @BindView(R.id.activity_account_answerCount_edittext)
    TextView mAnswerCountTextview;
    @BindView(R.id.activity_account_accountStatus_textview)
    TextView mAccountStatusTextview;
    @BindView(R.id.activity_account_rewardBadges_listview)
    ListView mAccountStatusListview;


    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_account;
    }

    @Override
    protected void postCreateTreatment() {
        mUser = User.getCurrentUser();

        displayUserInformation();

        toggleEditableTextView();
    }

    private void editProfil(){
        toggleEditableTextView();

        mUpdateButtonImageView.setImageResource(R.drawable.outline_refresh_white_18dp);
    }

    private void displayUserInformation() {
        if(mUser != null) {
            mPseudonymTextview.setText(mUser.getUserName());
            mFirstnameTextview.setText(mUser.getUserFirstName());
            mLastnameTextview.setText(mUser.getUserLastName());
            mAddressTextview.setText(mUser.getEmailAddress());
            mScholarClassTextview.setText(mUser.getScholarClass());
            mEstablishmentTextview.setText(mUser.getEstablishment());
            mAskedCountTextview.setText(String.valueOf(mUser.getAskedCount()));
            mAnswerCountTextview.setText(String.valueOf(mUser.getAnsweredCount()));
            mAccountStatusTextview.setText("Normal"); //String.valueOf( mUser.getAccountStatus() )

            Glide.with(getApplicationContext())
                    .load(mUser.getAvatarUrl())
                    .into(mProfilPicturImageview);

            //Readonly
            mAddressTextview.setEnabled(false);
            mAccountStatusTextview.setEnabled(false);
            mAskedCountTextview.setEnabled(false);
            mAnswerCountTextview.setEnabled(false);
        }else{
            finish();
        }
    }

    @OnClick(R.id.activity_account_update_button)
    public void OnUpdateClick(){
        if(mFirstnameTextview.isEnabled()){
            mUpdateButtonImageView.setImageResource(R.drawable.outline_edit_white_18dp);

            updateUserProfile();

            UserHelper.saveState(mUser);

            User.setCurrentUser(mUser);

            Snackbar.make(mCoordinatorLayout,
                    "Profil mis à jour", Snackbar.LENGTH_LONG).show();
        }else{
            editProfil();
        }

    }

    private void updateUserProfile() {
        mUser.setUserName(mPseudonymTextview.getText().toString());
        mUser.setUserFirstName(mFirstnameTextview.getText().toString());
        mUser.setUserLastName(mLastnameTextview.getText().toString());
        mUser.setScholarClass(mScholarClassTextview.getText().toString());
        mUser.setEstablishment(mEstablishmentTextview.getText().toString());
        mUser.setAskedCount( Integer.parseInt( mAskedCountTextview.getText().toString() ));
        mUser.setAnsweredCount( Integer.parseInt( mAnswerCountTextview.getText().toString() ));

        toggleEditableTextView();
    }

    private void toggleEditableTextView(){
        mPseudonymTextview.setEnabled(!mPseudonymTextview.isEnabled());
        mFirstnameTextview.setEnabled(!mFirstnameTextview.isEnabled());
        mLastnameTextview.setEnabled(!mLastnameTextview.isEnabled());
        mScholarClassTextview.setEnabled(!mScholarClassTextview.isEnabled());
        mEstablishmentTextview.setEnabled(!mEstablishmentTextview.isEnabled());
    }

    @Override
    protected void onResume() {
        super.onResume();
        View focused = getCurrentFocus();
        if (focused != null && focused.isEnabled()) {
            focused.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }
    }
}
