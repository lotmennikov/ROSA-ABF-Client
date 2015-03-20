package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import hse.zhizh.abfclient.Model.Commit;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitBranches;

/**
 * Получение списка коммитов (не асинхронное)
 *
 * Created by E-Lev on 07.03.2015.
 */
public class GitCommitList {

    private final String COMMANDTAG = "Git CommitList";

    private final Repository mRepo;
    public Commit[] result;
    public String errorMessage;

    public GitCommitList(Repository repo) {
        mRepo = repo;
        errorMessage = null;
    }

    // get commits list
    public boolean execute() {
        try {
            JGitBranches branches = new JGitBranches(mRepo);
            ArrayList<Commit> commits = branches.getCommits();
            if (commits != null) {
                result = commits.toArray(new Commit[commits.size()]);

                Log.d(Settings.TAG, COMMANDTAG + " success");
                return true;
            } else {
                errorMessage = branches.errorMessage;
                Log.e(Settings.TAG, COMMANDTAG + " fail");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

}
