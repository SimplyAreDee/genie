package click.acme.genius.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import click.acme.genius.R;
import click.acme.genius.Utils.ScholarLevel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements Validator.ValidationListener {

    @NotEmpty
    @BindView(R.id.fragment_layout_search_edittext)
    TextInputEditText mEditText;
    @BindView(R.id.fragment_layout_search_spinner)
    Spinner mSubjectSpinner;
    @BindView(R.id.fragment_layout_search_spinner2)
    Spinner mSchoolclassSpinner;
    @BindView(R.id.fragment_layout_search_spinner3)
    Spinner mIntervalSpinner;
    @BindView(R.id.fragment_layout_search_switch2)
    Switch mLowLvlSwitch;
    @BindView(R.id.fragment_layout_search_switch3)
    Switch mWithAnswerSwitch;
    @BindView(R.id.fragment_layout_search_switch4)
    Switch mWithCertifiedAnswerSwitch;
    @BindView(R.id.fragment_layout_search_switch5)
    Switch mWithoutAnswerSwitch;

    private Validator validator;
    private OnDataSearchValidate mDataSearchValidate;

    public interface OnDataSearchValidate {
        public void onDataSearchValidate(Bundle data);
    }

    public SearchFragment() {
    }

    @Override
    protected BaseFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout_search;
    }

    @Override
    protected void configureDesign() {
        configureFormValidator();
    }

    @OnClick(R.id.fragment_layout_search_validate)
    public void onClickSearchValidation(){
        validator.validate();
    }

    @Override
    protected void updateDesign() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDataSearchValidate = (OnDataSearchValidate) context;
    }

    @Override
    public void onValidationSucceeded() {
        Bundle bundle = getBundleFromSearchForm();

        mDataSearchValidate.onDataSearchValidate(bundle);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void configureFormValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private Bundle getBundleFromSearchForm(){
        Bundle bundle = new Bundle();

        if(mEditText.getText() != null && !mEditText.getText().toString().isEmpty()){
            bundle.putString("searchText", mEditText.getText().toString());
        }
        bundle.putString("subject", mSubjectSpinner.getSelectedItem().toString());
        bundle.putInt("scholarclass", ScholarLevel.FromStringToInt(mSchoolclassSpinner.getSelectedItem().toString()));
        bundle.putString("interval", mIntervalSpinner.getSelectedItem().toString());
        bundle.putBoolean("lowLvlQuestion",mLowLvlSwitch.isChecked());
        bundle.putBoolean("withAnswer",mWithAnswerSwitch.isChecked());
        bundle.putBoolean("withCertifiedAnswer",mWithCertifiedAnswerSwitch.isChecked());
        bundle.putBoolean("withoutAnswer",mWithoutAnswerSwitch.isChecked());

        return bundle;
    }
}