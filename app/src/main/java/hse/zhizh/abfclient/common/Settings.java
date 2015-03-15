package hse.zhizh.abfclient.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    // current user credentials
    public static String repo_username = "lotmen";
    public static String repo_password = "fab688";

    // selected project
    public static Project currentProject;

    // getApplicationContext()
    public static Context appContext;

    static {
        currentProject = null;
        downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

// заглушечный тупейший менеджер паролей

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

}
