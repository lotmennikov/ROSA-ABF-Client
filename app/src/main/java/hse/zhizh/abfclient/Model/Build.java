package hse.zhizh.abfclient.Model;

/**
 * Created by E-Lev on 27.01.2015.
 */
public class Build {

    private int buildId;
    private String message;

    public Build(int buildId,String message) {
        this.buildId = buildId;
        this.message = message;
    }

    public int getBuildId() { return buildId; }
    public String getUrl(){return message;}

}
