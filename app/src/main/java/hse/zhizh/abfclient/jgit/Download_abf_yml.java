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
import java.util.zip.GZIPInputStream;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/7/2015.
 */
public class Download_abf_yml {
    Repository repository;

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
                InputStream input = urlConnection.getInputStream();
                final byte[] buffer = new byte[4096];
                FileOutputStream output = new FileOutputStream(
                        new File(binFiles.toString() + "/" + file.getName()));
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.close();
                input.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (ProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
