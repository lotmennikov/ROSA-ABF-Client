package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 10.03.15.
 */
public class Platform {
    private int id;
    private String name;
    private Repo[] repos;

    public Platform(int id, String message,Repo[] repos){
        this.id = id;
        this.name=message;
        this.repos = repos;
    }

    public String getMessage(){ return name; }
    public int getId(){ return id; }
    public Repo[] getRepos() { return repos;}
}
