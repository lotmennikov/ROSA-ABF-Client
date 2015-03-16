package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 08.03.2015.
 */
public class ABFPlatforms extends ABFQuery {
    private final String COMMANDTAG = " ABFPlatforms";

    private final CommandResultListener activity;

    public Platform[] result;

    public ABFPlatforms(CommandResultListener activ) {
        this.activity = activ;
        this.result = null;

    }

    /*
        Получение массива сборок
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " Sending request...");
        try {

        // TODO request
//            result = new ProjectsRequest().getProjectByOwnerAndName(groupName, projectName);
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
            Log.d(Settings.TAG, COMMANDTAG + " success");
        } else {
            Log.d(Settings.TAG, COMMANDTAG + " fail");
        }
        activity.onCommandExecuted(PLATFORMS_QUERY, success);
    }


}

