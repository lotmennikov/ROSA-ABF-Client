package hse.zhizh.abfclient.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Класс запроса для получения проектов
 */
public class CreateBuildRequest implements ApiRequest {
    String PROJECTS_URL = "https://abf.rosalinux.ru//api/v1/build_lists.json";
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
            String urlParameters = "project_id="+request_args[0]+"&commit_hash"+request_args[1]+"&update_type="+request_args[2]+
                    "&save_to_repository_id="+request_args[3]+"&build_for_platform_id="+request_args[4]+
                    "&inlude_repos="+request_args[5]+"&arch_id="+request_args[6];

            // Send post request
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

        return SessionImpl.requestContent(con);
    }
}
