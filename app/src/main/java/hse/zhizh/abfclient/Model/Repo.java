package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 10.03.15.
 */
public class Repo {
    private int id;
    private String name;

    public Repo(int id, String name){
        this.id = id;
        this.name=name;
    }

    public int getId(){ return id; }
    public String getName() { return name; }
}
