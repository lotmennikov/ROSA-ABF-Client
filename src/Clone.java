

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.tukaani.xz.XZInputStream;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.zip.GZIPInputStream;

// Testing JGit clone command
public class Clone {
    public static final int COMMIT_TYPE_HEAD = 0;
    public static final int COMMIT_TYPE_TAG = 1;
    public static final int COMMIT_TYPE_TEMP = 2;
    public static final int COMMIT_TYPE_REMOTE = 3;

   // static Git g;
    static Reposit r = new Reposit();
    public static void main(String[] args)  {
        XTrustProvider.install();
        Clone.cloneRepo(r);
        String[] arr = Clone.getBranches(r);
       // String[] arr2 = Clone.getBranches2();
        for (String s : arr) {
            System.out.println(s);
        }
        System.out.println();
        for (String s : arr) {
            System.out.println(s);
        }
       // Clone.checkout(r, arr[2]);
        r.git = getGit(r);
        boolean b = pullRepo(r);
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

    public static String convertRemoteName(String remote) {
        String[] splits = remote.split("/");
        if (getCommitType(splits) != COMMIT_TYPE_REMOTE)
            return null;
        return String.format("refs/heads/%s", splits[3]);
    }

    public static String[] getBranches(Reposit reposit) {
        try {
            Set<String> branchSet = new HashSet<String>();
            List<String> branchList = new ArrayList<String>();
            List<Ref> localRefs = reposit.git.branchList().call();
            for (Ref ref : localRefs) {
                branchSet.add(ref.getName());
                branchList.add(ref.getName());
            }
            List<Ref> remoteRefs = reposit.git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE).call();
            for (Ref ref : remoteRefs) {
                String name = ref.getName();
                String localName = convertRemoteName(name);
                if (branchSet.contains(localName))
                    continue;
                branchList.add(name);
            }
            return branchList.toArray(new String[0]);
        } catch (GitAPIException e) {
        }
        return new String[0];
    }

   /* public static String[] getBranches2() {
        try {
            List<String> branchList = new ArrayList<String>();
            List<Ref> refs = .branchList()
                    .setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : refs) {
                String name = ref.getName();
                branchList.add(name);
            }
            return branchList.toArray(new String[0]);
        } catch (GitAPIException e) {
        }
        return new String[0];
    }*/

    public static void checkout(Reposit reposit, String branchName) {
        try {
            reposit.git.checkout().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        //TODO
     /*   Ref ref = git.checkout().
                setCreateBranch(true).
                setName("branchName").
                setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                setStartPoint("origin/" + branchName).
                call();*/
    }

    public static Git getGit(Reposit reposit) {
        if (reposit.git != null)
            return reposit.git;
        File f = r.getDir();
        try {
            reposit.git = Git.open(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reposit.git;
    }

    public static boolean pullRepo(Reposit reposit) {
        Git git;
        try {
            git = getGit(reposit);
        } catch (Exception e) {
            return false;
        }
        PullCommand pullCommand = git.pull()
                .setProgressMonitor(new RepoCloneMonitor());

               //TODO
               // .setTransportConfigCallback(new SgitTransportCallback());

        String username = reposit.getUsername();
        String password = reposit.getPassword();
        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(
                    username, password);
            pullCommand.setCredentialsProvider(auth);
        }
        try {
            pullCommand.call();
        } catch (TransportException e) {

            return false;
        } catch (Exception e) {
            return false;
        } catch (OutOfMemoryError e) {
            return false;
        } catch (Throwable e) {
            return false;
        }

        //TODO
       // reposit.updateLatestCommitInfo();

        return true;
    }
    // Uses cloneCommand to clone repository
    public static boolean cloneRepo(Reposit reposit) {
        System.err.println("Clone procedure begin...");

        File localRepo = reposit.getDir();

        // initializing cloneCommand
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(reposit.getRemoteURL()).setCloneAllBranches(true)
                .setProgressMonitor(new RepoCloneMonitor())
                .setDirectory(localRepo);


        String username = reposit.getUsername();
        String password = reposit.getPassword();
        Locale.setDefault(Locale.US);

        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(
                    username, password);
            cloneCommand.setCredentialsProvider(auth);
        }
        try {
            // execution of cloneCommand
            reposit.git = cloneCommand.call();
            System.err.println("Clone procedure ends with no exception...");
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
            return false;
        } catch (TransportException e) {
            e.printStackTrace();
            return false;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        } catch (JGitInternalException e) {
            e.printStackTrace();
            return false;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void download_abf_yml(Reposit reposit) {
        File abf_yml_file = new File(reposit.getDir().toString() +"/.abf.yml");
        if (abf_yml_file.exists()) {
            new File(reposit.getDir().toString() + "/abf.yml files").mkdirs();
        } else {
            System.out.println("No .abf.yml file found!");
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(reposit.getDir().toString() +"/.abf.yml"));
            br.readLine(); //sources:
            String line = br.readLine();
            while (line != null)
            {
                String hash = line.substring(line.indexOf(": ") + 2);
                URL url = new URL("http://file-store.rosalinux.ru/download/" + hash);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream input = urlConnection.getInputStream();
                if (line.contains(".tar.gz")) {
                    GZIPInputStream gzIn = new GZIPInputStream(input);
                    decompress_tar(gzIn, reposit);
                    gzIn.close();
                } else if (line.contains(".tar.bz2")) {
                    BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                    decompress_tar(bzIn, reposit);
                    bzIn.close();
                } else if(line.contains(".tar.xz")) {
                    XZInputStream xzIn = new XZInputStream(input);
                    decompress_tar(xzIn, reposit);
                    xzIn.close();
                } else if (line.contains(".tar")) {
                    decompress_tar(input, reposit);
                    input.close();
                } else if (line.contains(".bz2")) {
                    BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(input);
                    final byte[] buffer = new byte[4096];
                    FileOutputStream output = new FileOutputStream(
                            new File(reposit.getDir().toString() +
                                    "/abf.yml files/" + line.substring(line.indexOf('"') + 1, line.indexOf(".bz2"))));
                    int n = 0;
                    while (-1 != (n = bzIn.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    output.close();
                    bzIn.close();
                } else {
                    final byte[] buffer = new byte[4096];
                    FileOutputStream output = new FileOutputStream(
                            new File(reposit.getDir().toString() +
                                    "/abf.yml files/" + line.substring(line.indexOf('"') + 1, line.indexOf("\":"))));
                    int n = 0;
                    while (-1 != (n = input.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    output.close();
                    input.close();
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in file .abf.yml!");
        }

    }

    private static void decompress_tar(InputStream stream, Reposit reposit) throws IOException {
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(stream);
        TarArchiveEntry entry;
        String individualFiles;
        int offset;
        FileOutputStream outputFile;
        /* Create a loop to read every single entry in TAR file */
        while (( entry = myTarFile.getNextTarEntry()) != null) {
             /* Get the name of the file */
            individualFiles = reposit.getDir().toString() + "/abf.yml files/" + entry.getName();
            /* Get Size of the file and create a byte array for the size */
            if (entry.isDirectory()) {
                File outputDir = new File(individualFiles);
                outputDir.mkdirs();
            } else {
                byte[] content = new byte[(int) entry.getSize()];
                offset = 0;
                /* Read file from the archive into byte array */
                myTarFile.read(content, offset, content.length - offset);
                /* Define OutputStream for writing the file */
                outputFile = new FileOutputStream(new File(individualFiles));
                outputFile.write(content, 0, content.length);
                /* Close Output Stream */
                outputFile.close();
            }
        }
        /* Close TarAchiveInputStream */
        myTarFile.close();
    }

    // Simple repository object
    static class Reposit {

        // Clone to directory
        public File getDir() {
            return new File("C:/TestGit/");
        }

        public Git git;

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


    // Progress reporting interface implementation
    static class RepoCloneMonitor implements ProgressMonitor {

        private int mTotalWork;
        private int mWorkDone;
        private String mTitle;

        private void publishProgressInner() {
            String status = "";
            String percent = "";
            if (mTitle != null) {
                status = String.format(Locale.getDefault(), "%s ... ", mTitle);
                percent = "0%";
            }
            if (mTotalWork != 0) {
                int p = 100 * mWorkDone / mTotalWork;
                percent = String.format(Locale.getDefault(), "(%d%%)", p);
            }
           // mRepo.updateStatus(status + percent);
            System.out.println(status + percent);
        }

        @Override
        public void start(int totalTasks) {
            publishProgressInner();
        }

        @Override
        public void beginTask(String title, int totalWork) {
            mTotalWork = totalWork;
            mWorkDone = 0;
            mTitle = title;
            publishProgressInner();
        }

        @Override
        public void update(int i) {
            mWorkDone += i;
            publishProgressInner();
        }

        @Override
        public void endTask() {
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

    }

}
