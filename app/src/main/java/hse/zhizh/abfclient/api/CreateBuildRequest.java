package hse.zhizh.abfclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Repo;
import hse.zhizh.abfclient.Session.SessionImpl;

/**
 * Created by EvgenyMac on 18.01.15.
 *
 * Класс запроса для получения проектов
 */
public class CreateBuildRequest implements ApiRequest {
    String NEW_BUILD_URL = "https://abf.rosalinux.ru/api/v1/build_lists.json";
    /*
    Посылка запроса к апи для получения проектов
    if request_args.length==0 возвращает все проекты
    request_args[0] - id проекта
     */
    public String sendRequest(String... request_args) throws Exception{
        String https_url = NEW_BUILD_URL;
        URL url;
        System.out.println("URL: "+https_url);
        HttpsURLConnection con=null;
        try {
            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();
            SessionImpl.authenticate(con);
            SessionImpl.setConnectionProperties(con,"POST");
//            String urlParameters = "build_list[project_id]=118524" +
//                    "&build_list[commit_hash]=f31e22b1968c795b2cf2567137102c5e512c4971" +
//                    "&build_list[update_type]=recommended&" +
//                    "build_list[save_to_repository_id]=1067" +
//                    "&build_list[build_for_platform_id]=376" +
//                    "&build_list[include_repos][]=388" +
//                    "&build_list[arch_id]=2";
            String urlParameters =
                    "build_list[project_id]=:project_id" +
                    "&build_list[commit_hash]=:commit_hash" +
                    "&build_list[update_type]=:update_type" +
                    "&build_list[save_to_repository_id]=:save_to_repository_id" +
                    "&build_list[build_for_platform_id]=:build_for_platform_id" +
                    ":include_repos" +
                    "&build_list[arch_id]=:arch_id";
            urlParameters=urlParameters
            .replace(":project_id",request_args[0])
            .replace(":commit_hash",request_args[1])
            .replace(":update_type",request_args[2])
            .replace(":save_to_repository_id",request_args[3])
            .replace(":build_for_platform_id",request_args[4])
            .replace(":include_repos",request_args[5])
            .replace(":arch_id",request_args[6]);

            System.out.println("Params: "+urlParameters);

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
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return SessionImpl.requestContent(con);
    }

    public BuildResponse createBuildList(int project_id,String commit_hash,String update_type,int save_to_repository_id,
                                 int build_for_platform_id,int[] include_repos_id,int arch_id) throws Exception{
        String s_project_id=Integer.toString(project_id);
        String s_save_to_repositiry_id=Integer.toString(save_to_repository_id);
        String s_build_for_platform_id=Integer.toString(build_for_platform_id);
        String s_include_repos_id=convert_to_include_repos_array(include_repos_id);
        String s_arch_id=Integer.toString(arch_id);
        return parseJsonResponse(sendRequest(s_project_id,commit_hash,update_type,s_save_to_repositiry_id
                ,s_build_for_platform_id,s_include_repos_id,s_arch_id));
    }

    private String convert_to_include_repos_array(int[] include_repos_id){
      String converted ="";
        for(int i = 0;i<include_repos_id.length;i++){
            converted+="&build_list[include_repos][]="+include_repos_id[i];
        }
        return converted;
    }

    private BuildResponse parseJsonResponse(String response){
        BuildResponse build;
        try {
            String json = response;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject proj = (JSONObject)jsonObject.get("build_list");
            int id = proj.getInt("id");
            String message = proj.getString("message");
            build = new BuildResponse(id,message);
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
