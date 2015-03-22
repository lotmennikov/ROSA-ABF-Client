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
    private ProjectRepo[] ProjectRepositories; //Репозитории для проекта(необходимо для сборок)
    private ProjectRef[] ProjectRefs;
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

    public Project(int id, String name, String fullname, String git_url, String description, int owner_id, boolean islocal) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.git_url = git_url;
        this.description = description;
        this.owner_id = owner_id;
        this.local = islocal;
        initialized = false;
        repository = null;
    }

    public void setProjectRepositories(ProjectRepo[] rep){
        ProjectRepositories = rep;
    }

    public ProjectRepo[] getProjectRepositories(){
        return ProjectRepositories;
    }

    public void setProjectRefs(ProjectRef[] rep){
        ProjectRefs = rep;
    }

    public ProjectRef[] getProjectRefs(){
        return ProjectRefs;
    }

    // called only in DBHelper
    public void setLocal(boolean local) {
        this.local = local;
    }

    // Creating repository object. WITHOUT INITIALIZATION!
    public void createRepo() {
        repository = new Repository(this.name, this.fullname, this.git_url);
    }

    public void eraseRepo() {
        if (this.repository == null) {
            createRepo();
        }
        this.repository.Clear();
        this.repository = null;
        initialized = false;
        local = false;
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
