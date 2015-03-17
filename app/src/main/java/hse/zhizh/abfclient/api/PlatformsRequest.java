package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repo;
import hse.zhizh.abfclient.Session.SessionImpl;

import static hse.zhizh.abfclient.Session.SessionImpl.requestContent;

/**
 * Запрос для платформ
 */
public class PlatformsRequest implements ApiRequest {
    String PLATFORMS_URL = "https://abf.rosalinux.ru/api/v1/platforms";
    /*
    Посылка запроса к апи для получения проектов
    if request_args.length==0 возвращает все проекты
    request_args[0] - id проекта
     */
    public String sendRequest(String... request_args) throws Exception{
        String https_url = PLATFORMS_URL;
        if(request_args.length == 1){
            https_url=PLATFORMS_URL + "/" + request_args[0];
        }
        https_url=https_url.concat(".json");
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

        return requestContent(con);
    }

    public String sendRequestPlatformsForBuild(String... request_args) throws Exception{
        String https_url = "https://abf.rosalinux.ru/api/v1/platforms/platforms_for_build.json";
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
        return requestContent(con);
    }

    public Platform getPlatformById(int id) throws Exception{
        String s_id = Integer.toString(id);
        return parseJsonResponseById(sendRequest(s_id));
    }

    public Platform[] listPlatforms() throws Exception{
        return parseJsonListPlatforms(sendRequestPlatformsForBuild());
    }

    private Platform[] parseJsonListPlatforms(String response){
        Platform[] platforms;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonPlatforms = (JSONArray)jsonObject.get("platforms");
            platforms = new Platform[jsonPlatforms.length()];
            String name="";
            int id =0;
            for (int i = 0; i < jsonPlatforms.length(); ++i) {
                JSONObject proj = jsonPlatforms.getJSONObject(i);
                id = proj.getInt("id");
                name = proj.getString("name");
                JSONArray json_repos = proj.getJSONArray("repositories");
                Repo[] repos = new Repo[json_repos.length()];
                for(int j = 0;j <json_repos.length();j++){
                  JSONObject json_repo =json_repos.getJSONObject(j);
                  String repo_name = json_repo.getString("name");
                  int repo_id = json_repo.getInt("id");
                  repos[j]=new Repo(repo_id,repo_name);
                }
                platforms[i] = new Platform(id,name,repos);
            }
            return platforms;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Platform parseJsonResponseById(String response){
        Platform platform;
        try {
            String json = response;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject proj = (JSONObject)jsonObject.get("platform");
            int id = proj.getInt("id");
            String name = proj.getString("name");
            JSONArray json_repos = proj.getJSONArray("repositories");
            Repo[] repos = new Repo[json_repos.length()];
            for(int j = 0;j <json_repos.length();j++){
                JSONObject json_repo =json_repos.getJSONObject(j);
                String repo_name = json_repo.getString("name");
                int repo_id = json_repo.getInt("id");
                repos[j]=new Repo(repo_id,repo_name);
            }
            platform = new Platform(id,name,repos);
            return platform;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
