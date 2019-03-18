package click.acme.genius.controllers.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.helpers.UserHelper;
import click.acme.genius.models.User;
import click.acme.genius.R;
import click.acme.genius.utils.ScholarLevel;

/**
 * Présente la partie qui concerne les informations de l'utilisateur
 * Gère également la modification au travers d'une vue dialogue
 */
public class AccountDetailFragment extends BaseFragment {
    @BindView(R.id.fragment_account_detail_profilAvatar_imageView)
    ImageView mProfilPicturImageview;
    @BindView(R.id.fragment_account_detail_pseudonym_edittext)
    TextView mPseudonymTextview;
    @BindView(R.id.fragment_account_detail_firstname_edittext)
    TextView mFirstnameTextview;
    @BindView(R.id.fragment_account_detail_lastname_edittext)
    TextView mLastnameTextview;
    @BindView(R.id.fragment_account_detail_address_edittext)
    TextView mAddressTextview;
    @BindView(R.id.fragment_account_detail_scholarClass_edittext)
    TextView mScholarClassTextview;
    @BindView(R.id.fragment_account_detail_establishment_edittext)
    TextView mEstablishmentTextview;
    @BindView(R.id.fragment_account_detail_askedCount_edittext)
    TextView mAskedCountTextview;
    @BindView(R.id.fragment_account_detail_answerCount_edittext)
    TextView mAnswerCountTextview;

    private Dialog mEditDialog;
    private User mUser;

    public AccountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseFragment newInstance() {
        return new AccountDetailFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_account_detail;
    }

    @Override
    protected void configureDesign() {
        mUser = User.getCurrentUser();

        displayUserInformation();
    }

    @Override
    protected void updateDesign() {

    }

    /**
     * Affiche les informations de l'utilisateur sur la vue
     */
    private void displayUserInformation() {
        if(mUser != null) {
            mPseudonymTextview.setText(mUser.getUserName());
            mFirstnameTextview.setText(mUser.getUserFirstName());
            mLastnameTextview.setText(mUser.getUserLastName());
            mAddressTextview.setText(mUser.getEmailAddress());
            mScholarClassTextview.setText(ScholarLevel.FromIntToString(mUser.getScholarLevel()));
            mEstablishmentTextview.setText(mUser.getEstablishment());
            mAskedCountTextview.setText(String.valueOf(mUser.getAskedCount()));
            mAnswerCountTextview.setText(String.valueOf(mUser.getAnsweredCount()));

            Glide.with(getContext())
                    .load(mUser.getAvatarUrl())
                    .into(mProfilPicturImageview);
        }
    }

    /**
     * Affiche la boite de dialogue pour modifier les éléments d'informations du compte
     * @param view View l'élément à l'origine du déclenchement
     */
    @OnClick(R.id.fragment_account_detail_floatingbutton)
    public void OnEditClickButton(View view){
        View editView = getActivity().getLayoutInflater().inflate(R.layout.fragment_account_detail_edit, new LinearLayout(getActivity()), false);

        mEditDialog = new Dialog(getActivity());
        mEditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mEditDialog.setContentView(editView);

        populateEditDialog();

        ImageButton validate = editView.findViewById(R.id.fragment_account_detail_edit_validate);
        validate.setOnClickListener(listener -> {
            editUserInformation();
        });

        mEditDialog.show();

        Window window = mEditDialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    /**
     * Permet de renseigner les éléments de la boite de dialogue avec les informations du compte
     */
    private void populateEditDialog() {
        if(mEditDialog != null){
            EditText nickName = mEditDialog.findViewById(R.id.fragment_account_detail_edit_pseudonym_edittext);
            EditText firstname = mEditDialog.findViewById(R.id.fragment_account_detail_edit_firstname_edittext);
            EditText lastname = mEditDialog.findViewById(R.id.fragment_account_detail_edit_lastname_edittext);
            Spinner scholarClass = mEditDialog.findViewById(R.id.fragment_account_detail_edit_scholarClass_edittext);
            EditText establishment = mEditDialog.findViewById(R.id.fragment_account_detail_edit_establishment_edittext);

            configureMatiereSpinner(scholarClass);

            nickName.setText(mUser.getUserName());
            firstname.setText(mUser.getUserFirstName());
            lastname.setText(mUser.getUserLastName());
            scholarClass.setSelection( mUser.getScholarLevel() );
            establishment.setText(mUser.getEstablishment());
        }
    }

    /**
     * Récupère les éléments du formulaire de la boite de dialogue pour mettre à jour les informations
     * du compte et demande son enregistrement sur le SGBD
     */
    private void editUserInformation() {
        if(mEditDialog != null){
            EditText nickName = mEditDialog.findViewById(R.id.fragment_account_detail_edit_pseudonym_edittext);
            EditText firstname = mEditDialog.findViewById(R.id.fragment_account_detail_edit_firstname_edittext);
            EditText lastname = mEditDialog.findViewById(R.id.fragment_account_detail_edit_lastname_edittext);
            Spinner scholarClass = mEditDialog.findViewById(R.id.fragment_account_detail_edit_scholarClass_edittext);
            EditText establishment = mEditDialog.findViewById(R.id.fragment_account_detail_edit_establishment_edittext);

            mUser.setUserName(nickName.getText().toString());
            mUser.setUserFirstName(firstname.getText().toString());
            mUser.setUserLastName(lastname.getText().toString());
            mUser.setScholarLevel( ScholarLevel.FromStringToInt(scholarClass.getSelectedItem().toString()) );
            mUser.setEstablishment(establishment.getText().toString());

            if(mUser.getUserName() != null && !mUser.getUserName().isEmpty()) {
                UserHelper.saveState(mUser);
            }
        }
    }

    /**
     * Ajoute la liste des matières à la boite déroulante passée en paramêtre
     * @param spinner Spinner la boite déroulante à paramétrer
     * @return Spinner
     */
    private Spinner configureMatiereSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.scholarclass_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        return spinner;
    }
}