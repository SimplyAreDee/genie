package click.acme.genius.Controllers.Activities;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AskUsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private String mSubjectSelected = "";

    private static final String PERMS = Manifest.permission.CAMERA;
    private static final int RC_CAMERA_PERMS = 100;
    private static final int RC_CAMERA_TAKEN = 200;

    @BindView(R.id.activity_ask_help_reference_scan_btn)
    Button mReferenceScanBtn;
    @BindView(R.id.activity_ask_help_reference_textview)
    TextView mReferenceTextView;
    @BindView(R.id.activity_ask_help_matiere_spinner)
    Spinner mMatiereSpinner;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_ask_us;
    }

    @Override
    protected void postCreateTreatment() {
        configureMatiereSpinner();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CAMERA_PERMS)
    @OnClick(R.id.activity_ask_help_reference_scan_btn)
    void OnClickReferenceScanBtn(View view) {
        showScanActivity();
    }

    private void showScanActivity() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_camera_access), RC_CAMERA_PERMS, PERMS);
            return;
        }
        Intent intent = new Intent(AskUsActivity.this, ScanActivity.class);
        startActivityForResult(intent, RC_CAMERA_TAKEN);
    }

    private void configureMatiereSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMatiereSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mSubjectSelected = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CAMERA_TAKEN) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("barcode");
                mReferenceTextView.setText(result);
            }
        }
    }
}
