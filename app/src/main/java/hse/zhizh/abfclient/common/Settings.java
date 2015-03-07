package hse.zhizh.abfclient.common;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.Model.User;

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
    public static List<Project> projects;

    // selected project
    public static Project currentProject;

    // list of active repositories
    public static List<Repository> repositoryList;

    // current repository (temp)
    public static Repository currentRepository;

    // current user
    public static User currentUser;

    // getApplicationContext()
    public static Context appContext;

    static {
        repositoryList = new ArrayList<Repository>();
        currentUser = null;
        currentRepository = null;
        projects = initProjects();
        currentProject = projects.get(0);
    }

    // temporary initialization
    // TODO replace by loading from database
    static List<Project> initProjects() {
        List<Project> projects = new ArrayList<>();
        Project testProject = new Project(1, "0ad", "lotmen/0ad", "lotmen/0ad.git", "descr", 1);
//        testProject.initRepo();
        projects.add(testProject);

        // some other projects


        return projects;
    }

}
