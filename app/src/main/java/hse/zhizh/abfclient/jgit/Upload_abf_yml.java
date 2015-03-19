package hse.zhizh.abfclient.jgit;

//import org.apache.commons.codec.binary.Base64;
import android.util.Base64;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.common.Settings;

/**
 * Created by Administrator on 3/7/2015.
 */
public class Upload_abf_yml {

    private Repository repository;
    public String errorMessage;
    private int statusCode;

    public Upload_abf_yml(Repository repository) {
        this.repository = repository;
    }


    public int upload_abf_yml(File fileToUpload) {

         /*   String urlToConnect = "http://file-store.rosalinux.ru/api/v1/upload";
        File fileToUpload = new File("C:/test2.txt");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

        URLConnection connection = null;
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("creepycheese", "ewqforce1".toCharArray());
            }
        });
        try {
            connection = new URL(urlToConnect).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true); // This sets request method to POST.
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        PrintWriter writer = null;
        try {
            try {
                writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"test2.txt\"");
            writer.println( "Content-Type: "  + URLConnection.guessContentTypeFromName("test2.txt"));
            writer.println("Content-Transfer-Encoding: binary");
            writer.println();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                for (String line; (line = reader.readLine()) != null;) {
                    writer.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            }
            writer.println("--" + boundary + "--");
        } finally {
            if (writer != null) writer.close();
        }


// Connection is lazily executed whenever you request any status.
        int responseCode = 0;
        try {
            responseCode = ((HttpURLConnection) connection).getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(responseCode); // Should be 200*/

        //HttpClient client = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        InputStreamEntity reqEntity = null;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(fileToUpload));
       /* try {
            reqEntity = new InputStreamEntity(
                    new FileInputStream(fileToUpload), -1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true); // Send in multiple parts if needed*/
        HttpPost request = new HttpPost("http://file-store.rosalinux.ru/api/v1/upload");
        request.setEntity(builder.build());
        String encoding = Base64.encodeToString(new String(Settings.repo_username + ":" + Settings.repo_password).getBytes(), Base64.NO_WRAP);
        request.setHeader("Authorization", encoding);
        CloseableHttpResponse response2 = null;
        Header[] headers = request.getAllHeaders();
        try {
            response2 = httpclient.execute(request);
        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity2.getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject json = new JSONObject(result.toString());
            String hash = null;
            if (response2.getStatusLine().getStatusCode() == 422) {
                System.out.println("This file has already been uploaded!");
                hash = ((JSONArray)json.get("sha1_hash")).getString(0);
                hash = hash.substring(1, hash.indexOf('\'', 1));
            } else if (response2.getStatusLine().getStatusCode() == 201) {
                statusCode = 201;
                System.out.println("File uploaded successfully!");
                hash = json.getString("sha1_hash");
                File abf_yml_file = new File(repository.getDir().toString() +"/.abf.yml");
                if (!abf_yml_file.exists()) {
                    abf_yml_file.getParentFile().mkdirs();
                    abf_yml_file.createNewFile();
                }
                FileWriter fw = new FileWriter (abf_yml_file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                if (abf_yml_file.length() == 0) {
                    bw.write("sources:");
                    bw.newLine();
                }
                bw.write("  \"" + fileToUpload.getName() + "\": " + hash);
                bw.close();
            } else {
                statusCode = response2.getStatusLine().getStatusCode();
                System.out.println("Unknown response code!");
                return -1;
            }
//TODO где этот метод лежит??
//            EntityUtils.consume(entity2);

        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return -1;
        } catch (JSONException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return -1;
        } finally {
        try {
            response2.close();
        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
    }
        return statusCode;
    }
}
