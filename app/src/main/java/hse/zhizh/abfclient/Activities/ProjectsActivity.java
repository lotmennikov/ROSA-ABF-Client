package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.jgit.diff.Edit;


import java.util.ArrayList;

import hse.zhizh.abfclient.ABFQueries.ABFProjectID;
import hse.zhizh.abfclient.ABFQueries.ABFProjects;
import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.Database.FeedProjectsDbHelper;
import hse.zhizh.abfclient.Database.ProjectsContract;
import hse.zhizh.abfclient.GitWrappers.GitClone;
import hse.zhizh.abfclient.GitWrappers.GitCommand;
import hse.zhizh.abfclient.GitWrappers.GitInit;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/* Список проектов
 *
 * Перечисление загруженных проектов
 * Добавление и клонирование нового проекта
 * Удаление репозитория проекта с устройства
 *
 *
 */
public class ProjectsActivity extends ActionBarActivity implements CommandResultListener {
    static final String ActivityTag = "ProjectInfoActivity";

    ListView projectsList;

    ABFQuery abfQuery;
    GitClone cloneCommand;

    Project[] projects;

    AlertDialog initDialog;
    AlertDialog deleteDialog;
    AlertDialog retryCloneDialog;
    ProgressDialog progressDialog;

    Dialog addProjectDialog;
    EditText addpGroup;
    EditText addpProject;
    Button addpClone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        setTitle("Projects");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        projectsList = (ListView)findViewById(R.id.projectsList);
        projectsList.setEmptyView(findViewById(R.id.projects_empty));

        abfQuery = null;

        // выбор проекта по обычному нажатию
        projectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // текущий проект - выбранный
                Settings.currentProject = projects[position];

                // сделать репу, если нет
                if (Settings.currentProject.getRepo() == null) {
                    Settings.currentProject.createRepo();
                }
                if (Settings.currentProject.isInitialized()) {
                    startProjectActivity();
                } else {
                    // выбор способа инициализации
                    if (Settings.currentProject.isLocal()) {
                        if (InitCurrentProjectRepository())
                            startProjectActivity();
                    } else {
                        initDialog.show();
                    }
                }

                Log.d(Settings.TAG, ActivityTag + " Project onClick, starting projectInfoActivity " + position);
            }
        });
        // удаление по длинному нажжатию
        projectsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (projects[position].isLocal()) {
                    Settings.currentProject = projects[position];
                    deleteDialog.show();
                }
                return true;
            }
        });

        initInitDialog();
        initAddProjectDialog();
        initDeleteDialog();
        initRetryDialog();
        progressDialog = new ProgressDialog(this);

        getDatabaseProjects();
    }

    // Test - меню выбора способа инициализации проекта
    private void initInitDialog() {
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Initialize project");

        // init
        blder.setNegativeButton("Init locally", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (InitCurrentProjectRepository()) {
                    AddCurrentProjectToDB();
                    startProjectActivity();// переход
                }
            }
        });

        // clone
        blder.setPositiveButton("Clone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // clone
                cloneCommand = new GitClone(Settings.currentProject.getRepo(), ProjectsActivity.this);
                cloneCommand.execute();
                progressDialog.setTitle("Cloning project...");
                if (!progressDialog.isShowing()) progressDialog.show();
                // ждём клонирования

            }
        });
        initDialog = blder.create();
    }

    // Диалог добавления проекта
    private void initAddProjectDialog() {
        // custom dialog

        addProjectDialog = new Dialog(this);
        addProjectDialog.setContentView(R.layout.dialog_addproject);
        addProjectDialog.setTitle("Add project");

        addpGroup = (EditText)addProjectDialog.findViewById(R.id.addp_groupText);
        addpProject = (EditText)addProjectDialog.findViewById(R.id.addp_projectText);
        addpClone = (Button)addProjectDialog.findViewById(R.id.addp_clonebutton);

        addpClone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Clone только здесь
                String groupName = addpGroup.getText().toString();
                String projectName = addpProject.getText().toString();
                if (abfQuery == null) {
                    abfQuery = new ABFProjectID(ProjectsActivity.this,
                            projectName,
                            groupName);
                    abfQuery.execute();
                    progressDialog.setTitle("Retrieving project info...");
                    if (!progressDialog.isShowing()) progressDialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "AbfQuery not finished", Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Clone Project:" + groupName + "/" + projectName, Toast.LENGTH_SHORT).show();
                addProjectDialog.dismiss();
            }
        });

    }

    // Диалог удаления проекта
    private void initDeleteDialog() {
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Delete project?");

        // init
        blder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // clone
        blder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete from DB
                FeedProjectsDbHelper helper = new FeedProjectsDbHelper(getApplicationContext());
                int projectID = Settings.currentProject.getId();
                helper.deleteProject(projectID);
                helper.close();

                // TODO удаление с устройства
                Settings.currentProject = null;
                Toast.makeText(getApplicationContext(), "Project was removed from the DB, but not from the device", Toast.LENGTH_SHORT).show();
                // refresh list
                getDatabaseProjects();

            }
        });
        deleteDialog = blder.create();

    }

    // диалог повторного клонирования
    private void initRetryDialog() {
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Clone Failed. Retry?");

        // init
        blder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // clone
        blder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // запуск клонирования
                cloneCommand = new GitClone(Settings.currentProject.getRepo(), ProjectsActivity.this);
                cloneCommand.execute();
                progressDialog.setTitle("Cloning project...");
                if (!progressDialog.isShowing()) progressDialog.show();
            }
        });
        retryCloneDialog = blder.create();
    }

    // локальный инит - только если в базе
    private boolean InitCurrentProjectRepository() {
        GitInit initCommand = new GitInit(Settings.currentProject.getRepo());
        if (initCommand.execute()) { // норм
            Settings.currentProject.init();
//            Toast.makeText(getApplicationContext(), "Init", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Init Failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // переход к проекту
    private void startProjectActivity() {
        Intent projectinfo_intent = new Intent(ProjectsActivity.this, ProjectInfoActivity.class);
        startActivity(projectinfo_intent);
    }

    // добавление проекта в базу
    public void AddCurrentProjectToDB() {
//        Toast.makeText(getApplicationContext(), "Adding project to DB - " + Settings.currentProject.getName(), Toast.LENGTH_SHORT).show();
        Settings.currentProject.setLocal(true);
        FeedProjectsDbHelper helper = new FeedProjectsDbHelper(this.getApplicationContext());
        helper.addProject(Settings.currentProject);
        helper.close();
    }

    // Связь с базой - получение проектов в базе
    private void getDatabaseProjects() {
        FeedProjectsDbHelper h = new FeedProjectsDbHelper(getApplicationContext());
        try {
            ArrayList<Project> projectsList = h.readProjects();
            h.close();
            projects = projectsList.toArray(new Project[projectsList.size()]);
            setProjectsList();
//            Toast.makeText(getApplicationContext(), projectsList.size() + " projects in DB", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            h.close();
            Toast.makeText(getApplicationContext(), "Database Read Error", Toast.LENGTH_SHORT).show();
        }
    }

    // получение проектов по ABF
    public void onGetProjectsButtonClick(View v) {
        if (abfQuery == null) {
            abfQuery = new ABFProjects(this);
            abfQuery.execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "ABF Query has not finished yet", Toast.LENGTH_SHORT).show();
        }
    }

    // Test
    public void onGetDBProjectsClick(View v) {
        getDatabaseProjects();
    }

    // Test
    public void onClearDBButtonClick(View v) {
        FeedProjectsDbHelper helper = new FeedProjectsDbHelper(this.getApplicationContext());
        helper.deleteAll();
        helper.close();
        Toast.makeText(this.getApplicationContext(), "All projects were removed", Toast.LENGTH_SHORT).show();
    }

    // получение проектов, завершение клонирования
    @Override
    public void onCommandExecuted(int commandID, boolean success) {
            switch (commandID) {

// ************ JGIt ***************
                // клонирование
                case GitCommand.CLONE_COMMAND:
                    progressDialog.dismiss();
                    if (success) {
                        Settings.currentProject.init(); // set initialized to true
                        Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_SHORT);
                        tst.show();
                        AddCurrentProjectToDB();

                        // переход к проекту
                        startProjectActivity();
                    } else {
                        // попробовать ещё
                        retryCloneDialog.show();
                        Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_SHORT);
                        tst.show();
                    }
                    break;
// ********** ABF *******************
                // получение списка проектов
                case ABFQuery.PROJECTS_QUERY:
                    if (success) {
                        projects = ((ABFProjects)abfQuery).projects;

                        setProjectsList();

                        Toast tst = Toast.makeText(this.getApplicationContext(), "Got Projects", Toast.LENGTH_SHORT);
                        tst.show();
                    } else {
                        Toast tst = Toast.makeText(this.getApplicationContext(), "GetProjects Failed", Toast.LENGTH_SHORT);
                        tst.show();
                    }
                    abfQuery = null;
                    break;

                case ABFQuery.PROJECTID_QUERY:
                    if (success) {
                        // теперь проект текущий
                        Settings.currentProject = ((ABFProjectID)abfQuery).result;
                        // сделать репу, если нет
                        if (Settings.currentProject.getRepo() == null) {
                            Settings.currentProject.createRepo();
                        }
                        // запуск клонирования
                        cloneCommand = new GitClone(Settings.currentProject.getRepo(), ProjectsActivity.this);
                        cloneCommand.execute();
                        progressDialog.setTitle("Cloning project...");
                        if (!progressDialog.isShowing()) progressDialog.show();


                        Toast tst = Toast.makeText(this.getApplicationContext(), "Cloning ProjectID: " + Settings.currentProject.getId(), Toast.LENGTH_SHORT);
                        tst.show();
                    } else {
                        progressDialog.dismiss();
                        Toast tst = Toast.makeText(this.getApplicationContext(), "GetProjectID Failed", Toast.LENGTH_SHORT);
                        tst.show();
                    }
                    abfQuery = null;
                    break;

                default:
                    break;
            }

    }

    // вывод списка проектов
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

        Log.d(Settings.TAG, ActivityTag + " Setting Listener");
    }


// --------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projects, menu);
        return true;
    }

    // Кнопочки верхнего меню - добавление проекта
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
            {
                return true;
            }
            case R.id.action_addproject:
                addProjectDialog.show();
                return true;
            case R.id.test_cleardbButton:
                onClearDBButtonClick(null);
                break;
            case R.id.test_getdbprojects:
                onGetDBProjectsClick(null);
                break;
            case R.id.test_getprojects:
                onGetProjectsButtonClick(null);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh DB list
        getDatabaseProjects();
    }

}
