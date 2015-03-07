package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitReset;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class GitReset {

    private final String COMMANDTAG = "Git Reset";

    private final Repository mRepo;

    public GitReset(Repository repo) {
        mRepo = repo;
    }

    // reset repository
    public boolean execute() {
        try {
            JGitReset reset = new JGitReset(mRepo);

            if (reset.reset()) {
                Log.d(Settings.TAG + COMMANDTAG, "reset success");
                return true;
            } else {
                Log.e(Settings.TAG + COMMANDTAG, "reset failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
