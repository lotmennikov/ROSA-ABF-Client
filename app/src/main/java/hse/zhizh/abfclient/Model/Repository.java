package hse.zhizh.abfclient.Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;

import hse.zhizh.abfclient.common.Settings;

/**
 *
 *
 * Created by E-Lev on 07.01.2015.
 */
public class Repository {

    private String name;
    private File path;
    private String url;

    public Git git;

    // selected branch
    public String currentBranch;

    // default repository
    public Repository(Context cntxt) {
        name = "0ad";
        path = cntxt.getFilesDir();
        git = null;
        File projdir = new File(path.getAbsolutePath() + "/" + name + "/");

        if (projdir.exists()) {
            try {
                FileUtils.deleteDirectory(projdir);
            } catch (Exception e) {
                Log.d("ABF-Client", "Directory had not been deleted! :" + projdir.getAbsolutePath());
            }
        }
        path = projdir;
        url = "https://abf.io/lotmen/0ad.git";
    }

    //repository from project info
    public Repository(Context cntxt, String name, String git_url) {
        this.name = name;
        this.path = cntxt.getFilesDir();
        File projdir = new File(this.path.getAbsolutePath() + "/" + this.name + "/");
        this.path = projdir;
        this.url = git_url;

        this.git = null;
    }

    public String getBranchName(){
        try {
            return getGit().getRepository().getFullBranch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // initGitLocally
    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    // clear repository directory
    public void Clear() {
        // delete dir if exists }
        if (path.exists()) {
            try {
                FileUtils.deleteDirectory(path);
            } catch (Exception e) {
                Log.d(Settings.TAG, "Directory had not been deleted! :" + path.getAbsolutePath());
            }
        }
    }

    public String getName() {
        return name;
    }

    public File getDir() {
        return path;
    }

    public String getRemoteURL() {
        return url;
    }

}
