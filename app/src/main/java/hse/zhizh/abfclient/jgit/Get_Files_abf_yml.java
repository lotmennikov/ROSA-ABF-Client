package hse.zhizh.abfclient.jgit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/9/2015.
 */
public class Get_Files_abf_yml {
    Repository repository;
    public Get_Files_abf_yml(Repository repository) {
        this.repository = repository;
    }
    public List<AbfFile> getFiles() {
        List<AbfFile> files = new ArrayList<AbfFile>();
        File abf_yml_file = new File(repository.getDir().toString() +"/.abf.yml");
        if (!abf_yml_file.exists()) {
            System.out.println("No .abf.yml file found!");
            return files;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(abf_yml_file));
            int flag = 0;
            String line = br.readLine();
            while (line != null) {
                if (line.contains("removed_sources:"))
                    flag = 1;
                else if (line.contains("sources:"))
                    flag = 2;
                else if (flag == 1) {
                    String name = null;
                    String hash = null;
                    if (line.indexOf("\"") != -1) {
                        name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                        hash = line.substring(line.indexOf(": ") + 2);
                    } else {
                        name = line.substring(line.indexOf(" ") + 2, line.indexOf(": "));
                        hash = line.substring(line.indexOf(": ") + 2);
                    }
                    files.add(new AbfFile(name, hash, true));
                } else if (flag == 2) {
                    String name = null;
                    String hash = null;
                    if (line.indexOf("\"") != -1) {
                        name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                        hash = line.substring(line.indexOf(": ") + 2);
                    } else {
                        name = line.substring(line.indexOf(" ") + 2, line.indexOf(": "));
                        hash = line.substring(line.indexOf(": ") + 2);
                    }
                    files.add(new AbfFile(name, hash, false));
                } else flag = 0;
                line = br.readLine();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}
