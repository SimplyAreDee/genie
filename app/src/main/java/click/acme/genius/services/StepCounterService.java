package click.acme.genius.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import click.acme.genius.controllers.activities.MainActivity;

/**
 * Service prévu pour récuperer l'activité de l'utilisateur
 * L'intention étant de permettre de passer en status prémium si l'utilisateur à eu une certaine
 * activité.
 */
public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mStepCount;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (sensorEvent.values[0] == 1.0f) {
                mStepCount++;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MainActivity.sBroadcastIntegerAction);
                broadcastIntent.putExtra("steps", mStepCount);
                sendBroadcast(broadcastIntent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this, mSensor);
        super.onDestroy();
    }
}
