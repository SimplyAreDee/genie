package click.acme.genius.Controllers.Activities;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AskUsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener {

    private String mSubjectSelected = "";

    private static final String PERMS = Manifest.permission.CAMERA;
    private static final int RC_CAMERA_PERMS = 100;
    private static final int RC_CAMERA_TAKEN = 200;

    @BindView(R.id.activity_ask_help_reference_scan_btn)
    Button mReferenceScanBtn;
    @BindView(R.id.activity_ask_help_matiere_spinner)
    Spinner mMatiereSpinner;
    @BindView(R.id.activity_ask_envoyer_btn)
    Button mSendDataBtn;

    @NotEmpty
    @BindView(R.id.activity_ask_help_reference_edittext)
    EditText mReferenceEditText;
    @NotEmpty
    @BindView(R.id.activity_ask_help_page_edittext)
    EditText mPageEditText;
    @NotEmpty
    @BindView(R.id.activity_ask_help_number_edittext)
    EditText mNumberEditText;
    @NotEmpty
    @BindView(R.id.activity_ask_help_consign_edittext)
    EditText mConsignEditText;
    private Validator validator;


    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_ask_us;
    }

    @Override
    protected void postCreateTreatment() {
        configureFormValidator();
        configureMatiereSpinner();
    }

    private void configureFormValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
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

    @OnClick(R.id.activity_ask_envoyer_btn)
    void OnClickSendDataBtn(View view) {
        validator.validate();
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
                mReferenceEditText.setText(result);
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "SHAZAM!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
