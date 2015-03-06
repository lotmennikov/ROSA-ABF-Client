
import org.eclipse.jgit.api.PullCommand;

import java.io.*;
import java.util.ArrayList;


// Testing JGit commands
public class Test {


    public static void main(String[] args) {
        XTrustProvider.install();
        Repository r = new Repository();
        JGitClone JGitCloneCommand = new JGitClone(r);
        JGitBranches JGitBranchesCommand = new JGitBranches(r);
        JGitPull pullCommand = new JGitPull(r);
        Upload_abf_yml upload__command = new Upload_abf_yml(r);
        Download_abf_yml download_command = new Download_abf_yml(r);
        JGitCloneCommand.cloneRepo();
        String[] arr = JGitBranchesCommand.getBranches();
        for (String s : arr) {
            System.out.println(s);
        }
        // Clone.checkout(r, arr[2]);
        //  boolean c = commitChanges(r, "test commit message2", true);
        ArrayList<Commit> commits = JGitBranchesCommand.getCommits();
        upload__command.upload_abf_yml(new File("C:/test10.txt"));
        download_command.download_abf_yml();
        boolean b = pullCommand.pullRepo();
        //    boolean d = pushRepo(r, true);
    }
}

  /*  public static int getCommitType(String[] splits) {
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

    public static String[] getBranches(Repository reposit) {
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

   *//* public static String[] getBranches2() {
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
    }*//*

    //TODO
    //Not tested!!!
    public boolean reset(Repository reposit) {
        try {
            reposit.git.reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (Exception e) {
            return false;
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    public static void checkout(Repository reposit, String branchName) {
        try {
            reposit.git.checkout().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        //TODO
     *//*   Ref ref = git.checkout().
                setCreateBranch(true).
                setName("branchName").
                setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
                setStartPoint("origin/" + branchName).
                call();*//*
    }

    public static Git getGit(Repository reposit) {
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

    public static boolean pullRepo(Repository reposit) {
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

    public static boolean commitChanges(Repository reposit, String commitMessage, boolean stageAll) {
        Git git;
        try {
            git = getGit(reposit);
        } catch (Exception e) {
            return false;
        }
        AddCommand add_all = git.add().addFilepattern("."); // add all files to stage
        CommitCommand cc = git.commit().setAll(stageAll).setMessage(commitMessage);

        try {
            add_all.call();
            cc.call();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static boolean pushRepo(Repository reposit, boolean pushAll) {
        Git git;
        try {
            git = getGit(reposit);
        } catch (Exception e) {
            return false;
        }
        PushCommand pushCommand = git.push().setPushTags()
                .setProgressMonitor(new RepoCloneMonitor())
                .setRemote(reposit.getRemoteURL());
        if (pushAll) {
            pushCommand.setPushAll();
        } else {
            RefSpec spec = new RefSpec(reposit.getBranchName());
            pushCommand.setRefSpecs(spec);
        }

        String username = reposit.getUsername();
        String password = reposit.getPassword();
        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(
                    username, password);
            pushCommand.setCredentialsProvider(auth);
        }

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

    private static void parseRemoteRefUpdate(RemoteRefUpdate update) {
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

    public static boolean getCommits(Repository reposit) {
        Git git;
        RevWalk walk;
        try {
            git = getGit(reposit);
        } catch (Exception e) {
            return false;
        }
        org.eclipse.jgit.lib.Repository repository = git.getRepository();
        try {
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
            int k = 5;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        walk = new RevWalk(repository);
        ObjectId from = null;
        try {
            from = repository.resolve(repository.getFullBranch());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ObjectId to = null;
        try {
            to = repository.resolve("refs/remotes/origin/" + repository.getBranch());
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
            System.out.println(rev.getAuthorIdent().getName());
            System.out.println(rev.getFullMessage());
        }
        walk.dispose();
        try {
            walk.markStart(walk.parseCommit(to));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (Iterator<RevCommit> iterator = walk.iterator(); iterator.hasNext();) {
            RevCommit rev = iterator.next();
            System.out.println(rev.getAuthorIdent().getName());
            System.out.println(rev.getFullMessage());
        }
        try {
            Iterable<RevCommit> commits = null;
            try {
                commits = git.log().call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

            for (RevCommit commit : commits) {
                System.out.println(commit.getAuthorIdent().getName());
                System.out.println(commit.getFullMessage());
              *//*  String auth_name = commit.getAuthorIdent().getName();
                String hash = commit.getName();
                int date = commit.getCommitTime();*//*
            }
        } catch(Exception e) {

        }
        return true;
    }

    // Uses cloneCommand to clone repository
    public static boolean cloneRepo(Repository reposit) {
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

    public static boolean add_file_to_stage(Repository reposit, File f) {
        Git git;
        try {
            git = getGit(reposit);
        } catch (Exception e) {
           return false;
        }
        try {
            git.add().addFilepattern(f.getAbsolutePath()).call();
        } catch (GitAPIException e) {
            return false;
        }
        return true;
    }

    public static void upload_abf_yml(Repository reposit, File fileToUpload) {
     *//*   String urlToConnect = "http://file-store.rosalinux.ru/api/v1/upload";
        File fileToUpload = new File("C:/test2.txt");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

        URLConnection connection = null;
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("creepycheese", "ewqforce1".toCharArray());
            }
        });
        try {
            connection = new URL(urlToConnect).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true); // This sets request method to POST.
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        PrintWriter writer = null;
        try {
            try {
                writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"test2.txt\"");
            writer.println( "Content-Type: "  + URLConnection.guessContentTypeFromName("test2.txt"));
            writer.println("Content-Transfer-Encoding: binary");
            writer.println();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                for (String line; (line = reader.readLine()) != null;) {
                    writer.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            }
            writer.println("--" + boundary + "--");
        } finally {
            if (writer != null) writer.close();
        }


// Connection is lazily executed whenever you request any status.
        int responseCode = 0;
        try {
            responseCode = ((HttpURLConnection) connection).getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(responseCode); // Should be 200*//*

        CloseableHttpClient httpclient = HttpClients.createDefault();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", new FileBody(fileToUpload));
        HttpPost request = new HttpPost("http://file-store.rosalinux.ru/api/v1/upload");
        request.setEntity(builder.build());
        String encoding = new Base64().encodeAsString(new String("creepycheese:ewqforce1").getBytes());
        request.setHeader("Authorization", encoding);
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity2.getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject json = new JSONObject(result.toString());
            String hash = null;
            if (response2.getStatusLine().getStatusCode() == 422) {
                System.out.println("This file has already been uploaded!");
                hash = ((JSONArray)json.get("sha1_hash")).getString(0);
                hash = hash.substring(1, hash.indexOf('\'', 1));
            } else if (response2.getStatusLine().getStatusCode() == 201) {
                System.out.println("File uploaded successfully!");
                hash = json.getString("sha1_hash");
                File abf_yml_file = new File(reposit.getDir().toString() +"/.abf.yml");
                if (!abf_yml_file.exists()) {
                    abf_yml_file.getParentFile().mkdirs();
                    abf_yml_file.createNewFile();
                }
                FileWriter fw = new FileWriter (abf_yml_file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                if (abf_yml_file.length() == 0) {
                    bw.write("sources:");
                    bw.newLine();
                }
                bw.write("  \"" + fileToUpload.getName() + "\": " + hash);
                bw.close();
            } else {
                System.out.println("Unknown response code!");
            }
            EntityUtils.consume(entity2);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void download_abf_yml(Repository reposit) {
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
               *//* } else if(line.contains(".tar.xz")) {
                    XZInputStream xzIn = new XZInputStream(input);
                    decompress_tar(xzIn, reposit);
                    xzIn.close();*//*
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

    private static void decompress_tar(InputStream stream, Repository reposit) throws IOException {
        TarArchiveInputStream myTarFile = new TarArchiveInputStream(stream);
        TarArchiveEntry entry;
        String individualFiles;
        int offset;
        FileOutputStream outputFile;
        *//* Create a loop to read every single entry in TAR file *//*
        while (( entry = myTarFile.getNextTarEntry()) != null) {
             *//* Get the name of the file *//*
            individualFiles = reposit.getDir().toString() + "/abf.yml files/" + entry.getName();
            *//* Get Size of the file and create a byte array for the size *//*
            if (entry.isDirectory()) {
                File outputDir = new File(individualFiles);
                outputDir.mkdirs();
            } else {
                byte[] content = new byte[(int) entry.getSize()];
                offset = 0;
                *//* Read file from the archive into byte array *//*
                myTarFile.read(content, offset, content.length - offset);
                *//* Define OutputStream for writing the file *//*
                outputFile = new FileOutputStream(new File(individualFiles));
                outputFile.write(content, 0, content.length);
                *//* Close Output Stream *//*
                outputFile.close();
            }
        }
        *//* Close TarAchiveInputStream *//*
        myTarFile.close();
    }

    // Simple repository object
    static class Repository {

        // Clone to directory
        public File getDir() {
            return new File("C:/TestGit/");
        }

        public Git git;

        public String getBranchName(){
            try {
                return git.getRepository().getFullBranch();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
*/