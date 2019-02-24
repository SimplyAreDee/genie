package click.acme.genius.Controllers.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected Bundle mBundle;
    // 1 - Force developer implement those methods
    protected abstract BaseDialogFragment newInstance();

    protected abstract int getFragmentLayout();

    protected abstract void configureDesign();

    protected abstract void updateDesign();

    public BaseDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getFragmentLayout(), container, false);

        ButterKnife.bind(this, view);

        mBundle = savedInstanceState;

        this.configureDesign();

        return (view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 5 - Handling Bundle Restoration
        Icepick.restoreInstanceState(this, savedInstanceState);
        // 7 - Update Design (Developer will call this method instead of override onActivityCreated())
        this.updateDesign();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 6 - Handling Bundle Save
        Icepick.saveInstanceState(this, outState);
    }

}
