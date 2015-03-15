package hse.zhizh.abfclient.GitWrappers;

import android.os.AsyncTask;

/**
 * Created by E-Lev on 07.01.2015.
 */
public abstract class GitCommand extends AsyncTask<Void, Void, Boolean> {

    public static final int CLONE_COMMAND = 101;
//    public static final int GETBRANCHES_COMMAND = 2;
    public static final int PULL_COMMAND = 103;
    public static final int PUSH_COMMAND = 104;
    public static final int COMMIT_COMMAND = 105;
    public static final int SETBRANCH_COMMAND = 106;
    public static final int DOWNLOADABF_COMMAND = 107;
    public static final int UPLOAD_COMMAND = 108;
//    public static final int RESET_COMMAND = 7;
//    public static final int GETCOMMITS_COMMAND = 8;
//    public static final int INIT_COMMAND = 9;

}
