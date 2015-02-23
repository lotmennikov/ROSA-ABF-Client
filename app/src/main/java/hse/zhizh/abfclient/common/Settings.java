package hse.zhizh.abfclient.common;

import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.Model.User;

/**
 * Created by E-Lev on 02.02.2015.
 */
public class Settings {


    // list of active repositories
    public static List<Repository> repositoryList;

    // current user
    public static User currentUser;


    static {
        repositoryList = new ArrayList<Repository>();
        currentUser = null;
    }



}
