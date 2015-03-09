package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 07.03.15.
 */
public class BuildsRequest implements ApiRequest {
    String BUILDS_URL = "https://abf.rosalinux.ru/api/v1/projects/:project_id/build_lists.json";

    // внешний запрос для получения списка сборок
    public Build[] getBuilds(int projectId) {
        try {
            String json = sendRequest(Integer.toString(projectId));  //new ProjectsRequest().getProjects();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonBuilds = (JSONArray) jsonObject.get("build_lists");
            Build[] builds = new Build[jsonBuilds.length()];
            String url = "";
            int status = 0;
            int build_id = 0, project_id = 0;
            for (int i = 0; i < jsonBuilds.length(); ++i) {
                JSONObject proj = jsonBuilds.getJSONObject(i);
                build_id = proj.getInt("id");
                status = proj.getInt("status");
                project_id = proj.getInt("project_id");
                url = proj.getString("url");
                builds[i] = new Build(build_id, project_id, status, url);
            }

            return builds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    Посылка запроса к апи для получения сборок для проекта
    @args[0]=project_id - id проекта, для которого необходимо получить сборки
     */
    public String sendRequest(String... args) throws Exception{
        String https_url=BUILDS_URL.replace(":project_id",args[0]);
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.setConnectionProperties(con, "GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SessionImpl.requestContent(con);
    }

}
