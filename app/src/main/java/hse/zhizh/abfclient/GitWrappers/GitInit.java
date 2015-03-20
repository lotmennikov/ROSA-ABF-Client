package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitInit;

/**
 * Local initialization of the repository
 *
 * Created by E-Lev on 23.02.2015.
 */
public class GitInit {

    private static final String COMMANDTAG = "Git Init";

    private final Repository mRepo;
    public String errorMessage;

    public GitInit(Repository repo) {
        mRepo = repo;
        errorMessage = null;
    }

    // init repository locally
    public boolean execute() {
        try {
            JGitInit initcom = new JGitInit(mRepo);
            if (initcom.initRepo()) {
                Log.d(Settings.TAG, COMMANDTAG + "git initialized");
                return true;
            } else {
                errorMessage = initcom.errorMessage;
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