package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.Download_abf_yml;

/**
 * Created by E-Lev on 15.03.2015.
 */
public class GitDownloadAbf extends GitCommand {

    private final String COMMANDTAG = "GitDownABF";

    private final Repository mRepo;
    private final CommandResultListener activity;
    private final List<AbfFile> abfFiles;

    public GitDownloadAbf(Repository rep, CommandResultListener activ, List<AbfFile> abfFiles) {
        mRepo = rep;
        activity = activ;
        this.abfFiles = new ArrayList<AbfFile>();
        this.abfFiles.addAll(abfFiles);
    }

    // Асинхронное выполнение
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "procedure begin...");
        try {
            Download_abf_yml downloadAbf = new Download_abf_yml(mRepo);

            if (downloadAbf.download_files(abfFiles)) {
                Log.d(Settings.TAG + COMMANDTAG, "procedure ends with no exception...");
                return true;
            } else {
                errorMessage = downloadAbf.errorMessage;
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
        activity.onCommandExecuted(DOWNLOADABF_COMMAND, success);
    }

    @Override
    protected void onCancelled() {
    }

}
