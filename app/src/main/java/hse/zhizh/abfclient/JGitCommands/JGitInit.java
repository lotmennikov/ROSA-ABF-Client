package hse.zhizh.abfclient.JGitCommands;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitQuery;

/**
 * Local initialization of the repository
 *
 * Created by E-Lev on 23.02.2015.
 */
public class JGitInit {

    private final String COMMANDTAG = "JGit Init";

    private final Repository mRepo;

    public JGitInit(Repository repo) {
        mRepo = repo;
    }

    // init repository locally
    public boolean execute() {
        // TODO move init to jgit_class
        if (mRepo.getGit() != null) {
            Log.d(Settings.TAG + COMMANDTAG, "git initialized");
            return true;
        } else {
            Log.e(Settings.TAG + COMMANDTAG, "git not initialized");
        return false;
        }
    }

}