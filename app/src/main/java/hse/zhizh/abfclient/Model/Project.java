package hse.zhizh.abfclient.Model;

/**
 * Created by E-Lev on 27.01.2015.
 */
public class Project {
    private int id;
    private String name;
    private String fullname;
    private String git_url;
    private String description;
    private int owner_id;

    public Project(int id, String name, String fullname, String git_url, String description, int owner_id) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.git_url = git_url;
        this.description = description;
        this.owner_id = owner_id;
    }

    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getGitUrl() { return git_url; }
    public int getOwnerId() { return owner_id; }

}
