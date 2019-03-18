package click.acme.genius.controllers.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.helpers.UserHelper;
import click.acme.genius.models.User;
import click.acme.genius.R;
import click.acme.genius.services.StepCounterService;

/**
 * Affiche le menu principal de l'application
 */
public class MainActivity extends BaseActivity {

    public static final String sBroadcastIntegerAction = "click.acme.genius.broadcast.integer";

    @BindView(R.id.main_activity_progress_cardview)
    CardView mProgressCardView;
    @BindView(R.id.main_activity_secretfeature)
    TextView mSecretFeatureTextView;

    private IntentFilter mIntentFilter;
    private SharedPreferences mSharedPref;
    private FirebaseUser mUserCurrentlyLoggedIn;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void postCreateTreatment() {
        mSharedPref = getPreferences(Context.MODE_PRIVATE);

        restoreSecretFeatureValue();

        mProgressCardView.setVisibility(View.VISIBLE);

        startServiceIfStepSensorAviable();

        mUserCurrentlyLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        //Si l'utilisateur est acutellement connecté avec Firebase.Auth, on va mettre à jour son profil
        if(mUserCurrentlyLoggedIn != null) {
            FirebaseFirestore.getInstance().collection("users").document(
                    mUserCurrentlyLoggedIn.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    setCurrentUserStaticValue(task);

                    mProgressCardView.setVisibility(View.GONE);
                }
            });
        }
    }

    //Instancie l'utilisateur dans la valeur static de la classe User
    //TODO : peut être que la logique de mise à jour n'est pas correct ici.
    private void setCurrentUserStaticValue(Task<DocumentSnapshot> task) {
        if(task.isSuccessful()){
            DocumentSnapshot document = task.getResult();
            if(document != null && document.exists()){
                User user =  document.toObject(User.class);
                if(user != null) {
                    if (mUserCurrentlyLoggedIn.getEmail() != null) {
                        //On recupère toujours l'adresse de firebase.auth
                        user.setEmailAddress(mUserCurrentlyLoggedIn.getEmail());
                    }
                    UserHelper.saveState(user);

                    User.setCurrentUser(user);

                    return;
                }
            }
        }
        //Si on a pas réussi à récuperer l'utilisateur, il n'existe pas donc on le créer.
        User.setCurrentUser(new User(mUserCurrentlyLoggedIn));

        UserHelper.saveState(User.getCurrentUser());
    }

    @OnClick(R.id.main_activity_ask_help_btn)
    void OnClickAskHelpButton(){
        Intent intent = new Intent(MainActivity.this, QuestionAskActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_give_help_btn)
    void OnClickGiveHelpButton() {
        Intent intent = new Intent(MainActivity.this, QuestionListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_discuss_btn)
    void OnClickDiscussButton() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_profil_imageView)
    void OnClickAccountButton() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    //Toute la partie qui se trouve en dessous n'est pas utilisé actuellement, elle est en place
    //pour une prochaine feature, mais en attendant on enregistre quand même les valeurs.

    /**
     * Actuellement ce n'est pas utilisé, un feature pour une prochaine mise à jour
     * mais en attendant on garde les valeurs.
     */
    private void startServiceIfStepSensorAviable() {
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(sBroadcastIntegerAction);

            Intent serviceIntent = new Intent(MainActivity.this, StepCounterService.class);
            startService(serviceIntent);
        }
    }

    private void restoreSecretFeatureValue() {
        int steps = mSharedPref.getInt("steps", 0);

        mSecretFeatureTextView.setText(String.valueOf(steps));
    }

    @Override
    protected void onPause() {
        if(mReceiver != null) {
            try {

                unregisterReceiver(mReceiver);

                SharedPreferences.Editor editor = mSharedPref.edit();

                int steps = Integer.parseInt(mSecretFeatureTextView.getText().toString());

                editor.putInt("steps", steps);

                editor.commit();
            } catch(IllegalArgumentException e) {
                //Il se peut que le service n'est pas été inscrit alors qu'on essaye de le
                //désinscrire, cas rencontré pendant les tests
                e.printStackTrace();
            }

        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mReceiver != null && mIntentFilter != null) {
            registerReceiver(mReceiver, mIntentFilter);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(sBroadcastIntegerAction)) {
                int steps = intent.getIntExtra("steps",0);

                mSecretFeatureTextView.setText(String.valueOf(steps));
            }
        }
    };
}
