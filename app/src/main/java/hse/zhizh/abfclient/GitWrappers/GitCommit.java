package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitCommit;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class GitCommit extends GitCommand {

    private final String COMMANDTAG = "Git Commit";

    private final Repository mRepo;
    private final CommandResultListener activity;
    private final String commitMessage;


    public GitCommit(Repository rep, CommandResultListener activ, String commitMessage) {
        this.mRepo = rep;
        this.activity = activ;
        this.commitMessage = commitMessage;
        errorMessage = null;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");
        try {
            JGitCommit pull = new JGitCommit(mRepo);

            if (pull.commitChanges(commitMessage)) {

                Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
                return true;

            } else {
                errorMessage = pull.errorMessage;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + COMMANDTAG, "success");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "fail");
        }
        activity.onCommandExecuted(COMMIT_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }



}
