package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by E-Lev on 07.03.2015.
 */
public class JGitInit {

    Repository repository;
    public JGitInit(Repository repository) {
        this.repository = repository;
    }

    // local repo init
    public boolean initRepo() {
        Git git;
        File f = repository.getDir();
        git = null;
        try {
            git = Git.open(f);
            repository.setGit(git);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
