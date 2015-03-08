package hse.zhizh.abfclient.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuildsFragment extends Fragment implements ProjectActivityEventListener{


    public BuildsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_builds, container, false);
    }



    // TODO refresh
    public void refresh(Build[] builds) {

    }

// project activity events

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean onBranchChanged() {
        return false;
    }

    @Override
    public boolean onRefreshed() {
        return false;
    }
}
