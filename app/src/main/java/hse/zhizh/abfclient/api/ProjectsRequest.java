package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Session.SessionImpl;

import static hse.zhizh.abfclient.Session.SessionImpl.requestContent;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Класс запроса для получения проектов
 */
public class ProjectsRequest implements ApiRequest {
    String PROJECTS_URL = "https://abf.rosalinux.ru/api/v1/projects";
    /*
    Посылка запроса к апи для получения проектов
    if request_args.length==0 возвращает все проекты
    request_args[0] - id проекта
     */
    public String sendRequest(String... request_args) throws Exception{
        String https_url = PROJECTS_URL;
        if(request_args.length == 1){
            https_url=https_url.concat("/"+request_args[0]+".json");
        }else
        {
            https_url=https_url.concat(".json");
        }
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.setConnectionProperties(con,"GET");
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



    public Project getProjectByOwnerAndName(String owner, String name) throws Exception{
        String https_url = "https://abf.rosalinux.ru/api/v1/projects/get_id.json?name=:name&owner=:owner";
        https_url=https_url.replace(":owner",owner).replace(":name",name);
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.setConnectionProperties(con,"GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJsonResponse(requestContent(con));
    }

    // парсинг json на один проект
    private Project parseJsonResponse(String response){
        Project project;
        try {
            String json = response;
            JSONObject jsonObject = new JSONObject(json);

            JSONObject proj = (JSONObject)jsonObject.get("project");

            int pid = proj.getInt("id");
            String pname = proj.getString("name");
            String pfullname = proj.getString("fullname");
            String pgiturl = proj.getString("git_url");
            String pdescription = "";
            int powner = proj.getJSONObject("owner").getInt("id");
            project = new Project(pid,pname, pfullname, pgiturl, pdescription, powner);

            return project;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*
    Получение всех проектов
     */
    public String getProjects(){
        try {
            return sendRequest();
        } catch(Exception e) {
            return "[]";
        }
    }

    /*
    Получает проект по его id
    String @id-id проекта
    при неудаче возвращаеет "[]"
    */
    public String getProject(String id){
        try {
            return sendRequest(id);
        } catch(Exception e) {
            return "[]";
        }
    }

    /*
    Удаление проекта по id
    @id - id удаляемого проекта
     */
    public String deleteProject(String id) throws Exception{
        String https_url = PROJECTS_URL+"/"+id;
        URL url;
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.setConnectionProperties(con,"DELETE");
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
 }
