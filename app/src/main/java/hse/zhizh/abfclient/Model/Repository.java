package hse.zhizh.abfclient.Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;

/**
 * Created by E-Lev on 07.01.2015.
 */
public class Repository {

    String name;
    File path;
    String url;

    public Git git;

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
