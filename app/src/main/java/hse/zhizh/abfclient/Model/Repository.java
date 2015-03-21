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

    //repository from project info
    public Repository(String name, String git_url) {
        this.name = name;
        this.path = new File(Settings.repo_path);
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

    public File getBinDir() {
        File binPath = Settings.downloadsDir;
        return binPath;
    }

    public String getRemoteURL() {
        return url;
    }

}
