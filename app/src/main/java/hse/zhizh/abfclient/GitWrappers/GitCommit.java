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
    private final boolean stageAll;
    private final boolean isAmend;


    public GitCommit(Repository rep, CommandResultListener activ, String commitMessage,boolean stage, boolean amend) {
        this.mRepo = rep;
        this.activity = activ;
        this.commitMessage = commitMessage;
        this.stageAll = stage;
        this.isAmend = amend;
        errorMessage = null;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " procedure begin...");
        try {
            JGitCommit pull = new JGitCommit(mRepo);

            if (pull.commitChanges(commitMessage, Settings.repo_username, Settings.repo_email ,stageAll, isAmend)) {

                Log.d(Settings.TAG, COMMANDTAG + " procedure ends with no exception...");
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
            Log.d(Settings.TAG, COMMANDTAG + "success");
        } else {
            Log.d(Settings.TAG, COMMANDTAG + "fail");
        }
        activity.onCommandExecuted(COMMIT_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }



}
