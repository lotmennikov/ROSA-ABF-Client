package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.ProjectRef;
import hse.zhizh.abfclient.api.ProjectsRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 16.03.2015.
 */
public class ABFProjectRefs extends ABFQuery {

    private final String COMMANDTAG = "ABF ProjectRefs";

    private final CommandResultListener activity;
    private final int projectId;

    public ProjectRef[] result;

    public ABFProjectRefs(CommandResultListener activ, int project_id) {
        this.activity = activ;
        this.projectId = project_id;
        this.result = null;
    }

    /*
        Получение references проекта
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " Sending request...");
        try {
           ProjectsRequest refsRequest = new ProjectsRequest();
           ProjectRef[] projectRefs = refsRequest.getProjectRefs(projectId);
            if (projectRefs != null) {
                result = projectRefs;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d(Settings.TAG, COMMANDTAG + " success");
        } else {
            Log.d(Settings.TAG, COMMANDTAG + " fail");
        }
        activity.onCommandExecuted(PROJECTREFS_QUERY, success);
    }
}