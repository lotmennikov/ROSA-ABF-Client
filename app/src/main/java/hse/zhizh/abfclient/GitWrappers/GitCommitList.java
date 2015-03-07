package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import hse.zhizh.abfclient.Model.Commit;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitBranches;

/**
 * Created by E-Lev on 07.03.2015.
 */
public class GitCommitList {

    private final String COMMANDTAG = "Git CommitList";

    private final Repository mRepo;
    public Commit[] result;

    public GitCommitList(Repository repo) {
        mRepo = repo;
    }

    // get commits list
    public boolean execute() {
        try {
            JGitBranches branches = new JGitBranches(mRepo);
            ArrayList<Commit> commits = branches.getCommits();
            result = commits.toArray(new Commit[commits.size()]);
            if (result != null) {
                Log.d(Settings.TAG + COMMANDTAG, "list received");
                return true;
            } else {
                Log.e(Settings.TAG + COMMANDTAG, "fail");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
