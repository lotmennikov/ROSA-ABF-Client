package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/6/2015.
 */
public class JGitCommit {

    Repository repository;
    public String errorMessage;
    public JGitCommit(Repository repository) {
        this.repository = repository;
    }


    public boolean commitChanges(String commitMessage) {
        Git git = repository.getGit();
        CommitCommand cc = git.commit().setMessage(commitMessage);
        //  CommitCommand cc = git.commit().setAll(stageAll).setMessage(commitMessage);

        try {
            cc.call();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
