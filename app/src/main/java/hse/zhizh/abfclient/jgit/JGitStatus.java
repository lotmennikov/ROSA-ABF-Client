package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;

import java.util.Set;

import hse.zhizh.abfclient.Model.Repository;

/**
 * Created by Administrator on 3/19/2015.
 */
public class JGitStatus {

    Repository repository;

    private StringBuffer mResult = new StringBuffer();

    public JGitStatus(Repository repository) {
        this.repository = repository;
    }

    public StringBuffer getStatusResult() {
        return mResult;
    }

    public boolean getStatus() {
        try {
            org.eclipse.jgit.api.Status status = repository.getGit().status().call();
            convertStatus(status);
        } catch (NoWorkTreeException e) {
            e.printStackTrace();
            return false;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void convertStatus(org.eclipse.jgit.api.Status status) {
        if (!status.hasUncommittedChanges() && status.isClean()) {
            mResult.append("nothing to commit, working directory clean");
            return;
        }
        // TODO if working dir not clean
        convertStatusSet("Added files:", status.getAdded());
        convertStatusSet("Changed files:", status.getChanged());
        convertStatusSet("Removed files:", status.getRemoved());
        convertStatusSet("Missing files:", status.getMissing());
        convertStatusSet("Modified files:", status.getModified());
        convertStatusSet("Conflicting files:", status.getConflicting());
        convertStatusSet("Untracked files:", status.getUntracked());

    }

    private void convertStatusSet(String type, Set<String> status) {
        if (status.isEmpty())
            return;
        mResult.append(type);
        mResult.append("\n\n");
        for (String s : status) {
            mResult.append('\t');
            mResult.append(s);
            mResult.append('\n');
        }
        mResult.append("\n");
    }
}
