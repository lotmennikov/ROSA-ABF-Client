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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import hse.zhizh.abfclient.common.Settings;

public class SessionImpl implements Session {
    private static String username;
    private static String userpass;

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

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Действие аутентефикации(задает имя пользователя и пароль по умолчанию для соединений)
    public static void authenticate(HttpsURLConnection con){
    /*    Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,userpass.toCharArray());
            }
        });*/
        String encoding = Base64.encodeToString(new String(username + ":" + userpass).getBytes(), Base64.NO_WRAP);
        encoding = "Basic " + encoding;
        con.setRequestProperty("Authorization", encoding);
    }

    /*
    Создание Https соединения и получения группы пользователя(используется для подтверждения логина)
     */
    public HttpsURLConnection createConnection() throws Exception {
        String https_url = "https://abf.rosalinux.ru/api/v1/groups.json";
        URL url;
        trustAllHosts();
        HttpsURLConnection con=null;
        try {
          /*  String encoding = Base64.encodeToString(new String(username + ":" + userpass).getBytes(), Base64.NO_WRAP);
            encoding = "Basic " + encoding;*/
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            authenticate(con);
            setConnectionProperties(con,"GET");
           // con.setRequestMethod("GET");
          //  con.setRequestProperty("Authorization", encoding);
            con.connect();
            int code = con.getResponseCode();
            System.out.println("code: " + code);
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
      //  con.setAllowUserInteraction(true);
        con.setRequestMethod(requestMethod);
       // con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    }
    /*
    Инициализация сессии с указанным именем пользователя и паролем
    задает имя пользователя и пароль для последующих действий
     */
    public SessionImpl (String username,String userpass){
        this.username=username;
        this.userpass=userpass;
      //  authenticate();
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

                    content += input;
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