package hse.zhizh.abfclient.Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import org.apache.commons.io.FileUtils;
/**
 * Created by E-Lev on 07.01.2015.
 */
public class Repository {

    String name;
    File path;
    String url;

    public Repository(Context cntxt) {
        name = "0ad";
        path = cntxt.getFilesDir();
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
