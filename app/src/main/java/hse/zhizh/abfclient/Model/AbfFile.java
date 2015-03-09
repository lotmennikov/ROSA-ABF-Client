package hse.zhizh.abfclient.Model;

/**
 * Created by Administrator on 3/9/2015.
 */
public class AbfFile {
    private String name;
    private String hash;
    private boolean isRemoved;
    public AbfFile(String name, String hash, boolean isRemoved) {
        this.name = name;
        this.hash = hash;
        this.isRemoved = isRemoved;
    }
    public String getName() {
        return name;
    }
    public String getHash() {
        return hash;
    }
    public boolean isRemoved() {
        return isRemoved;
    }
}
