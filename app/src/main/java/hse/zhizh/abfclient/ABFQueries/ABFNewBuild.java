package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.api.CreateBuildRequest;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 08.03.2015.
 */
public class ABFNewBuild extends ABFQuery {

    private final String COMMANDTAG = " ABFNewBuild";

    private final CommandResultListener activity;
    private final int projectId;
    private final String commitHash;
    private final String updateType;
    private final int repoId;
    private final int platformId;
    private final int[] includeRepos;
    private final int archId;

    public BuildResponse result;

    public ABFNewBuild(CommandResultListener activ,
                       int project_id, String commit_hash,
                       String update_type, int repo_id,
                       int platform_id, int[] include_repos, int arch_id) {
        this.activity = activ;
        this.result = null;
        this.projectId = project_id;
        this.commitHash = commit_hash;
        this.updateType = update_type;
        this.repoId = repo_id;
        this.platformId = platform_id;
        this.includeRepos = include_repos;
        this.archId = arch_id;
    }

    /*
        Получение массива сборок
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG, COMMANDTAG + " Sending request...");
        try {
            CreateBuildRequest breq = new CreateBuildRequest();
            BuildResponse br = breq.createBuildList(projectId, commitHash, updateType, repoId, platformId, includeRepos, archId);
            if (br != null) {
                result = br;
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
        activity.onCommandExecuted(NEWBUILD_QUERY, success);
    }
}

