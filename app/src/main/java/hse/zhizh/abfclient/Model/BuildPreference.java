package hse.zhizh.abfclient.Model;

import java.util.List;

/**
 * Created by E-Lev on 24.03.2015.
 */
public class BuildPreference {
    private String platform;
    private List<String> plRepos;
    private String architecture;
    private String repository;
    private String ref;
    private String updateType;

    public BuildPreference(String platform, List<String> plRepos, String architecture, String repository, String updateType, String ref) {
        this.platform = platform;
        this.plRepos = plRepos;
        this.architecture = architecture;
        this.repository = repository;
        this.updateType = updateType;
        this.ref = ref;
    }

    public String getPlatform() { return platform; }
    public List<String> getPlRepos() { return plRepos; }
    public String getArchitecture() {return architecture; }
    public String getRepository() { return repository; }
    public String getRef() { return ref; }
    public String getUpdateType() { return updateType; }

}
