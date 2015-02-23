package hse.zhizh.abfclient.Model;

/**
 * Created by E-Lev on 23.02.2015.
 */
public class Commit {

    private String name;
    private String hash;
    private String date;

    public Commit(String name, String hash, String date) {
        this.name = name;
        this.hash = hash;
        this.date = date;
    }

}
