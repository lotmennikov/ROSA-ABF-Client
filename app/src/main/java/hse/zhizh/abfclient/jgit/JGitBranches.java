package hse.zhizh.abfclient.jgit;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.*;

import hse.zhizh.abfclient.Model.Commit;
import hse.zhizh.abfclient.Model.Repository;

public class JGitBranches {

    private static final int COMMIT_TYPE_HEAD = 0;
    private static final int COMMIT_TYPE_TAG = 1;
    private static final int COMMIT_TYPE_TEMP = 2;
    private static final int COMMIT_TYPE_REMOTE = 3;

    Repository repository;

    public JGitBranches(Repository repository) {
        this.repository = repository;
    }

    //get all branches
    public String[] getBranches() {
        Set<String> branchSet = new HashSet<String>();
        List<String> branchList = new ArrayList<String>();
        try {
            List<Ref> localRefs = repository.getGit().branchList().call();
            for (Ref ref : localRefs) {
                branchSet.add(ref.getName());
                branchList.add(ref.getName());
            }
            List<Ref> remoteRefs = repository.getGit().branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE).call();
            for (Ref ref : remoteRefs) {
                String name = ref.getName();
                String localName = convertRemoteName(name);
                if (branchSet.contains(localName))
                    continue;
                branchList.add(name);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return branchList.toArray(new String[branchList.size()]);
    }

    public static String convertRemoteName(String remote) {
        String[] splits = remote.split("/");
        if (getCommitType(splits) != COMMIT_TYPE_REMOTE)
            return null;
        return String.format("refs/heads/%s", splits[3]);
    }

    public static int getCommitType(String[] splits) {
        if (splits.length == 4)
            return COMMIT_TYPE_REMOTE;
        if (splits.length != 3)
            return COMMIT_TYPE_TEMP;
        String type = splits[1];
        if ("tags".equals(type))
            return COMMIT_TYPE_TAG;
        return COMMIT_TYPE_HEAD;
    }

    //set current branch
    public boolean checkout(String branchName) {
        String[] splits = branchName.split("/");
        if (splits.length == 4) {
            try {
                repository.getGit().checkout().setCreateBranch(true).setName(splits[3])
                        .setStartPoint(branchName).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return false;
            }
            try {
                repository.getGit()
                        .branchCreate()
                        .setUpstreamMode(
                                CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                        .setStartPoint(branchName).setName(splits[3])
                        .setForce(true).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            try {
                repository.getGit().checkout().setName(branchName).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("Current branch is " + repository.getBranchName());
        return true;
    }

    //get all commits of current branch
    public  ArrayList<Commit> getCommits() {
        Git git = repository.getGit();
        RevWalk walk;
        ArrayList<Commit> commits = new ArrayList<Commit>();
        org.eclipse.jgit.lib.Repository repo = git.getRepository();
    /*    try {
            String s1 = repository.getBranch();
            String s2 = repository.getFullBranch();
            Set<String> s3 = repository.getRemoteNames();
            // String s4 = repository.getRemoteName(Constants.R_R);
            List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            StoredConfig config = repository.getConfig();
            Set<String> remotes = config.getSubsections("remote");
            String url = config.getString("remote", remotes.toArray()[0].toString(), "url");
            if (url != null) {
                System.out.println("Origin comes from " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }*/
        walk = new RevWalk(repo);
        ObjectId from = null;
        try {
            from = repo.resolve(repo.getFullBranch());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ObjectId to = null;
        try {
            to = repo.resolve("refs/remotes/origin/" + repo.getBranch());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            walk.markStart(walk.parseCommit(from));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            walk.markUninteresting(walk.parseCommit(to));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (Iterator<RevCommit> iterator = walk.iterator(); iterator.hasNext();) {
            RevCommit rev = iterator.next();
            commits.add(new Commit(rev.getFullMessage(), rev.getName(), rev.getCommitterIdent().getName(), rev.getCommitterIdent().getWhen(), false));
         /*   System.out.println(rev.getCommitterIdent().getName());
            System.out.println(rev.getFullMessage());
            Date d = rev.getCommitterIdent().getWhen();*/
        }
        walk.dispose();
        try {
            walk.markStart(walk.parseCommit(to));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (Iterator<RevCommit> iterator = walk.iterator(); iterator.hasNext();) {
            RevCommit rev = iterator.next();
            commits.add(new Commit(rev.getFullMessage(), rev.getName(), rev.getCommitterIdent().getName(), rev.getCommitterIdent().getWhen(), true));
          /*  System.out.println(rev.getCommitterIdent().getName());
            System.out.println(rev.getFullMessage());
            Date d = rev.getCommitterIdent().getWhen();*/
        }
        /*try {
            Iterable<RevCommit> commits = null;
            try {
                commits = git.log().call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

            for (RevCommit commit : commits) {
                System.out.println(commit.getAuthorIdent().getName());
                System.out.println(commit.getFullMessage());
            }
        } catch(Exception e) {

        }*/
        return commits;
    }
}
