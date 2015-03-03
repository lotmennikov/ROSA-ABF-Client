import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;


public class Repository {

    private Git git = null;
    public String getBranchName(){
        try {
            return getGit().getRepository().getFullBranch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Git getGit() {
        if (git != null)
            return git;
        File f = getDir();
        Git git = null;
        try {
            git = Git.open(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public File getDir() {
        return new File("C:/TestGit/");
    }

    public String getRemoteURL() {
            return "https://abf.io/creepycheese/pvpgn.git";
        }

    public String getUsername() {
            return "creepycheese";
        }

    public String getPassword() {
            return "ewqforce1";
        }

    public void updateStatus(String s) {
            System.out.println(s);
        }
}
