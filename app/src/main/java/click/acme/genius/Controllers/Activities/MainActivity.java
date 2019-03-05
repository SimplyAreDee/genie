package click.acme.genius.Controllers.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import butterknife.OnClick;
import click.acme.genius.Helpers.UserHelper;
import click.acme.genius.Models.User;
import click.acme.genius.R;

public class MainActivity extends BaseActivity {

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void postCreateTreatment() {
        FirebaseUser userCurrentlyLoggedIn = FirebaseAuth.getInstance().getCurrentUser();
        if(userCurrentlyLoggedIn != null) {
            FirebaseFirestore.getInstance().collection("users").document(userCurrentlyLoggedIn.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                           User user =  document.toObject(User.class);

                           user.setEmailAdress(userCurrentlyLoggedIn.getEmail());//On recupère toujours l'adresse de firebase.auth
                           UserHelper.saveState(user);

                           User.setCurrentUser(user);
                        }else{
                            User.setCurrentUser(new User(userCurrentlyLoggedIn));

                            UserHelper.saveState(User.getCurrentUser());
                        }
                    }else{
                        User.setCurrentUser(new User(userCurrentlyLoggedIn));

                        UserHelper.saveState(User.getCurrentUser());
                    }
                }
            });
        }
    }

    @OnClick(R.id.main_activity_ask_help_btn)
    void OnClickAskHelpButton(){
        Intent intent = new Intent(MainActivity.this, AskUsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_give_help_btn)
    void OnClickGiveHelpButton() {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_discuss_btn)
    void OnClickDiscussButton() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_activity_profil_imageView)
    void OnClickAccountButton() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
    }
}
