package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import hse.zhizh.abfclient.Model.Architecture;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Session.Session;
import hse.zhizh.abfclient.Session.SessionImpl;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Класс для получения архитектур пользователя
 * http://abf-doc.rosalinux.ru/abf/api/v1/architectures/
 */
public class ArchesRequest implements ApiRequest {
    String ARCHES_URL = "https://abf.rosalinux.ru/api/v1/arches.json";
    public String sendRequest(String... request_args) throws Exception{
        String https_url = ARCHES_URL;
        URL url;
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
        return SessionImpl.requestContent(con);
    }

    public Architecture[] getArches(){
        Architecture[] arches;
        try {
            String response = sendRequest();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonProjects = (JSONArray)jsonObject.get("architectures");
            arches = new Architecture[jsonProjects.length()];
            String name="";
            int id =0;
            for (int i = 0; i < jsonProjects.length(); ++i) {
                JSONObject proj = jsonProjects.getJSONObject(i);
                id = proj.getInt("id");
                name = proj.getString("name");
                arches[i] = new Architecture(id,name);
            }
            return arches;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
