package click.acme.genius.controllers.activities;

import android.content.Intent;
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
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import java.util.Arrays;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;
import click.acme.genius.utils.ConnectivityCheck;

/**
 * Activité Bootloader de l'application, effectue les vérifications concernant les prérequis de lancement
 * de l'application (connection internet, authentification)
 */
public class BootstrapActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;

    @BindView(R.id.bootstrap_activity_refresh_btn) Button mRetryConnectivityButton;
    @BindView(R.id.bootstrap_activity_login_btn) Button mConnexionButton;
    @BindView(R.id.bootstrap_activity_coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_bootstrap_screen;
    }

    @Override
    protected void postCreateTreatment() {
        mRetryConnectivityButton.setVisibility(View.INVISIBLE);
        mConnexionButton.setVisibility(View.INVISIBLE);
        //Initialisation de l'API MobileAds
        MobileAds.initialize(this, "ca-app-pub-8678425123175797~4620846413");
        //Vérifie si l'on possède d'une connexion.
        checkConnectivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //On va vérifier l'état de l'authentification
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    /**
     * Listener sur le bouton permettant de revérifier sa connectivité
     * @param view l'élément à l'origine de l'event
     */
    @OnClick(R.id.bootstrap_activity_refresh_btn)
    void onClickRetryConnectivityButton(View view) {
        checkConnectivity();
    }

    /**
     * Listener sur le bouton permettant d'afficher l'activité de connection
     * utile en cas d'annulation de la première authentification par l'utilisateur
     * @param view l'élément à l'origine de l'event
     */
    @OnClick(R.id.bootstrap_activity_login_btn)
    void onClickLoginButton(View view){
        startCreateAccountActivity();
    }

    /**
     * Vérifie la connectivité de l'application
     * Si il n'y a pas de connexion on affiche une popup et on rend le bouton permettant de revérifier
     * disponible
     * //TODO Pourrait être amélioré en souscrivant à un Service
     */
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

    /**
     * En fonction du statut actuel démarre soit le menu principal soit la création de compte
     */
    private void startNextActivity(){
        //TODO check if account created
        if(isCurrentUserLogged()) {
            mConnexionButton.setVisibility(View.INVISIBLE);
            startMainActivity();
        } else {
            mConnexionButton.setVisibility(View.VISIBLE);
            mConnexionButton.setEnabled(true);
            mConnexionButton.setFocusable(true);
            startCreateAccountActivity();
        }
    }

    /**
     * Démarre le menu principal
     */
    private void startMainActivity(){
        Intent intent = new Intent(BootstrapActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Permet de créer un nouveau compte
     * Utilise le provider GoogleAuth
     * //TODO implémenter le provider Facebook
     */
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

    /**
     * Affiche un message indiquant qu'il n'y a pas de connection actuellement
     */
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

    /**
     * Permet d'afficher le retour de l'authentification
     * @param coordinatorLayout le layout contenant la snackBar
     * @param message le message à afficher
     */
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Déclenchée sur le retour de l'authentification.
     * @param requestCode int l'id de l'activité
     * @param resultCode int le resultat
     * @param data int les données attachées
     */
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.mCoordinatorLayout, getString(R.string.connection_succeed));
                startNextActivity();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }
}
