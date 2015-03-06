package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hse.zhizh.abfclient.ABFQueries.ABFProjects;
import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.Database.FeedProjectsDbHelper;
import hse.zhizh.abfclient.GitWrappers.GitClone;
import hse.zhizh.abfclient.GitWrappers.GitCommand;
import hse.zhizh.abfclient.GitWrappers.GitInit;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/* Список проектов
 */
public class ProjectsActivity extends ActionBarActivity implements CommandResultListener {

    ListView projectsList;
    ABFProjects ProjectsCommand;
    GitClone cloneCommand;

    Project[] projects;

    AlertDialog initDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        projectsList = (ListView)findViewById(R.id.projectsList);

        projectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ProjectsActivity.this.getApplicationContext(), "Click on project " + position + projects[position].getFullname(), Toast.LENGTH_SHORT).show();

                // текущий проект - выбранный
                Settings.currentProject = projects[position];

                // сделать репу, если нет
                if (Settings.currentProject.getRepo() == null) {
                    Settings.currentProject.createRepo();
                }

                // выбор способа инициализации
                if (!Settings.currentProject.isInitialized()) {
                    initDialog.show();
                } else {
                    startProjectActivity();
                }
                Log.d(Settings.TAG + " ProjectsActivity", "Project onClick, starting projectInfoActivity " + position);
            }
        });
        initInitDialog();

//        getDatabaseProjects();
    }

    // меню выбора способа инициализации проекта
    private void initInitDialog() {
//        String[] menu_options = new String[]{ "Init locally", "Clone" };
//        ArrayAdapter<String> branchesAdapter = new ArrayAdapter<String>(this, R.layout.dialog_menu_item,menu_options );
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Initialize project");

        // init
        blder.setNegativeButton("Init locally", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GitInit initCommand = new GitInit(Settings.currentProject.getRepo());
                if (initCommand.execute()) { // норм
                    Settings.currentProject.init();
                    Toast.makeText(getApplicationContext(), "Init", Toast.LENGTH_SHORT).show();

                    // переход
                    startProjectActivity();
                } else
                    Toast.makeText(getApplicationContext(), "Init Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // clone
        blder.setPositiveButton("Clone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // clone
                cloneCommand = new GitClone(Settings.currentProject.getRepo(), ProjectsActivity.this);
                cloneCommand.execute();
                // ждём клонирования
                // TODO показать крутяшку
            }
        });
        initDialog = blder.create();
    }

    // переход к проекту
    private void startProjectActivity() {
        Intent projectinfo_intent = new Intent(ProjectsActivity.this, ProjectInfoActivity.class);
        startActivity(projectinfo_intent);
    }

    // TODO связь с базой - получение проектов в базе
    private void getDatabaseProjects() {
        //Тест добавления и создания БД
        FeedProjectsDbHelper h = new FeedProjectsDbHelper(getBaseContext());
//        System.out.println("NEW ROW: " + h.addProject("test","test","test","test","test","test", true));

    }

    // получение проектов
    public void onGetProjectsButtonClick(View v) {
        if (ProjectsCommand == null) {
            ProjectsCommand = new ABFProjects(this);
            ProjectsCommand.execute();
        }
    }


    // Test
    public void onGetDBProjectsClick(View v) {
        FeedProjectsDbHelper h = new FeedProjectsDbHelper(getBaseContext());
        try {
            ArrayList<Project> projectsList = h.readProjects();
            h.close();
            projects = (Project[]) projectsList.toArray();
            setProjectsList();
        } catch (Exception e) {
            e.printStackTrace();
            h.close();
//            h = new FeedProjectsDbHelper(getBaseContext());
//            h.
        }

    }

    // получение проектов, завершение клонирования
    @Override
    public void onCommandExecuted(int commandID, boolean success) {
            switch (commandID) {
                // клонирование
                case GitCommand.CLONE_COMMAND:
                    if (success) {
                        Settings.currentProject.init(); // set initialized to true
                        Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_SHORT);
                        tst.show();

                        // переход к проекту
                        startProjectActivity();
                    } else {
                        Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_SHORT);
                        tst.show();
                    }
                    break;

                // получение списка проектов
                case ABFQuery.PROJECTS_QUERY:
                    if (success) {
                        projects = ProjectsCommand.projects;

                        setProjectsList();

                        Toast tst = Toast.makeText(this.getApplicationContext(), "Got Projects", Toast.LENGTH_SHORT);
                        tst.show();
                    } else {
                        Toast tst = Toast.makeText(this.getApplicationContext(), "GetProjects Failed", Toast.LENGTH_SHORT);
                        tst.show();
                    }
                    ProjectsCommand = null;

                    break;
                default:
                    break;
            }

    }

    public void setProjectsList() {
        String[] proj = new String[projects.length];
        String newtext;
        for (int i = 0; i < proj.length; ++i) {
            newtext = projects[i].getId() + "\n"
                    + projects[i].getName() + " (" + projects[i].getFullname() + ")\n";
//                       +  projects[i].getDescription() + "\n";
            proj[i] = newtext;
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, proj);
        projectsList.setAdapter(listAdapter);

        Log.d(Settings.TAG + " ProjectsActivity", "Setting Listener");
    }


// --------------------
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

}
