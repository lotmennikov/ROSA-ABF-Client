package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitBranches;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class GitSetBranch extends GitCommand {

    private final String COMMANDTAG = "Git SetBranch";

    private final Repository mRepo;
    private final CommandResultListener activity;
    private final String branchname;
    public String errorMessage;

    public GitSetBranch(Repository rep, CommandResultListener activ, String branchname) {
        mRepo = rep;
        activity = activ;
        this.branchname = branchname;
        errorMessage = null;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");
        try {
            JGitBranches branches = new JGitBranches(mRepo);

            if (branches.checkout(branchname)) { // JGitQuery.cloneRepo(mRepo)) {
                Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
                return true;
            } else {
                errorMessage = branches.errorMessage;
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
            Log.d(Settings.TAG + COMMANDTAG, "Fail");
        }
        activity.onCommandExecuted(SETBRANCH_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }
}