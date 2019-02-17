package click.acme.genius.Controllers.Activities;

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
import click.acme.genius.Controllers.Fragments.ListFragment;
import click.acme.genius.Controllers.Fragments.SearchFragment;
import click.acme.genius.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Vibrator mVibrator;

    protected abstract int getFragmentLayout();

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

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

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
                fragment = new ListFragment();
            }
            if (fragmentId == R.id.activity_search_frame_layout) {
                fragment = new SearchFragment();
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

    protected void preBindTreatment() {
        //Override me
    }

}
