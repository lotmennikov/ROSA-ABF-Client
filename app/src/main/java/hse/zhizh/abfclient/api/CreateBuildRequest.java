package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Repo;
import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Класс запроса для получения проектов
 */
public class CreateBuildRequest implements ApiRequest {
    String PROJECTS_URL = "https://abf.rosalinux.ru/api/v1/build_lists.json";
    /*
    Посылка запроса к апи для получения проектов
    if request_args.length==0 возвращает все проекты
    request_args[0] - id проекта
     */
    public String sendRequest(String... request_args) throws Exception{
        String https_url = PROJECTS_URL;
        https_url=https_url.concat("/.json");
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.setConnectionProperties(con,"POST");
            String urlParameters = "project_id="+request_args[0]+"&commit_hash="+request_args[1]+"&update_type="+request_args[2]+
                    "&save_to_repository_id="+request_args[3]+"&build_for_platform_id="+request_args[4]+
                    "&include_repos="+request_args[5]+"&arch_id="+request_args[6];

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
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

    public BuildResponse createBuildList(int project_id,String commit_hash,String update_type,int save_to_repository_id,
                                 int build_for_platform_id,int[] include_repos_id,int arch_id) throws Exception{
        return parseJsonResponse(sendRequest());

        //TODO ДОПИЛИТЬ И РАЗОБРАТЬСЯ С @include_repos, как правильно передать массив
    }

    private BuildResponse parseJsonResponse(String response){
        BuildResponse build;
        try {
            String json = response;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject proj = (JSONObject)jsonObject.get("build_list");
            int id = proj.getInt("project_id");
            String message = proj.getString("message");
            build = new BuildResponse(id,message);
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
