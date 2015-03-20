package hse.zhizh.abfclient.GitWrappers;

import android.util.Log;

import java.util.List;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;
import hse.zhizh.abfclient.jgit.Get_Files_abf_yml;

/**
 * Getting .abf.yml file list
 *
 * Created by E-Lev on 10.03.2015.
 */
public class GitGetAbfFiles {
    private static final String COMMANDTAG = " GetAbfYml";

    private final Repository mRepo;

    public List<AbfFile> result;
    public String errorMessage;

    public GitGetAbfFiles(Repository repo) {
        mRepo = repo;
        result = null;
        errorMessage = null;
    }

    // init repository locally
    public boolean execute() {
        try {
            Get_Files_abf_yml getfiles = new Get_Files_abf_yml(mRepo);
            List<AbfFile> abfFileList = getfiles.getFiles();

            if (abfFileList != null) {
                result = abfFileList;

                Log.d(Settings.TAG + COMMANDTAG, "git abffiles received");
                return true;
            } else {
                errorMessage = getfiles.errorMessage;
                Log.e(Settings.TAG + COMMANDTAG, "git abffiles was not received");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return false;
        }
    }

}