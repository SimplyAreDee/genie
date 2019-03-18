package click.acme.genius.controllers.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import click.acme.genius.controllers.fragments.QuestionListFragment;
import click.acme.genius.controllers.fragments.QuestionDetailFragment;
import click.acme.genius.controllers.fragments.SearchFragment;
import click.acme.genius.R;

/**
 * Activité générique hérité de chaque activité
 * Permet de rassembler les éléments communs à chaque activité et d'implémenter des comportement
 * commun pour chacune des activités.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Vibrator mVibrator;

    /**
     * Retourne le layout utilisé par le fragment/activity
     * @return int le layout correspondant au fragment/activity
     */
    protected abstract int getFragmentLayout();

    /**
     * Appelé à la fin du onCreate une fois que le contentView a été effectué et que la liaison
     * avec les elements du layout a été faite.
     */
    protected abstract void postCreateTreatment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getFragmentLayout());

        preBindTreatment();

        ButterKnife.bind(this);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        postCreateTreatment();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /**
     * Permet de gèrer les retours d'erreurs de l'API firebase.
     * @return OnFailureListener le listener ecoutant pour les erreurs retournées par Firestore/Firebase
     */
    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Retourne l'utilisateur connecté avec l'API Auth de Firebase.
     * @return FirebaseUser Utilisateur connecté avec Firebase.Auth
     */
    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    /**
     * Indique si l'utilisateur est authentifié
     * @return Boolean l'utilisateur est (pas?)connecté
     */
    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    /**
     * Permet d'effectuer une notification à l'aide du vibreur
     * @param duration durée de la vibration
     */
    protected void notifyWithVibration(int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            mVibrator.vibrate(duration);
        }
    }

    /**
     * Usine permettant de créer et d'ajouter au FragmentManager des fragments
     *
     * @param fragmentId l'id du fragment issu de R.layout
     * @return Un fragment typé en fonction du type à construire (MainFragment,DetailFragment etc...)
     * @throws Exception Si l'id du fragment n'est pas géré par l'usine.
     */
    protected Fragment configureAndShowFragment(int fragmentId) throws Exception {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(fragmentId);
        if (fragment == null) {
            if (fragmentId == R.id.activity_list_frame_layout) {
                fragment = new QuestionListFragment();
            }
            if (fragmentId == R.id.activity_search_frame_layout) {
                fragment = new SearchFragment();
            }
            if(fragmentId == R.id.activity_question_detail_frame_layout){
                fragment = new QuestionDetailFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(fragmentId, fragment)
                        .commit();
            } else {
                throw new Exception("Fragment id not handled at this time");
            }

        }

        return fragment;
    }

    /**
     * Permet d'effectuer un traitement avant la liaison avec le Layout
     * doit être redéfinie dans la classe enfant.
     */
    protected void preBindTreatment() { }

}
