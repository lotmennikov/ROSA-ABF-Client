package hse.zhizh.abfclient.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

    public String getPlatformById(int id) throws Exception{
        String s_id = Integer.toString(id);
        return sendRequest(s_id);
    }

    public String listPlatforms() throws Exception{
        return sendRequest();
    }
}
