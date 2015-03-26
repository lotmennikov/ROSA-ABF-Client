package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 10.03.15.
 */
public class Platform {
    private int id;
    private String name;
    private PlatformRepo[] platformRepos;

    public Platform(int id, String message,PlatformRepo[] platformRepos){
        this.id = id;
        this.name=message;
        this.platformRepos = platformRepos;
    }

    public String getMessage(){ return name; }
    public int getId(){ return id; }
    public PlatformRepo[] getPlatformRepos() { return platformRepos;}
}
