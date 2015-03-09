package hse.zhizh.abfclient.jgit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/10/2015.
 */
public class Decompress_abf_yml {
    Repository repository;
    public Decompress_abf_yml(Repository repository) {
        this.repository = repository;
    }

    public void decompress_file(AbfFile file) {
        decompress_file(new File(repository.getBinDir() + "/" + file.getName()));
    }

    public void decompress_file(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            if (file.getName().contains(".tar.gz")) {
                GZIPInputStream gzIn = new GZIPInputStream(input);
                decompress_tar(gzIn, file.getName().substring(0, file.getName().indexOf(".tar.gz")));
                gzIn.close();
            } else if (file.getName().contains(".tar.bz2")) {
                BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                decompress_tar(bzIn, file.getName().substring(0, file.getName().indexOf(".tar.bz2")));
                bzIn.close();
            } else if (file.getName().contains(".tar")) {
                decompress_tar(input, file.getName().substring(0, file.getName().indexOf(".tar")));
                input.close();
            } else if (file.getName().contains(".bz2")) {
                BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                final byte[] buffer = new byte[4096];
                FileOutputStream output = new FileOutputStream(
                        new File(repository.getBinDir().toString() + "/" +
                                file.getName().substring(0, file.getName().indexOf(".bz2"))));
                int n = 0;
                while (-1 != (n = bzIn.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.close();
                bzIn.close();
            } else {
                System.out.println("Unsupported decompression type!");
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decompress_tar(InputStream stream, String filename) throws IOException {
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(stream);
        TarArchiveEntry entry;
        String individualFiles;
        int offset;
        FileOutputStream outputFile;
        /* Create a loop to read every single entry in TAR file */
        while (( entry = myTarFile.getNextTarEntry()) != null) {
             /* Get the name of the file */
            individualFiles = repository.getBinDir().toString() + "/" +
                    filename + "/" + entry.getName();
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
