package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Locale;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;

public class JGitClone {

    Repository repository;
    public String errorMessage;
    UsernamePasswordCredentialsProvider auth;

    public JGitClone(Repository repository) {
        this.repository = repository;
        String username = Settings.repo_username;
        String password = Settings.repo_password;
        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            auth = new UsernamePasswordCredentialsProvider(username, password);
        }
    }



    public boolean cloneRepo() {
        System.err.println("Clone procedure begin...");
        File localRepo = repository.getDir();

        // initializing cloneCommand
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repository.getRemoteURL()).setCloneAllBranches(true)
                .setProgressMonitor(new RepoCloneMonitor())
                .setDirectory(localRepo);

//            cloneCommand.setCredentialsProvider(auth);

        try {
            // execution of cloneCommand
            repository.setGit(cloneCommand.call());
            System.err.println("Clone procedure ends with no exception...");
        } catch (InvalidRemoteException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (TransportException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (GitAPIException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (JGitInternalException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (OutOfMemoryError e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }

        return true;
    }

    static class RepoCloneMonitor implements ProgressMonitor {

        private int mTotalWork;
        private int mWorkDone;
        private String mTitle;

        private void publishProgressInner() {
            String status = "";
            String percent = "";
            if (mTitle != null) {
                status = String.format(Locale.getDefault(), "%s ... ", mTitle);
                percent = "0%";
            }
            if (mTotalWork != 0) {
                int p = 100 * mWorkDone / mTotalWork;
                percent = String.format(Locale.getDefault(), "(%d%%)", p);
            }
            // mRepo.updateStatus(status + percent);
            System.out.println(status + percent);
        }

        @Override
        public void start(int totalTasks) {
            publishProgressInner();
        }

        @Override
        public void beginTask(String title, int totalWork) {
            mTotalWork = totalWork;
            mWorkDone = 0;
            mTitle = title;
            publishProgressInner();
        }

        @Override
        public void update(int i) {
            mWorkDone += i;
            publishProgressInner();
        }

        @Override
        public void endTask() {
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

    }
}
