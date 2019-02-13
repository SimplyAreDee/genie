package click.acme.genius.Controllers.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import click.acme.genius.R;
import click.acme.genius.Utils.ConnectivityCheck;

public class BootstrapActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootstrap_screen);

        checkConnectivity();

    }

    private void checkConnectivity() {
        new ConnectivityCheck(internet -> {
            if(internet){
                startNextActivity();
            }else{
                showNoConnexionError();
            }
        });
    }

    private void startNextActivity(){
        //TODO check if account created
        //If so
            startMainActivity();
        //else
            //startCreateAccountActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(BootstrapActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startCreateAccountActivity(){

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
}
