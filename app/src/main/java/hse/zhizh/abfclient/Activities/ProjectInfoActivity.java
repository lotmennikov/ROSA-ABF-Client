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
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.GitWrappers.GitBranch;
import hse.zhizh.abfclient.GitWrappers.GitCommand;
import hse.zhizh.abfclient.GitWrappers.GitSetBranch;
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

    GitBranch branchcom;

    String[] branches;
    AlertDialog branchDialog;

    private String[] tabLabels = new String[]{ "Builds", "Files", "Commits"};
    private ViewPager viewPager;
    private ActionBar actionBar;
    private ProjectPagerAdapter ppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

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
        getBranches();

        // текущая вкладка
        viewPager.setCurrentItem(1); // file list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_info, menu);

        getSupportActionBar().setTitle(project.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // список веток
    private void getBranches() {
        GitBranch brcom = new GitBranch(repo);
        if (brcom.execute())
            branches = brcom.result;
        else
            this.finish();
    }

    // TODO подправить окошко
    public void onBranchButtonClick(View v) {
        ArrayAdapter<String> branchesAdapter = new ArrayAdapter<String>(this, R.layout.dialog_menu_item, branches);
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Set branch");
        blder.setAdapter(branchesAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GitSetBranch setBranch = new GitSetBranch(repo, ProjectInfoActivity.this, branches[which]);
                setBranch.execute();
            }
        });
        branchDialog = blder.create();
        branchDialog.show();
    }

    // TODO новый коммит
    public void onCommitButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO pull repository
    public void onPullButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO push to repository
    public void onPushButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO reset repository
    public void onResetButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO create new build
    public void onNewBuildButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO сделать загрузку вкладки с содержимым
    private void initContentsTab() {

    }

    // TODO сделать загрузку вкладки со сборками
    private void initBuildsTab() {

    }

    // TODO сделать загрузку вкладки с коммитами
    private void initCommitsTab() {

    }

    // TODO Вставить ветки, гитовые команды
    @Override
    public void onCommandExecuted(int commandID, boolean success) {
        switch (commandID) {
/*            case JGitCommand.CLONE_COMMAND:
                if (success) {
                    project.init(); // set initialized to true
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
                break;
*/
            case GitCommand.COMMIT_COMMAND:
                break;
            case GitCommand.PULL_COMMAND:
                break;
            case GitCommand.PUSH_COMMAND:
                break;
            case GitCommand.SETBRANCH_COMMAND:
                if (success) {
//                    if (currentDir.exists()) {
//                        setFileList();
//                    } else {
//                        currentDir = repo.getDir();
//                        setFileList();
//                    }
                    Toast tst = Toast.makeText(this.getApplicationContext(), "new branch", Toast.LENGTH_LONG);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "branch set failed", Toast.LENGTH_LONG);
                    tst.show();
//                    this.finish();
                }
                break;
            default:
                break;
        }

    }

    // если вклалка не обрабатывает, можно закрывать
    @Override
    public void onBackPressed() {
        if (!((ProjectActivityEventListener)ppAdapter.getItem(viewPager.getCurrentItem())).onBackPressed())
            this.finish();
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
