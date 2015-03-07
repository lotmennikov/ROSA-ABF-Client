package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.GitWrappers.GitBranch;
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
public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener {

    Project project;
    Repository repo;

    GitBranch branchcom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        TextView textView = (TextView)findViewById(R.id.projectnameLabel);

        // test repository
        project = Settings.currentProject;
        repo = project.getRepo();

        textView.setText(project.getFullname());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_info, menu);

        getSupportActionBar().setTitle(project.getName());

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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    // TODO list commits
    public void onCommitsButtonClick(View v) {
        if (project.isInitialized()) {

        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // list file structure
    // TODO вделать в вкладку
    public void onContentsButtonClick(View v) {
        if (project.isInitialized()) {
            Intent projectcontent_intent = new Intent(ProjectInfoActivity.this, ProjectContentsActivity.class);
            startActivity(projectcontent_intent);
        } else {
            Toast.makeText(getApplicationContext(), "Not Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO list builds
    public void onBuildsButtonClick(View v) {
        Intent builds_intent = new Intent(ProjectInfoActivity.this, BuildsActivity.class);
        startActivity(builds_intent);
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
/*        switch (commandID) {
            case JGitCommand.CLONE_COMMAND:
                if (success) {
                    project.init(); // set initialized to true
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
                break;
            default:
                break;
        }
    */
    }

}
