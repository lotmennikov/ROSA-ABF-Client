package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;

import hse.zhizh.abfclient.ABFQueries.ABFProjects;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/* Список проектов
 */
public class ProjectsActivity extends ActionBarActivity implements CommandResultListener {

    ListView projectsList;
    ABFProjects ProjectsCommand;

    Project[] projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        projectsList = (ListView)findViewById(R.id.projectsList);

        projectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ProjectsActivity.this.getApplicationContext(), "Click on project " + position + projects[position].getFullname(), Toast.LENGTH_LONG).show();

                // текущий проект - выбранный
                Settings.currentProject = projects[position];

                // сделать репу, если нет
                if (Settings.currentProject.getRepo() == null) {
                    Settings.currentProject.createRepo();
                }
                Log.d(Settings.TAG + " ProjectsActivity", "Project onClick, starting projectInfoActivity " + position);

                // переход к проекту
                Intent projectinfo_intent = new Intent(ProjectsActivity.this, ProjectInfoActivity.class);
                startActivity(projectinfo_intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projects, menu);
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

    public void onGetProjectsButtonClick(View v) {

        if (ProjectsCommand == null) {
            ProjectsCommand = new ABFProjects(this);
            ProjectsCommand.execute();
        }
    }

    @Override
    public void onCommandExecuted(int commandId, boolean success) {
        // получение списка проектов
        if (success) {
            projects = ProjectsCommand.projects;

            String[] proj = new String[projects.length];
            String newtext;
            for (int i = 0; i < proj.length; ++i) {
                newtext = projects[i].getId() + "\n"
                       +  projects[i].getName() + " (" + projects[i].getFullname() + ")\n";
//                       +  projects[i].getDescription() + "\n";
                proj[i] = newtext;
            }


            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, proj);
            projectsList.setAdapter(listAdapter);

            Log.d(Settings.TAG + " ProjectsActivity", "Setting Listener");

            Toast tst = Toast.makeText(this.getApplicationContext(), "Got Projects", Toast.LENGTH_LONG);
            tst.show();
        } else {
            Toast tst = Toast.makeText(this.getApplicationContext(), "GetProjects Failed", Toast.LENGTH_LONG);
            tst.show();
        }
        ProjectsCommand = null;
    }
}
