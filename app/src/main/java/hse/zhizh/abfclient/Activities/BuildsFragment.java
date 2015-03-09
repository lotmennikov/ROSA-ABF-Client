package hse.zhizh.abfclient.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuildsFragment extends Fragment implements ProjectActivityEventListener{

    private Build[] builds;
    private ListView buildList;

    public BuildsFragment() {
        buildList = null;
        builds = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_builds, container, false);
        buildList = (ListView)fragmentView.findViewById(R.id.buildList);

        setBuildsList();
        return fragmentView;
    }

    // updating commits list
    public void setBuildsList() {
        if (buildList != null) {
            String[] blds;
            if (builds != null) {
                blds = new String[builds.length];
                String newtext;
                for (int i = 0; i < blds.length; ++i) {
                    newtext = builds[i].getBuildId() +  " - status: " + builds[i].getStatusCode() + "\n"
                            + builds[i].getUrl();
                    blds[i] = newtext;
                }

            } else {
                blds = new String[0];
            }
            Log.d(Settings.TAG + " BuildsFragment", "Setting Adapter");
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.contents_list_element, blds);
            buildList.setAdapter(listAdapter);
        }
    }

    // refresh list
    public void refresh(Build[] builds) {
        this.builds = builds;
        setBuildsList();
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
