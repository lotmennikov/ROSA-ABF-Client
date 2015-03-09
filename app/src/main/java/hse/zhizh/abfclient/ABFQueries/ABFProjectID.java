package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.api.ProjectsRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 09.03.2015.
 */
public class ABFProjectID extends ABFQuery {
    private final String COMMANDTAG = " ABF ProjectID";

    private final CommandResultListener activity;
    private final String projectName;
    private final String groupName;

    public Project result;

    public ABFProjectID(CommandResultListener activ, String projectName, String groupName) {
        this.activity = activ;
        this.result = null;
        this.projectName = projectName;
        this.groupName = groupName;
    }

    /*
        Получение массива сборок
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "Sending request...");
        try {
            result = new ProjectsRequest().getProjectByOwnerAndName(groupName, projectName);
            if (result != null) {
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
            Log.d(Settings.TAG + COMMANDTAG, "success");
        } else {
            Log.d(Settings.TAG + COMMANDTAG, "fail");
        }
        activity.onCommandExecuted(PROJECTID_QUERY, success);
    }


}
