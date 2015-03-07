package hse.zhizh.abfclient.Model;

import hse.zhizh.abfclient.common.Settings;

/**
 * Created by E-Lev on 27.01.2015.
 */
public class Project {

// database fields
    private int id;
    private String name;
    private String fullname;
    private String git_url;
    private String description;
    private int owner_id;
    private boolean local; // repository is on the device

// non-database
    private Repository repository; // repository object
    private boolean initialized;   // repository initialized\

    public Project(int id, String name, String fullname, String git_url, String description, int owner_id) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.git_url = git_url;
        this.description = description;
        this.owner_id = owner_id;

        local = false;
        initialized = false;
        repository = null;
    }

    // called only in DBHelper
    public void setLocal(boolean local) {
        this.local = local;
    }

    // Creating repository object. WITHOUT INITIALIZATION!
    public void createRepo() {
        repository = new Repository(Settings.appContext, this.name, this.git_url);
    }

    // must be called only after successful JGitInit or JGitClone
    public void init() {
        initialized = true;
    }

// Getters
    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getGitUrl() { return git_url; }
    public int getOwnerId() { return owner_id; }
    public Repository getRepo() { return repository; }

    public boolean isLocal() { return local; }
    public boolean isInitialized()   { return initialized;   }
}
