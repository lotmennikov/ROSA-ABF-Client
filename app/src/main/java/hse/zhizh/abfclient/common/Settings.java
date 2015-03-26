package hse.zhizh.abfclient.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hse.zhizh.abfclient.Model.BuildPreference;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.Model.User;
import hse.zhizh.abfclient.R;

/**
 * Created by E-Lev on 02.02.2015.
 */
public class Settings {
    // Default App tag
    public static final String TAG = "ABF Client";

    // Rest api request
    public static final String abfApiRequest = "https://abf.rosalinux.ru/api/v1/";

    // default folder for user downloads from filestore
    public static File downloadsDir;

    // date formatter for the commits list
    public static DateFormat commitDate = new SimpleDateFormat("dd.MM.yyyy");

    //default directory for repositories
    public static final String repo_path = "mnt/sdcard/Android/data/hse.zhizh.abfclient/repo/";

    // current user credentials
    public static String repo_username = null;
    public static String repo_password = null;
    public static String repo_email = null;

    // selected project
    public static Project currentProject;

    // getApplicationContext()
    public static Context appContext;

    static {
        currentProject = null;
        downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

// заглушечный тупейший менеджер паролей (но для других приложений недоступен)

    public static String getFromTempPasswordHolder(Context context, String username) {
       // SharedPreferences prefs = context.getSharedPreferences("")
        SharedPreferences shp = context.getSharedPreferences(context.getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        String password = shp.getString("password_"+username, "");
        return password;
    }

    public static void setTempPasswordHolder(Context context, String username, String password) {
        SharedPreferences shp = context.getSharedPreferences(context.getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        shp.edit()
                .putString("password_"+username, password)
                .apply();
    }

    public static void authSuccess(Context context, String username, String password, boolean remember) {

        SharedPreferences shp = context.getSharedPreferences(context.getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        shp.edit()
                .putString("last_user", username)
                .apply();

        repo_username = username;
        repo_password = password;

        if (remember) {
            setTempPasswordHolder(context, username, password);
        }
    }

    public static String getLastUsername(Context context) {
        SharedPreferences shp = context.getSharedPreferences(context.getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        String username = shp.getString("last_user", "");

        return username;
    }

// настройки сборки

    public static BuildPreference getBuildPrefs(String projectname) {
        SharedPreferences shp = appContext.getSharedPreferences(appContext.getString(R.string.build_preferences_file), Context.MODE_PRIVATE);
        String platform = shp.getString(projectname + "_platform", "");
        Set<String> plReposSet = shp.getStringSet(projectname + "_plrepos", new HashSet<String>());
        String repo = shp.getString(projectname + "_repo", "");
        String ref = shp.getString(projectname + "_ref", "");
        String arch = shp.getString(projectname + "_arch", "");
        String update = shp.getString(projectname + "_update", "");

        List<String> plRepos = new ArrayList<String>(plReposSet);

        return new BuildPreference(platform, plRepos, arch, repo, ref, update);

    }

    public static void setBuildPrefs(String projectname, BuildPreference prefs) {
        Set<String> plrepos = new HashSet<String>();
        plrepos.addAll(prefs.getPlRepos());
        SharedPreferences shp = appContext.getSharedPreferences(appContext.getString(R.string.build_preferences_file), Context.MODE_PRIVATE);
        shp.edit().putString(projectname + "_platform", prefs.getPlatform())
                  .putStringSet(projectname + "_plrepos", plrepos)
                  .putString(projectname + "_repo", prefs.getRepository())
                  .putString(projectname + "_ref", prefs.getRef())
                  .putString(projectname + "_arch", prefs.getArchitecture())
                  .putString(projectname + "_update", prefs.getUpdateType()).apply();
    }

}
