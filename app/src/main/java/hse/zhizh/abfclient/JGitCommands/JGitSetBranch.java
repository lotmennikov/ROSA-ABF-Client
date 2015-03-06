package hse.zhizh.abfclient.JGitCommands;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class JGitSetBranch extends JGitCommand {

    private final String COMMANDTAG = "JGit SetBranch";

    private final Repository mRepo;
    private final CommandResultListener activity;
    private final String branchname;

    public JGitSetBranch(Repository rep, CommandResultListener activ, String branchname) {
        mRepo = rep;
        activity = activ;
        this.branchname = branchname;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");

        // TODO подставить функцию
        if (true) { // JGitQuery.cloneRepo(mRepo)) {
            Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
            return true;
        } else
            return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + COMMANDTAG, "success");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "Fail");
        }
        activity.onCommandExecuted(CLONE_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }
}