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
import hse.zhizh.abfclient.jgit.JGitQuery;


/**
 * Клонирование проекта через JGit
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

        if (JGitQuery.cloneRepo(mRepo)) {
            Log.d("ABF CLIENT", "Clone procedure ends with no exception...");
            return true;
        } else
            return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d("ABF Client Clone Command", "Cloned successfully");
        } else {
            Log.d("ABF Client Clone Command", "Clone Failed");
        }
        activity.onCommandExecuted(CLONE_COMMAND, success);
    }

    @Override
    protected void onCancelled() {

    }

}
