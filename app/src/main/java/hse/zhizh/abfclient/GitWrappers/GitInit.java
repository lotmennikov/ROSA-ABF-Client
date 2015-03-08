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

    private final String COMMANDTAG = "Git Init";

    private final Repository mRepo;

    public GitInit(Repository repo) {
        mRepo = repo;
    }

    // init repository locally
    public boolean execute() {
        try {
            if (new JGitInit(mRepo).initRepo()) {
                Log.d(Settings.TAG + COMMANDTAG, "git initialized");
                return true;
            } else {
                Log.e(Settings.TAG + COMMANDTAG, "git not initialized");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}