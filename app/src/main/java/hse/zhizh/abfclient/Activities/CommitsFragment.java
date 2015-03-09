package hse.zhizh.abfclient.Activities;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import hse.zhizh.abfclient.Model.Commit;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

public class CommitsFragment extends Fragment implements ProjectActivityEventListener{

    private Commit[] commits;
    private ListView commitsList;


    public CommitsFragment() {
        // Required empty public constructor
        commits = null;
        commitsList = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_commits, container, false);
        commitsList = (ListView)fragmentView.findViewById(R.id.commitsList);
        setCommitsList();

        return fragmentView;
    }

    // updating commits list
    public void setCommitsList() {
        if (commitsList != null) {
            String[] comms;
            if (commits != null) {
                comms = new String[commits.length];
                String newtext;
                for (int i = 0; i < comms.length; ++i) {
                    newtext = commits[i].getHash() + (commits[i].isPushed() ? "" : " (not pushed)") + "\n"
                            + commits[i].getName() + "\n"
                            + Settings.commitDate.format(commits[i].getDate());
                    comms[i] = newtext;
                }

            } else {
                comms = new String[0];
            }
            Log.d(Settings.TAG + " CommitsFragment", "Setting Adapter");
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.contents_list_element, comms);
            commitsList.setAdapter(listAdapter);
        }
    }

    // change commit list
    public void refresh(Commit[] commits) {
        this.commits = commits;
        setCommitsList();
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
