package hse.zhizh.abfclient.JGitCommands;

import android.os.AsyncTask;

/**
 * Created by E-Lev on 07.01.2015.
 */
public abstract class JGitCommand extends AsyncTask<Void, Void, Boolean> {

    public static final int CLONE_COMMAND = 1;
//    public static final int GETBRANCHES_COMMAND = 2;
    public static final int PULL_COMMAND = 3;
    public static final int PUSH_COMMAND = 4;
    public static final int COMMIT_COMMAND = 5;
    public static final int SETBRANCH_COMMAND = 6;
//    public static final int RESET_COMMAND = 7;
    public static final int GETCOMMITS_COMMAND = 8;
//    public static final int INIT_COMMAND = 9;

}
