package hse.zhizh.abfclient.jgit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/7/2015.
 */
public class Download_abf_yml {
    Repository repository;
    public String errorMessage;
    public Download_abf_yml(Repository repository) {
        this.repository = repository;
    }



    public boolean download_files(List<AbfFile> files) {
        File binFiles = repository.getBinDir();
        if (!binFiles.exists())
            binFiles.mkdirs();
        for (AbfFile file : files) {
            try {
                URL url = new URL("http://file-store.rosalinux.ru/download/" + file.getHash());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int fileLength = urlConnection.getContentLength();
                InputStream input = urlConnection.getInputStream();
                final byte[] buffer = new byte[4096];
                FileOutputStream output = new FileOutputStream(
                        new File(binFiles.toString() + "/" + file.getName()));
                int n = 0;
                long total = 0;
                int curr_percent = -1;
                while (-1 != (n = input.read(buffer))) {
                    total += n;
                    if (fileLength > 0) // only if total length is known
                    {
                        if (curr_percent != (total * 100 / fileLength))
                            System.out.println(total * 100 / fileLength);
                        curr_percent = (int)(total * 100 / fileLength);
                    output.write(buffer, 0, n);
                }
                }
                output.close();
                input.close();
            } catch (MalformedURLException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;
            } catch (FileNotFoundException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;
            } catch (ProtocolException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
