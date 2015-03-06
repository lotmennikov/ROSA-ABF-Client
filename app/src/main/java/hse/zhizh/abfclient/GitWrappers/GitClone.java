package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitQuery;


/**
 * Клонирование проекта через JGit
 *
 * Created by E-Lev on 07.01.2015.
 */
public class GitClone extends GitCommand {

    private final Repository mRepo;
    private final CommandResultListener activity;

    public GitClone(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;
    }

    // Асинхронное выполнение клонирования
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, "Clone procedure begin...");
        // очистка папки
        mRepo.Clear();

        // вызов JGit
        if (JGitQuery.cloneRepo(mRepo)) {
            Log.d(Settings.TAG, "Clone procedure ends with no exception...");
            return true;
        } else
            return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + "Clone Command", "Cloned successfully");
        } else {
            Log.d(Settings.TAG + "Clone Command", "Clone Failed");
        }
        activity.onCommandExecuted(CLONE_COMMAND, success);
    }

    @Override
    protected void onCancelled() {

    }

}
