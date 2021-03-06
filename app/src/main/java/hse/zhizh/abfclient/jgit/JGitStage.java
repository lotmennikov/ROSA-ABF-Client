package hse.zhizh.abfclient.jgit;


import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;

import java.io.File;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by EvgenyMac on 22.03.15.
 */
public class JGitStage {

    Repository repository;
    public String errorMessage;

    public JGitStage(Repository repository) {
        this.repository = repository;
    }

    public boolean add_file_to_stage(File f) {

        Git git = repository.getGit();

        try {
            git.add().addFilepattern(getRelativePath(f)).call();
        } catch (GitAPIException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean stage_all() {
        Git git = repository.getGit();
        AddCommand add_all = git.add().addFilepattern("."); // add all files to stage
        try {
            add_all.call();
        } catch (GitAPIException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean unstage(File f) {
        Git git = repository.getGit();
        try {
            git.reset().setRef(Constants.HEAD).addPath(getRelativePath(f)).call();
        } catch (GitAPIException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getRelativePath(File f) {
        File base = repository.getDir();
        String relative = base.toURI().relativize(f.toURI()).getPath();
        return relative;
    }
}
