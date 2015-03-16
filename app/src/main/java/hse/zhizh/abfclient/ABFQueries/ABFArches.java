package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Architecture;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.api.ArchesRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 16.03.2015.
 */
public class ABFArches extends ABFQuery {

    private final String COMMANDTAG = " ABFArches";

    private final CommandResultListener activity;

    public Architecture[] result;

    public ABFArches(CommandResultListener activ) {
        this.activity = activ;
        this.result = null;
    }

    /*
        Получение архитектур
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " Sending request...");
        try {
            ArchesRequest archesRequest = new ArchesRequest();
            Architecture[] architectures = archesRequest.getArches();
            if (architectures != null) {
                result = architectures;
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
        activity.onCommandExecuted(ARCHES_QUERY, success);
    }
}

