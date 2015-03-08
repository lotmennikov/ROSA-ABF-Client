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
/*            String json = new BuildsRequest().getProjects();  //new ProjectsRequest().getProjects();
            response = json;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonProjects = (JSONArray)jsonObject.get("projects");
            projects = new Project[jsonProjects.length()];
            String pname = "", pfullname = "", pgiturl = "", pdescription = "";
            int pid = 0 , powner = 0 ;
            for (int i = 0; i < jsonProjects.length(); ++i) {
                JSONObject proj = jsonProjects.getJSONObject(i);
                pid = proj.getInt("id");
                pname = proj.getString("name");
                pfullname = proj.getString("fullname");
                pgiturl = proj.getString("git_url");
                pdescription = proj.getString("description");
                powner = proj.getJSONObject("owner").getInt("id");
                projects[i] = new Project(pid,pname, pfullname, pgiturl, pdescription, powner);
            }
            // TODO убрать return, распарсить сборки
*/
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

