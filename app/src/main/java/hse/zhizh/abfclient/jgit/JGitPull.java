package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by Administrator on 3/6/2015.
 */
public class JGitPull {
    Repository repository;
    UsernamePasswordCredentialsProvider auth;
    public JGitPull(Repository repository) {
        this.repository = repository;
        String username = Settings.repo_username;
        String password = Settings.repo_password;
        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            auth = new UsernamePasswordCredentialsProvider(username, password);
        }
    }



    public boolean pullRepo() {
        Git git = repository.getGit();
        PullCommand pullCommand = git.pull();

        //TODO
        //      .setProgressMonitor(new RepoCloneMonitor());

        //TODO
        // .setTransportConfigCallback(new SgitTransportCallback());

        pullCommand.setCredentialsProvider(auth);
        try {
            pullCommand.call();
        } catch (TransportException e) {

            return false;
        } catch (Exception e) {
            return false;
        } catch (OutOfMemoryError e) {
            return false;
        } catch (Throwable e) {
            return false;
        }

        //TODO
        // reposit.updateLatestCommitInfo();

        return true;
    }
}
