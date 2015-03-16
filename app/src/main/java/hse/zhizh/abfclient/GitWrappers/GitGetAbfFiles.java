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

    public GitGetAbfFiles(Repository repo) {
        mRepo = repo;
        result = null;
    }

    // init repository locally
    public boolean execute() {
        try {
            List<AbfFile> abfFileList = new Get_Files_abf_yml(mRepo).getFiles();

            if (abfFileList != null) {
                result = abfFileList;

                Log.d(Settings.TAG + COMMANDTAG, "git abffiles received");
                return true;
            } else {
                Log.e(Settings.TAG + COMMANDTAG, "git abffiles was not received");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}