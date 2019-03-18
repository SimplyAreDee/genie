package click.acme.genius.controllers.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import click.acme.genius.R;

/**
 * Affiche l'activité pour la détection de code barre de type IBAN
 */
public class ScanActivity extends BaseActivity {

    private CameraSource mCameraSource;

    @BindView(R.id.activity_scan_camera_surfaceview)
    SurfaceView mCameraSurfaceView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void postCreateTreatment() {
        initialiseBarcodeScanner();
    }

    private void initialiseBarcodeScanner() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).build();
        if (!barcodeDetector.isOperational()) {
            Log.w("AskUs", "Detector dependencies are not yet available.");
        }
        ScanActivity.BarcodeTrackerFactory barcodeFactory = new ScanActivity.BarcodeTrackerFactory();
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        mCameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();

        mCameraSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    mCameraSource.start(mCameraSurfaceView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes
                        .size() != 0) {
                    String barcodeResult = barcodes
                            .valueAt(0)
                            .displayValue;
                    notifyWithVibration(500);
                    returnScannedBarcode(barcodeResult);
                }
            }
        });
    }

    private class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
        @Override
        public Tracker<Barcode> create(Barcode barcode) {
            return new MyBarcodeTracker();
        }
    }

    private class MyBarcodeTracker extends Tracker<Barcode> {
        @Override
        public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode barcode) {
            Log.i("MyBarcodeTracker", barcode.displayValue);
        }
    }

    private void returnScannedBarcode(String barcodeValue) {
        Intent intent = new Intent();
        intent.putExtra("barcode", barcodeValue);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
