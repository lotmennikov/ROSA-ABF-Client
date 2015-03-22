package hse.zhizh.abfclient.jgit;

import android.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    private String errorMessage;
    private String statusMessage;

    public Upload_abf_yml(Repository repository) {
        this.repository = repository;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean upload_abf_yml(File fileToUpload) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(fileToUpload));
        HttpPost request = new HttpPost("http://file-store.rosalinux.ru/api/v1/upload");
        request.setEntity(builder.build());
        String encoding = Base64.encodeToString(new String(Settings.repo_username + ":" + Settings.repo_password).getBytes(), Base64.NO_WRAP);
        request.setHeader("Authorization", encoding);
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(request);
        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
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
                statusMessage = response2.getStatusLine().getReasonPhrase();
                System.out.println("This file has already been uploaded!");
                hash = ((JSONArray)json.get("sha1_hash")).getString(0);
                hash = hash.substring(1, hash.indexOf('\'', 1));
                writeToAbfYmlFile(fileToUpload, hash);
            } else if (response2.getStatusLine().getStatusCode() == 201) {
                statusMessage = response2.getStatusLine().getReasonPhrase();
                System.out.println("File uploaded successfully!");
                hash = json.getString("sha1_hash");
                File abf_yml_file = new File(repository.getDir().toString() +"/.abf.yml");
                if (!abf_yml_file.exists()) {
                    abf_yml_file.getParentFile().mkdirs();
                    abf_yml_file.createNewFile();
                }
                writeToAbfYmlFile(fileToUpload, hash);
            } else {
                statusMessage = response2.getStatusLine().getReasonPhrase();
                System.out.println("Unknown response code!");
                return false;
            }

//            EntityUtils.consume(entity2);

        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        } finally {
        try {
            response2.close();
        } catch (IOException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
    }
        return true;
    }

    private void writeToAbfYmlFile(File fileToUpload, String hash) {
        File abf_yml_file = new File(repository.getDir().toString() +"/.abf.yml");
        if (!abf_yml_file.exists()) {
            abf_yml_file.getParentFile().mkdirs();
            try {
                abf_yml_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String s = null;
        try {
            s = FileUtils.readFileToString(abf_yml_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.replaceAll("(?m)^[ \t]*\r?\n", "");
        String fileString = "  \"" + fileToUpload.getName() + "\": " + hash;
        if (!s.contains(fileToUpload.getName() + "\": " + hash)) {
            if (s.isEmpty()) {
                s = "sources:\n" + fileString + s;
            } else if (s.startsWith("sources:\n")) {
                s = s.substring(0, "sources:\n".length()) + fileString + s.substring("sources\n".length());
            } else if (s.startsWith("sources:")) {
                s = s.substring(0, "sources:".length()) + "\n" + fileString + s.substring("sources".length());
            } else if (s.contains("\nsources:\n")) {
                s = s.substring(0, s.indexOf("\nsources:\n") + "\nsources:\n".length()) + fileString + "\n" + s.substring(s.indexOf("\nsources:\n") + "\nsources:\n".length());
            } else if (s.contains("\nsources:")) {
                s = s.substring(0, s.indexOf("\nsources:") + "\nsources:".length()) + "\n" + fileString + "\n" + s.substring(s.indexOf("\nsources:") + "\nsources:".length());
            } else if (!s.contains("\nsources:")) {
                s = "sources:\n" + fileString + s;
            }
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(abf_yml_file, false));
                bw.write(s);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
