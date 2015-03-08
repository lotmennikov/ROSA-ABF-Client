package hse.zhizh.abfclient.Model;

/**
 * Created by E-Lev on 27.01.2015.
 */
public class Build {

    private int buildId;
    private int projectId;
    private int statusCode;

    public Build(int buildId, int projectId, int statusCode) {
        this.buildId = buildId;
        this.projectId = projectId;
        this.statusCode = statusCode;
    }

    public int getBuildId() { return buildId; }
    public int getProjectId() { return projectId; }
    public int getStatusCode() { return statusCode; }
}
