package click.acme.genius.Controllers.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import click.acme.genius.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {

    public SearchFragment() {
        Task<DocumentSnapshot> apiKey = FirebaseFirestore.getInstance()
                .collection("configurations")
                .document("2AC4f2ucepOFYsTmS9E2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Client client = new Client(
                                    "PNQLEU8GRR",
                                    task.getResult().getString("aloglia")
                            );
                            Index index = client.getIndex("questions_content");
                        }
                    }
                });

    }

    @Override
    protected BaseFragment newInstance() {
        return null;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout_search;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

    }
}
