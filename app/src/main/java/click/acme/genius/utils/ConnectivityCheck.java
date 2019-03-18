package click.acme.genius.utils;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Vérifie la disponibilité d'une connexion internet en envoyant un ping au serveur google
 */
public class ConnectivityCheck extends AsyncTask<Void,Void,Boolean> {

private Consumer mConsumer;
public  interface Consumer { void accept(Boolean internet); }

    public  ConnectivityCheck(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) { try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
        sock.close();
        return true;
    } catch (IOException e) { return false; } }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}
