package hse.zhizh.abfclient.Model;

import android.content.Context;

import java.io.File;

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
//       TODO Надо сделать что-то с удалением папки, джигиту непустые папки не нравятся
//       if (projdir.exists()) {
//            projdir.delete();
//        }
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

    public String getUsername() {
        return "lotmen";
    }

    public String getPassword() {
        return "fab688";
    }

}
