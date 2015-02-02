package hse.zhizh.abfclient.JGitCommands;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.jgit.JGitQuery;

/**
 * Получение списка веток
 *
 * Created by E-Lev on 02.02.2015.
 */
public class JGitBranch extends JGitCommand {

    private final Repository mRepo;
    private final CommandResultListener activity;

    public String[] result;

    public JGitBranch(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;

        result = null;
    }

    // Асинхронное доставание списка веток
    @Override
    protected Boolean doInBackground(Void... params) {
        result = JGitQuery.getBranches(mRepo);
        if (result != null)
            return true;
        else
            return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d("ABF Client Branches Command", "Success");
        } else {
            Log.d("ABF Client Branches Command", "Fail");
        }
        activity.onCommandExecuted(GETBRANCHES_COMMAND, success);
    }

    @Override
    protected void onCancelled() {

    }
}
