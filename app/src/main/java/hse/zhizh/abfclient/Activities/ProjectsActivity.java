package hse.zhizh.abfclient.Activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.ABFQueries.ABFProjects;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.R;

/* Список проектов
 */
public class ProjectsActivity extends ActionBarActivity implements CommandResultListener {

    TextView test_projectslist;
    ABFProjects ProjectsCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        test_projectslist = (TextView)findViewById(R.id.test_projectslabel);
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
        if (success) {
            Project[] proj = ProjectsCommand.projects;

            String newtext = "";
            for (int i = 0; i < proj.length; ++i) {
                newtext += proj[i].getId() + "\n"
                        +  proj[i].getName() + " (" + proj[i].getFullname() + ")\n"
                        +  proj[i].getDescription() + "\n";
            }
            test_projectslist.setText(newtext);
            Toast tst = Toast.makeText(this.getApplicationContext(), "Got Projects", Toast.LENGTH_LONG);
            tst.show();
        } else {
            Toast tst = Toast.makeText(this.getApplicationContext(), "GetProjects Failed", Toast.LENGTH_LONG);
            tst.show();
            test_projectslist.setText("ProjectsJSON: " + ProjectsCommand.response);
        }
        ProjectsCommand = null;
    }
}
