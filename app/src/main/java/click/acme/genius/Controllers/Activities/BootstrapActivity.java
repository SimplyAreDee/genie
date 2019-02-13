package click.acme.genius.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;
import click.acme.genius.Utils.ConnectivityCheck;

public class BootstrapActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;

    @BindView(R.id.bootstrap_activity_refresh_btn) Button mRetryConnectivityButton;
    @BindView(R.id.bootstrap_activity_login_btn) Button mConnexionButton;
    @BindView(R.id.bootstrap_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_bootstrap_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRetryConnectivityButton.setVisibility(View.INVISIBLE);
        mConnexionButton.setVisibility(View.INVISIBLE);
        checkConnectivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @OnClick(R.id.bootstrap_activity_refresh_btn)
    void onClickRetryConnectivityButton(View view) {
        checkConnectivity();
    }

    @OnClick(R.id.bootstrap_activity_login_btn)
    void onClickLoginButton(View view){
        startCreateAccountActivity();
    }

    private void checkConnectivity() {
        new ConnectivityCheck(internet -> {
            if(internet){
                startNextActivity();
            }else{
                mRetryConnectivityButton.setVisibility(View.VISIBLE);
                showNoConnexionError();
            }
        });
    }

    private void startNextActivity(){
        //TODO check if account created
        if(isCurrentUserLogged()) {
            mConnexionButton.setVisibility(View.INVISIBLE);
            startMainActivity();
        } else {
            mConnexionButton.setVisibility(View.VISIBLE);
            startCreateAccountActivity();
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(BootstrapActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startCreateAccountActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_genius_logo)
                        .build(),
                RC_SIGN_IN);
    }

    private void showNoConnexionError() {
        LayoutInflater myInflater = LayoutInflater.from(this);
        View view = myInflater.inflate(R.layout.toast_warning_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) view.findViewById(R.id.toast_layout_root_image);
        image.setImageResource(R.drawable.outline_warning_white_18dp);
        TextView text = (TextView) view.findViewById(R.id.toast_layout_root_text);
        text.setText(R.string.no_conn_error);

        Toast mytoast = new Toast( getApplicationContext() );
        mytoast.setView(view);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
    }

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
                startNextActivity();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }
}
