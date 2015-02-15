package hse.zhizh.abfclient.Session;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class SessionImpl implements Session {
    private String username;
    private String userpass;

    //метод для вывода результата запроса
    private void print_content(HttpsURLConnection con){
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //Действие аутентефикации(задает имя пользователя и пароль по умолчанию для соединений)
    private void authenticate(){
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,userpass.toCharArray());
            }
        });
    }

    /*
    Создание Https соединения и получения группы пользователя(используется для подтверждения логина)
     */
    public HttpsURLConnection createConnection() throws Exception {
        String https_url = "https://abf.rosalinux.ru/api/v1/groups.json";
        URL url;
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            setConnectionProperties(con,"GET");
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }

    /*
    Устанавливает свойства для https соединения con
    @con-соединение
     */
    public static void setConnectionProperties(HttpsURLConnection con,String requestMethod) throws  Exception{
        con.setAllowUserInteraction(true);
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    }
    /*
    Инициализация сессии с указанным именем пользователя и паролем
    задает имя пользователя и пароль для последующих действий
     */
    public SessionImpl (String username,String userpass){
        this.username=username;
        this.userpass=userpass;
        authenticate();
    }
    /*
    Функция для строкового представления ответа от сервера
    @con - соединение по которому был отправлен запрос
     */
    public static String requestContent(HttpsURLConnection con){
        String content="";
        if(con!=null){
            try {
                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                    //content = input;
                    content+=input;
                }
                br.close();
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return content;
    }
}