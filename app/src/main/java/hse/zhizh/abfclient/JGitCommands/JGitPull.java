package hse.zhizh.abfclient.JGitCommands;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitQuery;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class JGitPull extends JGitCommand {

    private final String COMMANDTAG = "JGit Pull";

    private final Repository mRepo;
    private final CommandResultListener activity;

    public JGitPull(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;
    }

    // Асинхронное выполнение инициализации епозитория
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "Init procedure begin...");

        // TODO подставить функцию
        if (true) { // JGitQuery.cloneRepo(mRepo)) {
            Log.d(Settings.TAG + COMMANDTAG, "Init procedure ends with no exception...");
            return true;
        } else
            return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + COMMANDTAG, "Initialized successfully");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "Initialization Failed");
        }
        activity.onCommandExecuted(CLONE_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }

}