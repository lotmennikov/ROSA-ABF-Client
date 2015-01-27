package hse.zhizh.abfclient.ABFQueries;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import hse.zhizh.abfclient.Activities.CommandResultListener;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.api.ProjectsRequest;

/**
 * Получение списка проектов
 *
 * Created by E-Lev on 07.01.2015.
 */
public class ABFProjects extends ABFQuery {

    public Project[] projects;
    private final CommandResultListener activity;

    public ABFProjects(CommandResultListener activ) {
        activity = activ;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String json = new ProjectsRequest().getProjects();
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.d("ABF-Client GetProjects", "Projects were successfully obtained");
        } else {
            Log.d("ABF-Client GetProjects", "Failed to get objects");
        }
        activity.onCommandExecuted(success);
    }


}
