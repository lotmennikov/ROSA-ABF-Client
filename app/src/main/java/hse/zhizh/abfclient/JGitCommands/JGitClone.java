package hse.zhizh.abfclient.JGitCommands;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Locale;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;


/**
 * Клонирование проекта через JGit
 * Требуется переделка
 *
 * Created by E-Lev on 07.01.2015.
 */
public class JGitClone extends JGitCommand {

    private final Repository mRepo;
    private final CommandResultListener activity;

    public JGitClone(Repository rep, CommandResultListener activ) {
        mRepo = rep;
        activity = activ;
    }

    // Асинхронное выполнение клонирования
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("ABF CLIENT", "Clone procedure begin...");

        File localRepo = mRepo.getDir();

        // initializing cloneCommand
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(mRepo.getRemoteURL()).setCloneAllBranches(true)
//                .setProgressMonitor(new RepoCloneMonitor())
                .setDirectory(localRepo);


        String username = mRepo.getUsername();
        String password = mRepo.getPassword();
        Locale.setDefault(Locale.US);

        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(
                    username, password);
            cloneCommand.setCredentialsProvider(auth);
        }

        try {
            // execution of cloneCommand
            cloneCommand.call();
            Log.d("ABF CLIENT", "Clone procedure ends with no exception...");
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
            return false;
        } catch (TransportException e) {
            e.printStackTrace();
            return false;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        } catch (JGitInternalException e) {
            e.printStackTrace();
            return false;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
      return true;

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d("ABF Client Clone Command", "Cloned successfully");
        } else {
            Log.d("ABF Client Clone Command", "Clone Failed");
        }
        activity.onCommandExecuted(success);
    }

    @Override
    protected void onCancelled() {

    }

}
