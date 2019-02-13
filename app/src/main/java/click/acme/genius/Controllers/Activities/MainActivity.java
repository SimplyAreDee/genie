package click.acme.genius.Controllers.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_activity_ask_help_btn) Button mAskHelpBtn;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void postCreateTreatment() {
    }

    @OnClick(R.id.main_activity_ask_help_btn)
    void OnClickAskHelpButton(View view){
        Intent intent = new Intent(MainActivity.this, AskUsActivity.class);
        startActivity(intent);
    }
}
