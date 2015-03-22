package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.ProjectRef;
import hse.zhizh.abfclient.Model.ProjectRepo;
import hse.zhizh.abfclient.Session.SessionImpl;
import hse.zhizh.abfclient.common.Settings;

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
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return requestContent(con);
    }


    /*
    Проверка проекта на существование
     */
    public boolean isProjectExist(int id) throws Exception{
        String https_url = "https://abf.rosalinux.ru/api/v1/projects/:id.json";
        https_url=https_url.replace(":id",Integer.toString(id));
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
            return code == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return false;
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
          //  new SessionImpl(Settings.repo_username,Settings.repo_password).setConnectionProperties(con, "GET");
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con,"GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return parseJsonResponse(requestContent(con));
    }

    public ProjectRef[] getProjectRefs(int id) throws Exception{
        String https_url = "https://abf.rosalinux.ru/api/v1/projects/:id/refs_list.json";
        https_url=https_url.replace(":id",Integer.toString(id));
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con,"GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return parsejsonRefsList(requestContent(con));
    }

    //парсинг запроса получения ссылок проекта
    public static ProjectRef[] parsejsonRefsList(String response){
        ProjectRef[] refs=null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arr = jsonObject.getJSONArray("refs_list");
            refs= new ProjectRef[arr.length()];
            for (int i = 0;i<arr.length();i++){
                JSONObject ref = arr.getJSONObject(i);
                JSONObject obj = ref.getJSONObject("object");
                String sha = obj.getString("sha");
                String ref_name = ref.getString("ref");
                refs[i] = new ProjectRef(sha,ref_name);
                System.out.println(sha);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return refs;
    }

    public ProjectRepo[] projectReposRequest(int id) throws Exception{
        String https_url = "https://abf.rosalinux.ru/api/v1/projects/:id.json";
        https_url=https_url.replace(":id",Integer.toString(id));
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con,"GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseProjectReposResponse(requestContent(con));
    }


    private ProjectRepo[] parseProjectReposResponse(String response) throws Exception{
        String json = response;
        JSONObject jsonObject = new JSONObject(json);
        JSONObject proj = (JSONObject)jsonObject.get("project");
        JSONArray arr = proj.getJSONArray("repositories");
        ProjectRepo[] projReps = new ProjectRepo[arr.length()];
        for (int i = 0;i<arr.length();i++) {
            JSONObject ref = arr.getJSONObject(i);
            Integer id = ref.getInt("id");
            String name = ref.getString("name");
            projReps[i] = new ProjectRepo(id,name);
        }
        return projReps;
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
            if(!pgiturl.contains("@")){
                System.out.println("replacing");
                pgiturl=pgiturl.replace("abf.",Settings.repo_username+"@abf.");
            }
            String pdescription = "";
            int powner = proj.getJSONObject("owner").getInt("id");
            project = new Project(pid,pname, pfullname, pgiturl, pdescription, powner);
/*            //Получения репозиториев проекта
            JSONArray arr = proj.getJSONArray("repositories");
            ProjectRepo[] projReps = new ProjectRepo[arr.length()];
            for (int i = 0;i<arr.length();i++) {
                JSONObject ref = arr.getJSONObject(i);
                Integer id = ref.getInt("id");
                String name = ref.getString("name");
                projReps[i] = new ProjectRepo(id,name);
            }
            project.setProjectRepositories(projReps);
            project.setProjectRefs(getProjectRefs(project.getId())); */
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
            e.printStackTrace();
            return null;
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
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con,"DELETE");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return requestContent(con);
    }
 }
