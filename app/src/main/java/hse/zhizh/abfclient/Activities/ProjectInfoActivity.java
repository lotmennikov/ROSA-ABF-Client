package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.output.WriterOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import hse.zhizh.abfclient.ABFQueries.ABFBuilds;
import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.GitWrappers.GitBranch;
import hse.zhizh.abfclient.GitWrappers.GitCommand;
import hse.zhizh.abfclient.GitWrappers.GitCommit;
import hse.zhizh.abfclient.GitWrappers.GitCommitList;
import hse.zhizh.abfclient.GitWrappers.GitPull;
import hse.zhizh.abfclient.GitWrappers.GitPush;
import hse.zhizh.abfclient.GitWrappers.GitSetBranch;
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
 *
 * TODO сделать вкладки
 */
public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener, ActionBar.TabListener {

    Project project;
    Repository repo;

    GitCommand gitCommand;
    ABFQuery abfQuery;


    String[] branches;
    Commit[] commits;
    Build[] builds;

    AlertDialog branchDialog;

    private String[] tabLabels = new String[]{ "Commits", "Files", "Builds"};
    private ViewPager viewPager;
    private ActionBar actionBar;
    private ProjectPagerAdapter ppAdapter;
    private Button branchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        branchButton = (Button)findViewById(R.id.branchButton);
        viewPager = (ViewPager)findViewById(R.id.projectInfoPager);
        actionBar = getSupportActionBar();
        ppAdapter = new ProjectPagerAdapter(getSupportFragmentManager());

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
        case R.id.action_settings:
            return true;
        case (android.R.id.home):
            this.finish();
            return true;
        case (R.id.action_newfile):
            onNewFileButtonClick(null);
            break;
        case (R.id.action_addbinary):
            // TODO
            break;
        case (R.id.action_newbuild):
            // TODO
            onNewBuildButtonClick(null);
            break;
        default:
            break;
    }
    return super.onOptionsItemSelected(item);
}

    // TODO подправить окошко
    public void onBranchButtonClick(View v) {

        getBranches();

        ArrayAdapter<String> branchesAdapter = new ArrayAdapter<String>(this, R.layout.dialog_menu_item, branches);
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Set branch");
        blder.setAdapter(branchesAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GitSetBranch setBranch = new GitSetBranch(repo, ProjectInfoActivity.this, branches[which]);
                String[] nameparts = branches[which].split("/");
                branchButton.setText(nameparts[nameparts.length-1]);
                setBranch.execute();
            }
        });
        branchDialog = blder.create();
        branchDialog.show();
    }

    // TODO check, сделать окошко
    public void onCommitButtonClick(View v) {
        if (gitCommand == null) {
            gitCommand = new GitCommit(repo, this, "ABF client commit");
            gitCommand.execute();
        }
    }

    // TODO check
    public void onPullButtonClick(View v) {
        if (gitCommand == null) {
            gitCommand = new GitPull(repo, this);
            gitCommand.execute();
        }
    }

    // TODO check
    public void onPushButtonClick(View v) {
        if (gitCommand == null) {
            gitCommand = new GitPush(repo, this);
            gitCommand.execute();
        }
    }

    // TODO reset repository
    public void onResetButtonClick(View v) {
        Toast.makeText(getApplicationContext(), "No function call", Toast.LENGTH_SHORT).show();
    }

    // TODO add dialog
    public void onNewFileButtonClick(View v) {
        File newfile = new File(repo.getDir() + "/file.txt");
        try {
            PrintWriter pw = new PrintWriter(newfile);
            pw.append("\ntime:" + System.currentTimeMillis());
            pw.close();
            Toast.makeText(getApplicationContext(), "file created", Toast.LENGTH_SHORT).show();
            ppAdapter.refreshContents();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "file was not created", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO add dialog, file chooser, jgit call
    public void onAddBinaryButtonClick(View v) {
        Toast.makeText(getApplicationContext(), "Add Binary Click", Toast.LENGTH_SHORT).show();
    }

    // TODO check
    public void onNewBuildButtonClick(View v) {
        Toast.makeText(getApplicationContext(), "New Build", Toast.LENGTH_SHORT).show();
        Intent newbuildIntent = new Intent(ProjectInfoActivity.this, NewBuildActivity.class);
        startActivity(newbuildIntent);
    }

    // если вклалка не обрабатывает, можно закрывать
    @Override
    public void onBackPressed() {
        if (!((ProjectActivityEventListener)ppAdapter.getItem(viewPager.getCurrentItem())).onBackPressed())
            this.finish();
    }


// Команды с асинхронным выполнением
    // TODO Вставить ветки, гитовые команды
    @Override
    public void onCommandExecuted(int commandID, boolean success) {
        switch (commandID) {
// -------- JGIT ------------
            case GitCommand.COMMIT_COMMAND:
                if (success) {
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Commit", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Commit Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
                gitCommand = null;
                break;
            case GitCommand.PULL_COMMAND:
                if (success) {
                    ppAdapter.refreshContents();
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Pull", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Pull Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
                gitCommand = null;
                break;
            case GitCommand.PUSH_COMMAND:
                if (success) {
                    getCommits();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Pushed", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Push Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
                gitCommand = null;
                break;
            case GitCommand.SETBRANCH_COMMAND:
                if (success) {
                    ppAdapter.refreshContents();
                    getCommits();
                    //getBuilds();
                    Toast tst = Toast.makeText(this.getApplicationContext(), "new branch", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "branch set failed", Toast.LENGTH_SHORT);
                    tst.show();
                    this.finish();
                }
                gitCommand = null;
                break;
// ---------- ABF -----------------
            case ABFQuery.BUILDS_QUERY:
                if (success) {
                    builds = ((ABFBuilds)abfQuery).builds;
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

// -------- tabs ---------

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
