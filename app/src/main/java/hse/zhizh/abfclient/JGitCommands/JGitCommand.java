package hse.zhizh.abfclient.JGitCommands;

import android.os.AsyncTask;

/**
 * Created by E-Lev on 07.01.2015.
 */
public abstract class JGitCommand extends AsyncTask<Void, Void, Boolean> {

    public static final int CLONE_COMMAND = 1;
    public static final int GETBRANCHES_COMMAND = 2;

}
