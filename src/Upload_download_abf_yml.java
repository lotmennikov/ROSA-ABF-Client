import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;


public class Upload_download_abf_yml {
    Repository repository;
    public Upload_download_abf_yml(Repository repository) {
        this.repository = repository;
    }
    public void upload_abf_yml(File fileToUpload) {

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

        CloseableHttpClient httpclient = HttpClients.createDefault();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(fileToUpload));
        HttpPost request = new HttpPost("http://file-store.rosalinux.ru/api/v1/upload");
        request.setEntity(builder.build());
        String encoding = new Base64().encodeAsString(new String("creepycheese:ewqforce1").getBytes());
        request.setHeader("Authorization", encoding);
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(request);
        } catch (IOException e) {
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
                System.out.println("Unknown response code!");
            }
            EntityUtils.consume(entity2);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void download_abf_yml() {
        File abf_yml_file = new File(repository.getDir().toString() +"/.abf.yml");
        if (abf_yml_file.exists()) {
            new File(repository.getDir().toString() + "/abf.yml files").mkdirs();
        } else {
            System.out.println("No .abf.yml file found!");
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(repository.getDir().toString() +"/.abf.yml"));
            br.readLine(); //sources:
            String line = br.readLine();
            while (line != null)
            {
                String hash = line.substring(line.indexOf(": ") + 2);
                URL url = new URL("http://file-store.rosalinux.ru/download/" + hash);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream input = urlConnection.getInputStream();
                if (line.contains(".tar.gz")) {
                    GZIPInputStream gzIn = new GZIPInputStream(input);
                    decompress_tar(gzIn, repository);
                    gzIn.close();
                } else if (line.contains(".tar.bz2")) {
                    BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                    decompress_tar(bzIn, repository);
                    bzIn.close();
               /* } else if(line.contains(".tar.xz")) {
                    XZInputStream xzIn = new XZInputStream(input);
                    decompress_tar(xzIn, reposit);
                    xzIn.close();*/
                } else if (line.contains(".tar")) {
                    decompress_tar(input, repository);
                    input.close();
                } else if (line.contains(".bz2")) {
                    BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                    final byte[] buffer = new byte[4096];
                    FileOutputStream output = new FileOutputStream(
                            new File(repository.getDir().toString() +
                                    "/abf.yml files/" + line.substring(line.indexOf('"') + 1, line.indexOf(".bz2"))));
                    int n = 0;
                    while (-1 != (n = bzIn.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    output.close();
                    bzIn.close();
                } else {
                    final byte[] buffer = new byte[4096];
                    FileOutputStream output = new FileOutputStream(
                            new File(repository.getDir().toString() +
                                    "/abf.yml files/" + line.substring(line.indexOf('"') + 1, line.indexOf("\":"))));
                    int n = 0;
                    while (-1 != (n = input.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    output.close();
                    input.close();
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in file .abf.yml!");
        }

    }

    private static void decompress_tar(InputStream stream, Repository reposit) throws IOException {
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(stream);
        TarArchiveEntry entry;
        String individualFiles;
        int offset;
        FileOutputStream outputFile;
        /* Create a loop to read every single entry in TAR file */
        while (( entry = myTarFile.getNextTarEntry()) != null) {
             /* Get the name of the file */
            individualFiles = reposit.getDir().toString() + "/abf.yml files/" + entry.getName();
            /* Get Size of the file and create a byte array for the size */
            if (entry.isDirectory()) {
                File outputDir = new File(individualFiles);
                outputDir.mkdirs();
            } else {
                byte[] content = new byte[(int) entry.getSize()];
                offset = 0;
                /* Read file from the archive into byte array */
                myTarFile.read(content, offset, content.length - offset);
                /* Define OutputStream for writing the file */
                outputFile = new FileOutputStream(new File(individualFiles));
                outputFile.write(content, 0, content.length);
                /* Close Output Stream */
                outputFile.close();
            }
        }
        /* Close TarAchiveInputStream */
        myTarFile.close();
    }
}
