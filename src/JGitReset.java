import org.eclipse.jgit.api.ResetCommand;

/**
 * Created by Administrator on 3/6/2015.
 */
public class JGitReset {
    Repository repository;
    public JGitReset(Repository repository) {
        this.repository = repository;
    }
    //TODO
    //Not tested!!!
    public boolean reset() {
        try {
            repository.getGit().reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (Exception e) {
            return false;
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
