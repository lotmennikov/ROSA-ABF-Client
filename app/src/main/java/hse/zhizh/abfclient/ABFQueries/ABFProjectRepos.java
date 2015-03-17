package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.ProjectRepo;
import hse.zhizh.abfclient.api.ProjectsRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 16.03.2015.
 */
public class ABFProjectRepos  extends ABFQuery {

    private final String COMMANDTAG = "ABFProjectRepos";

    private final CommandResultListener activity;
    private final int projectId;

    public ProjectRepo[] result;

    public ABFProjectRepos(CommandResultListener activ, int project_id) {
        this.activity = activ;
        this.projectId = project_id;
        this.result = null;
    }

    /*
        Получение архитектур
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " Sending request...");
        try {
            ProjectsRequest reposRequest = new ProjectsRequest();
            ProjectRepo[] repos = reposRequest.projectReposRequest(projectId);
            if (repos != null) {
                result = repos;
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
        activity.onCommandExecuted(PROJECTREPOS_QUERY, success);
    }
}