package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitPush;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class GitPush extends GitCommand {

    private final String COMMANDTAG = "Git Push";

    private final Repository mRepo;
    private final CommandResultListener activity;

    public GitPush(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");
        try {
            JGitPush push = new JGitPush(mRepo);

            if (push.pushRepo(false)) {

                Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
                return true;

            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
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
        activity.onCommandExecuted(PUSH_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }

}