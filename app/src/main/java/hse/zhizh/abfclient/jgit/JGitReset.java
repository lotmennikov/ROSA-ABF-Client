package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.ResetCommand;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/6/2015.
 */
public class JGitReset {

    Repository repository;
    public String errorMessage;

    public JGitReset(Repository repository) {
        this.repository = repository;
    }
    //TODO
    //Not tested!!!
    public boolean reset() {
        try {
            repository.getGit().reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (Throwable e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
