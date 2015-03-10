package hse.zhizh.abfclient.Model;

/**
 * Created by E-Lev on 10.03.2015.
 */
public class BuildResponse {
    private int buildId;
    private String message;

    public BuildResponse(int buildId, String message) {
        this.buildId = buildId;
        this.message = message;
    }

    public int getBuildId() {
        return buildId;
    }

    public String getUrl() {
        return message;
    }

}
