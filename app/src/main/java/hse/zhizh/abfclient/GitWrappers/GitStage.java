package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import java.io.File;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.JGitStage;

/**
 * Created by E-Lev on 22.03.2015.
 */
public class GitStage {

    private static final String COMMANDTAG = "Git Stage";

    private final Repository mRepo;
    public String errorMessage;

    public GitStage(Repository repo) {
        mRepo = repo;
        errorMessage = null;
    }


    // stage one file
    public boolean execute(File file, boolean stage) {
        try {
            JGitStage stagecom = new JGitStage(mRepo);
            if (stage) {
                if (stagecom.add_file_to_stage(file)) {
                    Log.d(Settings.TAG, COMMANDTAG + " file staged");
                    return true;
                } else {
                    errorMessage = stagecom.errorMessage;
                    Log.e(Settings.TAG, COMMANDTAG + " file was not staged");
                    return false;
                }
            } else {
                if (stagecom.unstage(file)) {
                    Log.d(Settings.TAG, COMMANDTAG + " file unstaged");
                    return true;
                } else {
                    errorMessage = stagecom.errorMessage;
                    Log.e(Settings.TAG, COMMANDTAG + " file was not unstaged");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

    // stage all files
    public boolean execute() {
        try {
            JGitStage stagecom = new JGitStage(mRepo);
            if (stagecom.stage_all()) {
                Log.d(Settings.TAG, COMMANDTAG + " all files staged");
                return true;
            } else {
                errorMessage = stagecom.errorMessage;
                Log.e(Settings.TAG, COMMANDTAG + " all files were not staged");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

}
