package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.output.WriterOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.ABFQueries.ABFBuilds;
import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.GitWrappers.GitBranch;
import hse.zhizh.abfclient.GitWrappers.GitCommand;
import hse.zhizh.abfclient.GitWrappers.GitCommit;
import hse.zhizh.abfclient.GitWrappers.GitCommitList;
import hse.zhizh.abfclient.GitWrappers.GitDownloadAbf;
import hse.zhizh.abfclient.GitWrappers.GitGetAbfFiles;
import hse.zhizh.abfclient.GitWrappers.GitPull;
import hse.zhizh.abfclient.GitWrappers.GitPush;
import hse.zhizh.abfclient.GitWrappers.GitReset;
import hse.zhizh.abfclient.GitWrappers.GitSetBranch;
import hse.zhizh.abfclient.GitWrappers.GitStatus;
import hse.zhizh.abfclient.GitWrappers.GitUpload;
import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.Model.Build;
import hse.zhizh.abfclient.Model.Commit;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/**
 * Обзор проекта
 *  Содержимое
 *  Коммиты
 *  Сборки
 *  Команды
 * * Pull
 * * Commit
 * * Reset
 * * Push
 * * New file
 * * Add binary file
 * * Download .abf.yml files
 * * Create build
 */
public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener, ActionBar.TabListener {
// activity request code
    static final int REQUEST_FILENAME = 1;
    static final int REQUEST_NEWBUILD = 2;
    static final String ActivityTag = "ProjectInfoActivity";
// project info
    Project project;
    Repository repo;
// command
    GitCommand gitCommand;
    ABFQuery abfQuery;
// arrays
    String[] branches;
    Commit[] commits;
    Build[] builds;
// dialogs
    AlertDialog branchDialog;
    Dialog downloadAbfDialog;
    Dialog addBinaryDialog;
    Dialog newFileDialog;
    Dialog commitDialog;
    ProgressDialog progressDialog;
// interface
    private String[] tabLabels = new String[]{ "Commits", "Files", "Builds"};
    private ViewPager viewPager;
    private ActionBar actionBar;
    private ProjectPagerAdapter ppAdapter;
    private Button branchButton;

    private MenuItem refreshBuildsButton;
// dialog item
    private EditText addbin_fileedit;

    boolean refreshBuildMessage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        branchButton = (Button)findViewById(R.id.branchButton);
        viewPager = (ViewPager)findViewById(R.id.projectInfoPager);
        actionBar = getSupportActionBar();
        ppAdapter = new ProjectPagerAdapter(getSupportFragmentManager());
        progressDialog = new ProgressDialog(this);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "Task is not finished", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.setCancelable(false);

        viewPager.setAdapter(ppAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab : tabLabels)
            actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(this));

        // test repository
        // репозиторий уже инициализирован
        project = Settings.currentProject;
        repo = project.getRepo();


      //  getBranches();
        getCommits();
        getBuilds();

        // текущая вкладка
        viewPager.setCurrentItem(1); // file list

        // branchButton caption
        String branchname = project.getRepo().getBranchName();
        System.out.println("branch:" + branchname);
        String[] nameparts = branchname.split("/");
        branchButton.setText(nameparts[nameparts.length-1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_info, menu);
        refreshBuildsButton = menu.findItem(R.id.action_refreshbuilds);
        refreshBuildsButton.setVisible(false);
        refreshBuildsButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onRefreshBuildsClick(null);
                return true;
            }
        });

        getSupportActionBar().setTitle(project.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    // список веток
    private void getBranches() {
        GitBranch brcom = new GitBranch(repo);
        if (brcom.execute())
            branches = brcom.result;
        else
            this.finish();
    }

// **** НА ВКЛАДКИ ****

    // список коммитов
    private void getCommits() {
        GitCommitList gcomList = new GitCommitList(repo);
        if (gcomList.execute()) {
            commits = gcomList.result;
        } else {
            commits = null;
            Toast.makeText(this.getApplicationContext(), "No commits!", Toast.LENGTH_SHORT).show();
            //this.finish();
        }
        ppAdapter.refreshCommits(commits);
    }

    // список сборок
    private void getBuilds() {
        if (abfQuery == null) {
            abfQuery = new ABFBuilds(this, project.getId());
            abfQuery.execute();
            // waiting for response
        } else {
            Toast.makeText(this.getApplicationContext(), "Last query has not finished yet!", Toast.LENGTH_SHORT).show();
        }
    }

// **** КНОПОЧКИ ****
    // обработчик кнопочек меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
        case R.id.action_pull:
            onPullButtonClick(null);
            return true;
        case R.id.action_push:
            onPushButtonClick(null);
            return true;
        case R.id.action_commit:
            onCommitButtonClick(null);
            return true;
        case R.id.action_reset:
            onResetButtonClick(null);
            return true;
        case (android.R.id.home):
            this.finish();
            return true;
        case (R.id.action_newfile):
            onNewFileButtonClick(null);
            break;
        case (R.id.action_addbinary):
            onAddBinaryButtonClick(null);
            break;
        case (R.id.action_downloadbinary):
            onDownloadBinariesButtonClick(null);
            break;
        case (R.id.action_newbuild):
            onNewBuildButtonClick(null);
            break;
        case (R.id.action_refreshbuilds):
            getBuilds();
            break;
        default:
            break;
    }
    return super.onOptionsItemSelected(item);
}
    // Вывод диалога выбора ветки
    public void onBranchButtonClick(View v) {

        getBranches();
        ArrayAdapter<String> branchesAdapter = new ArrayAdapter<String>(this, R.layout.dialog_menu_item, branches);
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Set branch");
        blder.setAdapter(branchesAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gitCommand = new GitSetBranch(repo, ProjectInfoActivity.this, branches[which]);
                String[] nameparts = branches[which].split("/");
                branchButton.setText(nameparts[nameparts.length-1]);
                gitCommand.execute();
            }
        });
        branchDialog = blder.create();
        branchDialog.show();
    }

    // Show commit dialog
    public void onCommitButtonClick(View v) {
        commitDialog = new Dialog(this);
        commitDialog.setContentView(R.layout.dialog_commit);
        commitDialog.setTitle("Commit");

        final EditText commitName = (EditText)commitDialog.findViewById(R.id.commit_summary);
        final Button commitCommit = (Button)commitDialog.findViewById(R.id.commit_commit);
        final TextView statusView = (TextView)commitDialog.findViewById(R.id.commit_status);
        GitStatus statuscom = new GitStatus(repo);
        String status = "Status: \n";
        if (statuscom.execute()) {
            status += statuscom.result;
        }
        statusView.setText(status);

        commitCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = commitName.getText().toString();
                if (gitCommand == null) {
                    gitCommand = new GitCommit(repo, ProjectInfoActivity.this, summary);
                    gitCommand.execute();
                }
                commitDialog.dismiss();
            }
        });
        commitDialog.show();
    }

    // Pull
    public void onPullButtonClick(View v) {
        if (gitCommand == null) {
            gitCommand = new GitPull(repo, this);
            progressDialog.setTitle("Pulling...");
            progressDialog.show();
            gitCommand.execute();
        }
    }

    // Push
    public void onPushButtonClick(View v) {
        if (gitCommand == null) {
            gitCommand = new GitPush(repo, this);
            progressDialog.setTitle("Pushing...");
            progressDialog.show();
            gitCommand.execute();
        }
    }

    // Reset
    public void onResetButtonClick(View v) {
        GitReset resetCommand = new GitReset(repo);
        if (resetCommand.execute()) {
            ppAdapter.refreshContents();
            getCommits();
            Toast.makeText(getApplicationContext(), "Hard Reset", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Reset Failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Shows new file dialog
    public void onNewFileButtonClick(View v) {
        final File currentDir = ppAdapter.getCurrentDir();

        newFileDialog = new Dialog(this);
        newFileDialog.setContentView(R.layout.dialog_newfile);
        newFileDialog.setTitle("New text file");

        final EditText newf_filename = (EditText)newFileDialog.findViewById(R.id.newfile_filename);
        Button newf_create = (Button)newFileDialog.findViewById(R.id.newfile_create);
        newf_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String newFilename = newf_filename.getText().toString().trim();
                    if (!newFilename.equals("")) {
                        newFilename = currentDir.getAbsolutePath() + "/" +newFilename;
                        File newFile = new File(newFilename);
                        Log.d(Settings.TAG, ActivityTag + " File: " + newFilename);
                        if (!newFile.exists()) {
                            if (newFile.createNewFile()) {
                                Toast.makeText(getApplicationContext(), "New File", Toast.LENGTH_SHORT).show();
                            }
                        }
                        ppAdapter.refreshContents();
                        newFileDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to create new file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        newFileDialog.show();

    }

    // Add binary file dialog
    public void onAddBinaryButtonClick(View v) {
        if (gitCommand == null) {
            // настройка окошка
            addBinaryDialog = new Dialog(this);
            addBinaryDialog.setContentView(R.layout.dialog_abffileupload);
            addBinaryDialog.setTitle("Add binary file");

            addbin_fileedit = (EditText) addBinaryDialog.findViewById(R.id.addbin_filepath);
            final Button addbin_browse = (Button) addBinaryDialog.findViewById(R.id.addbin_browsebutton);
            final Button addbin_cancel = (Button) addBinaryDialog.findViewById(R.id.addbin_cancel);
            final Button addbin_upload = (Button) addBinaryDialog.findViewById(R.id.addbin_upload);

            // Browse button
            addbin_browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentFile = addbin_fileedit.getText().toString();

                    Intent filechooserIntent = new Intent(ProjectInfoActivity.this, FileChooserActivity.class);
                    filechooserIntent.putExtra("Filename", currentFile);

                    startActivityForResult(filechooserIntent, REQUEST_FILENAME);
                }
            });
            // Cancel button
            addbin_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBinaryDialog.dismiss();
                }
            });
            // Upload button
            addbin_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File binFile = new File(addbin_fileedit.getText().toString());
                    if (binFile.exists()) {
                        uploadToFileStore(binFile);
                    }
                    addBinaryDialog.dismiss();
                }
            });

            addBinaryDialog.show();
        } else Toast.makeText(getApplicationContext(), "Unfinished git request", Toast.LENGTH_SHORT).show();
    }

    // Starting upload task
    private void uploadToFileStore(File binFile) {
            gitCommand = new GitUpload(repo, this, binFile);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            gitCommand.execute();
    }

    public void onDownloadBinariesButtonClick(View v) {
        if (gitCommand == null) {
            showDownloadBinChooser();
        } else Toast.makeText(getApplicationContext(), "Unfinished git request", Toast.LENGTH_SHORT).show();
    }

    // Starts NewBuildActivity
    public void onNewBuildButtonClick(View v) {
        Toast.makeText(getApplicationContext(), "New Build", Toast.LENGTH_SHORT).show();
        Intent newbuildIntent = new Intent(ProjectInfoActivity.this, NewBuildActivity.class);
        startActivityForResult(newbuildIntent, REQUEST_NEWBUILD);
    }


    public void onRefreshBuildsClick(View v) {
        Log.d(Settings.TAG, "ProjectInfoActivity: Refresh Click");
        refreshBuildMessage = true;
        getBuilds();
    }

    // если вклалка не обрабатывает, можно закрывать
    @Override
    public void onBackPressed() {
        if (!((ProjectActivityEventListener)ppAdapter.getItem(viewPager.getCurrentItem())).onBackPressed())
            this.finish();
    }


// Команды с асинхронным выполнением
    @Override
    public void onCommandExecuted(int commandID, boolean success) {
        switch (commandID) {
//  -------- JGIT ------------
            case GitCommand.COMMIT_COMMAND:
                if (success) {
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Committed", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Commit Failed" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                }
                gitCommand = null;
                break;
            case GitCommand.PULL_COMMAND:
                if (success) {
                    ppAdapter.refreshContents();
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Pulled", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Pull Failed" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                }
                if (progressDialog.isShowing()) progressDialog.dismiss();
                gitCommand = null;
                break;
            case GitCommand.UPLOAD_COMMAND:
                if (success) {
                    String message = ((GitUpload)gitCommand).result;
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Failed to upload file" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                }
                if (progressDialog.isShowing()) progressDialog.dismiss();
                gitCommand = null;
                break;
            case GitCommand.PUSH_COMMAND:
                if (success) {
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Pushed", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Push Failed" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                }
                if (progressDialog.isShowing()) progressDialog.dismiss();
                gitCommand = null;

                break;
            case GitCommand.SETBRANCH_COMMAND:
                if (success) {
                    ppAdapter.refreshContents();
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Checkout", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Checkout failed" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                    this.finish();
                }
                gitCommand = null;
                break;
            case GitCommand.DOWNLOADABF_COMMAND:
                if (success) {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "All files were downloaded successfully", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    String message = "Download Failed" + ((gitCommand.errorMessage != null) ? ": " + gitCommand.errorMessage : "");
                    Toast tst = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
                    tst.show();
                }
                if (progressDialog.isShowing()) progressDialog.dismiss();
                gitCommand = null;
                break;
//  ---------- ABF -----------------
            case ABFQuery.BUILDS_QUERY:
                if (success) {
                    builds = ((ABFBuilds)abfQuery).builds;
                    if (refreshBuildMessage) {
                        refreshBuildMessage = false;
                        Toast.makeText(this.getApplicationContext(), "Builds refreshed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    builds = null;
                    Toast.makeText(this.getApplicationContext(), "No builds!", Toast.LENGTH_SHORT).show();
                }
                abfQuery = null;
                ppAdapter.refreshBuilds(builds);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        if (abfQuery != null)
            abfQuery.cancel(true);
        if (gitCommand != null)
            gitCommand.cancel(false);
        super.onDestroy();
    }


    public void showDownloadBinChooser() {
        final GitGetAbfFiles abfFiles = new GitGetAbfFiles(repo);
        if (abfFiles.execute()) {
            final List<AbfFile> abfFileList = abfFiles.result;

            // настройка окошка
            downloadAbfDialog = new Dialog(this);
            downloadAbfDialog.setContentView(R.layout.dialog_listabfyml);
            downloadAbfDialog.setTitle("Download files");

            // настройка списка
            final AbfFileListAdapter abfAdapter = new AbfFileListAdapter(this,
                    R.layout.item_abfymllist, abfFileList);
            ListView listView = (ListView)downloadAbfDialog.findViewById(R.id.abffiles_list);
            // настройка кнопки
            View footer = this.getLayoutInflater().inflate(R.layout.item_abfllistfooter, null);
            Button downloadButton = (Button)footer.findViewById(R.id.abffiles_downloadbutton);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean[] checkList = abfAdapter.getChecklist();
                    startDownloadAbf(abfFileList, checkList);
                    downloadAbfDialog.dismiss();

                }
            });

            listView.addFooterView(footer);
            listView.setAdapter(abfAdapter);


            // показ
            downloadAbfDialog.show();
        }

    }


    private void startDownloadAbf(List<AbfFile> abfFiles, boolean[] checkList) {
        List<AbfFile> selectedAbf = new ArrayList<AbfFile>();
        for (int i = 0; i < checkList.length; ++i) {
            if (checkList[i]) selectedAbf.add(abfFiles.get(i));
        }
        gitCommand = new GitDownloadAbf(repo, this, selectedAbf);
        progressDialog.setTitle("Downloading files...");
        progressDialog.show();
        gitCommand.execute();
    }


    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        if (res_intent != null) {
            if (rescode == RESULT_OK && rcode == REQUEST_FILENAME) {
                if (addbin_fileedit != null) {
                    String filename = res_intent.getStringExtra("Filename");
                    addbin_fileedit.setText(filename);
                }
            }
            if (rescode == RESULT_OK && rcode == REQUEST_NEWBUILD) {
                getBuilds();
            }
        }
    }
//  -------- tabs ---------

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
        if (refreshBuildsButton != null)
            refreshBuildsButton.setVisible(tab.getPosition() == 2);
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
