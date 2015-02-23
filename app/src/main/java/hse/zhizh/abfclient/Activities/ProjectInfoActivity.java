package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.JGitCommands.JGitBranch;
import hse.zhizh.abfclient.JGitCommands.JGitClone;
import hse.zhizh.abfclient.JGitCommands.JGitCommand;
import hse.zhizh.abfclient.JGitCommands.JGitInit;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener {

    Project project;
    Repository repo;

    JGitBranch branchcom;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO locally init repository
    public void onInitButtonClick(View v) {
        if (repo.git == null) {
            JGitInit initcom = new JGitInit(repo, this);
            initcom.execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "Already initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // Тест клонирования объекта
    public void onCloneButtonClick(View v) {
        if (repo.git == null) {
            JGitClone clonecom = new JGitClone(repo, this);
            clonecom.execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "Already cloned", Toast.LENGTH_LONG).show();
        }
    }

    // TODO новый коммит
    public void onCommitButtonClick(View v) {


    }

    // TODO pull repository
    public void onPullButtonClick(View v) {

    }

    // TODO push to repository
    public void onPushButtonClick(View v) {
        if (project.isInitialized()) {
        }
    }


    // TODO reset repository
    public void onResetButtonClick(View v) {
        if (project.isInitialized()) {

        }
    }

    // TODO list commits
    public void onCommitsButtonClick(View v) {
        if (project.isInitialized()) {
        }
    }

    // list file structure
    public void onContentsButtonClick(View v) {
        if (project.isInitialized()) {
            Intent projectcontent_intent = new Intent(ProjectInfoActivity.this, ProjectContentsActivity.class);
            startActivity(projectcontent_intent);
        }
    }

    // TODO list builds
    public void onBuildsButtonClick(View v) {

    }

    // TODO create new build
    public void onNewBuildButtonClick(View v) {


    }

    @Override
    public void onCommandExecuted(int commandID, boolean success) {
        switch (commandID) {
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
            case JGitCommand.INIT_COMMAND:
                if (success) {
                    project.init(); // set initialized to true
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Init", Toast.LENGTH_SHORT);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Init Failed", Toast.LENGTH_SHORT);
                    tst.show();
                }
            default:
                break;
        }
    }
}
