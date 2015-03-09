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
        Получение JSON из запроса к REST API
        и его разбор на массив сборок
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(Settings.TAG + COMMANDTAG, "Sending request...");
        try {
           String json = new BuildsRequest().sendRequest(Integer.toString(projectId));  //new ProjectsRequest().getProjects();
            response = json;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonBuilds = (JSONArray)jsonObject.get("projects");
            builds = new Build[jsonBuilds.length()];
            String url = "";
            int status = 0;
            int build_id = 0 , project_id = 0 ;
            for (int i = 0; i < jsonBuilds.length(); ++i) {
                JSONObject proj = jsonBuilds.getJSONObject(i);
                build_id = proj.getInt("id");
                status = proj.getInt("status");
                project_id = proj.getInt("project_id");
                url = proj.getString("url");
                builds[i] = new Build(build_id,project_id,200,status,url);
            }
            // TODO убрать return, распарсить сборки
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
//        return true;
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

