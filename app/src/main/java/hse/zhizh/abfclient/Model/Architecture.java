package hse.zhizh.abfclient.Model;

/**
 * Created by EvgenyMac on 10.03.15.
 */
public class Architecture {
    private int id;
    private String name;

    public Architecture(int id, String name){
        this.id = id;
        this.name=name;
    }

    public int getId(){ return id; }
    public String getName() { return name; }
}
