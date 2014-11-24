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

public class SessionImpl {
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
    Создание Https соединения и получения груп пользователя(используется для подтверждения логина)
     */
    public HttpsURLConnection createConnection() throws Exception {
        String https_url = "https://abf.rosalinux.ru/api/v1/groups.json";
        URL url;
        HttpsURLConnection con=null;
        try {

            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            con.setAllowUserInteraction(true);
            con.setRequestMethod("GET");
            con.connect();
            //dumpl all cert info
            //print_https_cert(con);

            //dump all the content
            int code = con.getResponseCode();
            //print_content(con);
            System.out.println("code:" + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
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
    public String requestContent(HttpsURLConnection con){
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
                    //content+=input+"\n";
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return content;
    }
}