package hse.zhizh.abfclient.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 18.01.15.
 */
public class GroupsRequest {
    String GROUPS_URL = "https://abf.rosalinux.ru/api/v1/groups";
    /*
    Посылка запроса к апи для получения групп
    if request_args.length==0 возвращает все групы
    request_args[0] - id проекта
     */
    public String sendRequest(String... request_args) throws Exception{
        String https_url = GROUPS_URL;
        if(request_args.length == 1){
            https_url=https_url.concat("/"+request_args[0]+".json");
        }else
        {
            https_url=https_url.concat(".json");
        }
        URL url;
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con, "GET");
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

        return SessionImpl.requestContent(con);
    }


    /*
    Получение всех проектов
     */
    public String getGroups(){
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
    public String getGroup(String id){
        try {
            return sendRequest(id);
        } catch(Exception e) {
            return "[]";
        }
    }
}
