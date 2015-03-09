package hse.zhizh.abfclient.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 07.03.15.
 */
public class BuildsRequest implements ApiRequest {
    String BUILDS_URL = "https://abf.rosalinux.ru/api/v1/:project_id/build_lists.json";
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
