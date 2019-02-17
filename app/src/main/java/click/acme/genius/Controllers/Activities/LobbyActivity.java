package click.acme.genius.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import click.acme.genius.R;

import android.os.Bundle;
import android.webkit.WebView;

public class LobbyActivity extends BaseActivity {

    @BindView(R.id.activity_lobby_webview)
    WebView mWebView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_lobby;
    }

    @Override
    protected void postCreateTreatment() {
        mWebView.loadUrl("https://discord.gg/nuHXvUR");
    }
}
