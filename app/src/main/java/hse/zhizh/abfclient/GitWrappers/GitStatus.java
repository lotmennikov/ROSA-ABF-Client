package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitStatus;

/**
 * Created by E-Lev on 21.03.2015.
 */
public class GitStatus {

    private static final String COMMANDTAG = "Git Status";

    private final Repository mRepo;

    public String result;
    public String errorMessage;

    public GitStatus(Repository repo) {
        mRepo = repo;
        result = null;
        errorMessage = null;
    }

    // get git status
    public boolean execute() {
        try {
            JGitStatus status = new JGitStatus(mRepo);
            if (status.getStatus()) {
                result = status.getStatusResult().toString();
                Log.d(Settings.TAG, COMMANDTAG + "git initialized");
                return true;
            } else {
                errorMessage = status.errorMessage;
                Log.e(Settings.TAG, COMMANDTAG + "git not initialized");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }


}
