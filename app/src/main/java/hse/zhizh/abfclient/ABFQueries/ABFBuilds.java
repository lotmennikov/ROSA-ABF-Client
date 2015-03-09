package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.api.BuildsRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 08.03.2015.
 */
public class ABFBuilds extends ABFQuery {

    private final String COMMANDTAG = "ABF Builds";

    private final CommandResultListener activity;
    private final int projectId;

    public String response;
    public Build[] builds;

    public ABFBuilds(CommandResultListener activ, int project_id) {
        activity = activ;
        builds = null;
        projectId = project_id;
    }

    /*
        Получение массива сборок
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "Sending request...");
        builds = new BuildsRequest().getBuilds(projectId);

        if (builds != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG + COMMANDTAG, "success");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "fail");
        }
        activity.onCommandExecuted(BUILDS_QUERY, success);
    }
}

