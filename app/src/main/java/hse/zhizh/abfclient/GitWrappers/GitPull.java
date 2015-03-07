package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class GitPull extends GitCommand {

    private final String COMMANDTAG = "Git Pull";

    private final Repository mRepo;
    private final CommandResultListener activity;

    public GitPull(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");

        // TODO подставить функцию
        if (true) {
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
            Log.d(Settings.TAG + COMMANDTAG, "fail");
        }
        activity.onCommandExecuted(CLONE_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }

}