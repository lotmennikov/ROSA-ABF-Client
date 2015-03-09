package hse.zhizh.abfclient.ABFQueries;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Асинхронное выполнение запросов к серверу ABF
 *
 * Created by E-Lev on 07.01.2015.
 */
public abstract class ABFQuery extends AsyncTask<Void, Void, Boolean> {
    public static final int PROJECTS_QUERY = 201;
    public static final int BUILDS_QUERY = 202;
    public static final int PLATFORMS_QUERY = 203;
    public static final int NEWBUILD_QUERY = 204;
    public static final int PROJECTID_QUERY = 205;


}
