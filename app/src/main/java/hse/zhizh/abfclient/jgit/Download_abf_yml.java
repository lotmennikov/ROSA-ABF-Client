package hse.zhizh.abfclient.jgit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/7/2015.
 */
public class Download_abf_yml {

    Repository repository;
    public Download_abf_yml(Repository repository) {
        this.repository = repository;
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
