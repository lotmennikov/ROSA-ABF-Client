package hse.zhizh.abfclient.GitWrappers;

import android.os.AsyncTask;

import org.eclipse.jgit.lib.ProgressMonitor;

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

    public String errorMessage;
//    public static final int RESET_COMMAND = 7;
//    public static final int GETCOMMITS_COMMAND = 8;
//    public static final int INIT_COMMAND = 9;

/*     public class BasicProgressMonitor implements ProgressMonitor {
        private int mTotalWork;
        private int mWorkDone;
        private String mTitle;

        @Override
        public void start(int i) {
        }

        @Override
        public void beginTask(String title, int totalWork) {
            mTotalWork = totalWork;
            mWorkDone = 0;
            if (title != null) {
                mTitle = title;
            }
            setProgress();
        }

        @Override
        public void update(int i) {
            mWorkDone += i;
            if (mTotalWork != ProgressMonitor.UNKNOWN && mTotalWork != 0) {
                setProgress();
            }
        }

        @Override
        public void endTask() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        private void setProgress() {
            String msg = mTitle;
            int showedWorkDown = Math.min(mWorkDone, mTotalWork);
            int progress = 0;
            String rightHint = "0/0";
            String leftHint = "0%";
            if (mTotalWork != 0) {
                progress = 100 * showedWorkDown / mTotalWork;
                rightHint = showedWorkDown + "/" + mTotalWork;
                leftHint = progress + "%";
            }
            publishProgress(msg, leftHint, rightHint,
                    Integer.toString(progress));
        }

    }*/

}
