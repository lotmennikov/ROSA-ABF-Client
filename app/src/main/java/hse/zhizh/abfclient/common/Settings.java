package hse.zhizh.abfclient.common;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static final String TAG = "ABF Client";


//    public static String repo_username = "lotmen";
//    public static String repo_password = "fab688";

    public static String repo_username = "lotmen";
    public static String repo_password = "fab688";

    // user projects
//    public static List<Project> projects;

    // selected project
    public static Project currentProject;

    // list of active repositories
//    public static List<Repository> repositoryList;
    // current repository (temp)
//    public static Repository currentRepository;
    // current user
//    public static User currentUser;

    // getApplicationContext()
    public static Context appContext;

    static {
//        repositoryList = new ArrayList<Repository>();
//        currentUser = null;
//        currentRepository = null;
        currentProject = null;
//        projects = initProjects();
//        currentProject = projects.get(0);
    }

    public static DateFormat commitDate = new SimpleDateFormat("dd.MM.yyyy");

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


}
