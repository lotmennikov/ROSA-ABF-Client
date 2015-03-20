package hse.zhizh.abfclient.GitWrappers;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.jgit.JGitBranches;

/**
 * Получение списка веток
 *
 * Created by E-Lev on 02.02.2015.
 */
public class GitBranch {

    private final Repository mRepo;
    public String[] result;
    public String errorMessage;

    public GitBranch(Repository rep) {
        mRepo = rep;
        result = null;
        errorMessage = null;
    }

    // выполнение запроса
    // результат - массив названий веток
    // сохранён в result
    public boolean execute() {
        JGitBranches branches = new JGitBranches(mRepo);
        result = branches.getBranches();
        if (result != null)
            return true;
        else {
            errorMessage = branches.errorMessage;
            return false;
        }
    }

}
