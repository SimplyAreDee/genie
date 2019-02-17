package click.acme.genius.Controllers.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import click.acme.genius.R;
import icepick.Icepick;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    // 1 - Force developer implement those methods
    protected abstract BaseFragment newInstance();

    protected abstract int getFragmentLayout();

    protected abstract void configureDesign();

    protected abstract void updateDesign();

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 2 - Get layout identifier from abstract method
        View view = inflater.inflate(getFragmentLayout(), container, false);
        // 3 - Binding Views
        ButterKnife.bind(this, view);
        // 4 - Configure Design (Developer will call this method instead of override onCreateView())
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
