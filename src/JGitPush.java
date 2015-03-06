import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Collection;

/**
 * Created by Administrator on 3/6/2015.
 */
public class JGitPush {

    Repository repository;
    UsernamePasswordCredentialsProvider auth;
    private StringBuffer resultMsg = new StringBuffer();
    public JGitPush(Repository repository) {
        this.repository = repository;
        String username = Settings.repo_username;
        String password = Settings.repo_password;
        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            auth = new UsernamePasswordCredentialsProvider(username, password);
        }
    }
    //push all - push all branches
    public boolean pushRepo(boolean pushAll) {
        Git git = repository.getGit();
        PushCommand pushCommand = git.push().setPushTags()
                //   .setProgressMonitor(new RepoCloneMonitor())
                .setRemote(repository.getRemoteURL());
        if (pushAll) {
            pushCommand.setPushAll();
        } else {
            RefSpec spec = new RefSpec(repository.getBranchName());
            pushCommand.setRefSpecs(spec);
        }

        pushCommand.setCredentialsProvider(auth);

        try {
            Iterable<PushResult> result = pushCommand.call();
            for (PushResult r : result) {
                Collection<RemoteRefUpdate> updates = r.getRemoteUpdates();
                for (RemoteRefUpdate update : updates) {
                    parseRemoteRefUpdate(update);
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //Method to represent push results
    private void parseRemoteRefUpdate(RemoteRefUpdate update) {
        String msg = null;
        switch (update.getStatus()) {
            case AWAITING_REPORT:
                msg = String
                        .format("[%s] Push process is awaiting update report from remote repository.\n",
                                update.getRemoteName());
                break;
            case NON_EXISTING:
                msg = String.format("[%s] Remote ref didn't exist.\n",
                        update.getRemoteName());
                break;
            case NOT_ATTEMPTED:
                msg = String
                        .format("[%s] Push process hasn't yet attempted to update this ref.\n",
                                update.getRemoteName());
                break;
            case OK:
                msg = String.format("[%s] Success push to remote ref.\n",
                        update.getRemoteName());
                break;
            case REJECTED_NODELETE:
                msg = String
                        .format("[%s] Remote ref update was rejected,"
                                        + " because remote side doesn't support/allow deleting refs.\n",
                                update.getRemoteName());
                break;
            case REJECTED_NONFASTFORWARD:
                msg = String.format("[%s] Remote ref update was rejected,"
                                + " as it would cause non fast-forward update.\n",
                        update.getRemoteName());
            case REJECTED_OTHER_REASON:
                String reason = update.getMessage();
                if (reason == null || reason.isEmpty()) {
                    msg = String.format(
                            "[%s] Remote ref update was rejected.\n",
                            update.getRemoteName());
                } else {
                    msg = String
                            .format("[%s] Remote ref update was rejected, because %s.\n",
                                    update.getRemoteName(), reason);
                }
                break;
            case REJECTED_REMOTE_CHANGED:
                msg = String
                        .format("[%s] Remote ref update was rejected,"
                                        + " because old object id on remote "
                                        + "repository wasn't the same as defined expected old object.\n",
                                update.getRemoteName());
                break;
            case UP_TO_DATE:
                msg = String.format("[%s] remote ref is up to date\n",
                        update.getRemoteName());
                break;
        }
        resultMsg.append(msg);
    }

    //Add file to tracking
    public boolean add_file_to_stage(File f) {
        Git git = repository.getGit();;

        try {
            git.add().addFilepattern(f.getAbsolutePath()).call();
        } catch (GitAPIException e) {
            return false;
        }
        return true;
    }
}
