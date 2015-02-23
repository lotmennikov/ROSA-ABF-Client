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
public class JGitBranch {

    private final Repository mRepo;

    public String[] result;

    public JGitBranch(Repository rep) {
        mRepo = rep;

        result = null;
    }

    public boolean execute() {
        result = JGitQuery.getBranches(mRepo);
        if (result != null)
            return true;
        else
            return false;
    }

}
