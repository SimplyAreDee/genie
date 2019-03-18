package click.acme.genius.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.models.Question;
import click.acme.genius.models.User;
import click.acme.genius.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Affiche le formulaire de saisie de question
 */
public class QuestionAskActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener, RewardedVideoAdListener {
    private static final String PERMS = Manifest.permission.CAMERA;
    private static final int RC_CAMERA_PERMS = 100;
    private static final int RC_CAMERA_IBAN_TAKEN = 200;
    private static final int RC_CAMERA_REQUEST = 300;

    @BindView(R.id.activity_ask_help_reference_scan_btn)
    ImageButton mReferenceScanBtn;
    @BindView(R.id.activity_ask_help_subject_spinner)
    Spinner mSubjectSpinner;
    @BindView(R.id.activity_ask_envoyer_btn)
    ImageButton mSendDataBtn;
    @NotEmpty
    @BindView(R.id.activity_ask_help_title_edittext)
    EditText mTitleEditText;
    @BindView(R.id.activity_ask_help_reference_edittext)
    EditText mReferenceEditText;
    @BindView(R.id.activity_ask_help_page_edittext)
    EditText mPageEditText;
    @BindView(R.id.activity_ask_help_number_edittext)
    EditText mNumberEditText;
    @NotEmpty
    @BindView(R.id.activity_ask_help_instruction_edittext)
    EditText mInstructionEditText;
    @BindView(R.id.activity_ask_help_preview_imageview_tablerow)
    TableRow mImagePreviewContainer;
    @BindView(R.id.activity_ask_help_preview_imageview)
    ImageView mImagePreviewImageview;
    @BindView(R.id.activity_chat_detail_info)
    TextView mLoadingInfo;
    @BindView(R.id.activity_chat_detail_progressLayout)
    RelativeLayout mProgressLayout;

    private Validator validator;
    private FirebaseFirestore mFirestore;
    private String mSubjectSelected = "";
    private Bitmap mImageCaptured;
    private RewardedVideoAd mRewardedVideoAd;
    private String mPathImageSavedInFirebase;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_ask_us;
    }

    @Override
    protected void postCreateTreatment() {
        configureFormValidator();
        configureMatiereSpinner();

        mProgressLayout.setVisibility(View.GONE);

        //Une fois la question soumise on affiche une vidéo.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        //On précharge la vidéo
        loadRewardedVideoAd();

        initDatabase();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CAMERA_PERMS)
    @OnClick(R.id.activity_ask_help_reference_scan_btn)
    void OnClickReferenceScanBtn() {
        showScanActivity();
    }

    @AfterPermissionGranted(RC_CAMERA_PERMS)
    @OnClick(R.id.activity_ask_help_reference_capture_btn)
    void OnClickReferenceCaptureBtn() {
        showCaptureActivity();
    }

    @OnClick(R.id.activity_ask_envoyer_btn)
    void OnClickSendDataBtn(View view) {
        validator.validate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mSubjectSelected = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Si jamais on sort sans séléctionner de valeur
        mSubjectSelected = mSubjectSpinner.getSelectedItem().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si il s'agit d'une reconnaissance d'IBAN
        if (requestCode == RC_CAMERA_IBAN_TAKEN) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("barcode");
                mReferenceEditText.setText(result);
            }
        }
        //Si il s'agit d'une capture photo
        if (requestCode == RC_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                mImageCaptured = (Bitmap) data.getExtras().get("data");

                //On affiche la miniature
                Glide.with(this)
                        .load(mImageCaptured)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImagePreviewImageview);

                mImagePreviewContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "SHAZAM!", Toast.LENGTH_SHORT).show();
        mProgressLayout.setVisibility(View.VISIBLE);
        //Si on a une image à envoyer
        if(mImageCaptured != null) {
            mLoadingInfo.setText(getString(R.string.wait_image));
            uploadImageToFirebaseStoreAndSaveQuestionToDatabase();
        }else{ //Si on a pas d'image
            mLoadingInfo.setText(getString(R.string.wait_text));
            saveQuestionToDatabase();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().addTestDevice("955122ADA1F93E05A0EFB6FA1C5868C5").build());
    }

    private void uploadImageToFirebaseStoreAndSaveQuestionToDatabase(){

        String uuid = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference(uuid);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mImageCaptured.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageRef.putBytes(data).addOnSuccessListener(this, taskSnapshot -> {
            mPathImageSavedInFirebase = taskSnapshot.getMetadata().getPath();

            saveQuestionToDatabase();
        });
    }

    private void saveQuestionToDatabase() {
        Question question = getQuestionFromForm();
        if(mPathImageSavedInFirebase != null) {
            question.setCapturedImage(mPathImageSavedInFirebase);
        }

        mFirestore.collection("questions").document(question.getId()).set(question)
                .addOnCompleteListener(task -> showVideoAdd())
                .addOnFailureListener(task -> displayErrorMessage());
    }

    private void displayErrorMessage() {
        mProgressLayout.setVisibility(View.GONE);
    }

    private void showVideoAdd() {
        mProgressLayout.setVisibility(View.GONE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }else{
            Log.d("Add","videoAd not loaded");
            finish();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Affiche les messages d'erreur
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Configure le validateur du formulaire
    private void configureFormValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    //Affiche la capture vidéo pour la reconnaissance de l'IBAN, seulement si la permission est accordée
    private void showScanActivity() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_camera_access), RC_CAMERA_PERMS, PERMS);
            return;
        }

        Intent intent = new Intent(QuestionAskActivity.this, ScanActivity.class);
        startActivityForResult(intent, RC_CAMERA_IBAN_TAKEN);
    }

    //Affiche la capture pour ajouter une image, seulement si la permission est accordée
    private void showCaptureActivity() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.capture_perm_text), RC_CAMERA_PERMS, PERMS);
            return;
        }

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, RC_CAMERA_REQUEST);
    }

    //Peuple la boite déroulante avec la liste des matières
    private void configureMatiereSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSubjectSpinner.setAdapter(adapter);
        mSubjectSelected = mSubjectSpinner.getSelectedItem().toString();
    }

    private void initDatabase() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    //Retourne une instance de Question peuplé avec les éléments du formulaire
    private Question getQuestionFromForm(){
        Question question = new Question();

        question.generateId();
        question.setAuthorReference(User.getCurrentUser().getId());
        question.setTitle(mTitleEditText.getText().toString());
        question.setInstruction(mInstructionEditText.getText().toString());
        question.setSubject(mSubjectSelected);
        question.setPage(mPageEditText.getText().toString());
        question.setNumber(mNumberEditText.getText().toString());
        question.setIban(mReferenceEditText.getText().toString());

        return question;
    }


    //Les éléments ci-dessous sont relatif à la publicité
    @Override
    public void onRewarded(RewardItem rewardItem) {
        //TODO upadate count
        finish();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d("Add","onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d("Add","onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d("Add","onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        finish();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Quitte l'application suite à l'intéraction avec la pub
        Log.d("Add","onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d("Add", "onRewardedVideoFailedToLoad");
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d("Add","onRewardedVideoCompleted");
    }
}
