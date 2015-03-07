package hse.zhizh.abfclient.Activities;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hse.zhizh.abfclient.R;

public class CommitsFragment extends Fragment implements ProjectActivityEventListener{

    public CommitsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_commits, container, false);

        return rootView;
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
