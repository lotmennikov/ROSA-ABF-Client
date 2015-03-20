package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import java.io.File;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.Upload_abf_yml;

/**
 * Created by E-Lev on 15.03.2015.
 */
public class GitUpload extends GitCommand {

    private final String COMMANDTAG = "GitUpload";

    private final Repository mRepo;
    private final CommandResultListener activity;
    private final File binFile;
    public String result;

    public GitUpload(Repository rep, CommandResultListener activ, File binFile) {
        mRepo = rep;
        activity = activ;
        result = "";
        this.binFile = binFile;
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");
        try {
            Upload_abf_yml uploadAbf = new Upload_abf_yml(mRepo);
            if (uploadAbf.upload_abf_yml(binFile)) {
                result = uploadAbf.getStatusMessage();
                Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
                return true;
            } else {
                errorMessage = (uploadAbf.getStatusMessage() != null ? uploadAbf.getStatusMessage() : uploadAbf.getErrorMessage());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + COMMANDTAG, "success");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "fail");
        }
        activity.onCommandExecuted(UPLOAD_COMMAND, success);
    }

    @Override
    protected void onCancelled() {  }
}
