package hse.zhizh.abfclient.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import hse.zhizh.abfclient.ABFQueries.ABFArches;
import hse.zhizh.abfclient.ABFQueries.ABFNewBuild;
import hse.zhizh.abfclient.ABFQueries.ABFPlatforms;
import hse.zhizh.abfclient.ABFQueries.ABFProjectRefs;
import hse.zhizh.abfclient.ABFQueries.ABFProjectRepos;
import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.Model.Architecture;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.ProjectRef;
import hse.zhizh.abfclient.Model.ProjectRepo;
import hse.zhizh.abfclient.Model.Repo;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/*
 *
 * Configuring and starting a new build
 *
 * TODO start the build
 *
 */
public class NewBuildActivity extends ActionBarActivity implements CommandResultListener {

    // input
    private Project project;
    private Platform[] platforms;
    private Architecture[] arches;
    private ProjectRef[] refs;
    private ProjectRepo[] repos;

    private String[] updateTypes = new String[]{ "recommended", "bugfix" };

    // selection results
    private Platform selectedPlatform;
    private Architecture selectedArchitecture;
    private Repo selectedPlRepo;
    private ProjectRepo selectedProjectRepo;
    private ProjectRef selectedRef;
    private String updateType;

    // final response
    private BuildResponse buildResponse;

    private ABFPlatforms platformsQuery;
    private ABFArches archesQuery;
    private ABFProjectRefs refsQuery;
    private ABFProjectRepos reposQuery;
    private ABFNewBuild newBuildQuery;

    // interface
    EditText projectName;
    Spinner refsSpinner;
    Spinner reposSpinner;
    Spinner platformsSpinner;
    Spinner plReposSpinner;
    Spinner updateSpinner;
    Spinner archesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
        getSupportActionBar().setIcon(R.drawable.giticonabf);
        setTitle("Start New Build");

        project = Settings.currentProject;

        projectName = (EditText)findViewById(R.id.newb_projectname);
        refsSpinner = (Spinner)findViewById(R.id.newb_versionspinner);
        reposSpinner = (Spinner)findViewById(R.id.newb_repospinner);
        platformsSpinner = (Spinner)findViewById(R.id.newb_platfspinner);
        archesSpinner = (Spinner)findViewById(R.id.newb_archspinner);
        updateSpinner = (Spinner)findViewById(R.id.newb_updatespinner);

        projectName.setText(project.getFullname());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, updateTypes);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        updateSpinner.setAdapter(arrayAdapter);
        archesSpinner.setAdapter(arrayAdapter);
        reposSpinner.setAdapter(arrayAdapter);
        platformsSpinner.setAdapter(arrayAdapter);
        refsSpinner.setAdapter(arrayAdapter);

        sendAPIRequests();
    }

    private void sendAPIRequests() {
        archesQuery = new ABFArches(this);
        archesQuery.execute();

        platformsQuery = new ABFPlatforms(this);
        platformsQuery.execute();

        refsQuery = new ABFProjectRefs(this, project.getId());
        refsQuery.execute();

//        reposQuery = new ABFProjectRepos(this, project.getId());
//        reposQuery.execute();


    }

    private void setArchesList() {
        String[] archesNames = new String[arches.length];
        for (int i = 0; i < arches.length; ++i)
            archesNames[i] = arches[i].getName();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, archesNames);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        archesSpinner.setAdapter(arrayAdapter);
    }

    private void setRefsList() {
        String[] refsNames = new String[refs.length];
        for (int i = 0; i <refs.length; ++i)
            refsNames[i] = refs[i].getSha();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, refsNames);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        refsSpinner.setAdapter(arrayAdapter);
    }

    private void setPlatformsList() {
        String[] platformsNames = new String[platforms.length];
        for (int i = 0; i < platforms.length; ++i)
            platformsNames[i] = platforms[i].getMessage();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, platformsNames);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        platformsSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_build, menu);
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


    public void onStartNewBuildClick(View v) {
        //TODO obtain selections

        // TODO start build
        if (selectedArchitecture != null &&
            selectedPlatform != null &&
            selectedPlRepo != null &&
            selectedProjectRepo != null &&
            project != null) {
/*
            newBuildQuery = new ABFNewBuild(this,
                                            project.getId(),
                                            selectedRef.getSha(),
                                            updateType,
                                            selectedProjectRepo.getId(),
                                            selectedPlatform.getId(),
                                            new int[] { selectedProjectRepo.getId() },
                                            selectedArchitecture.getId());

            newBuildQuery.execute();
*/
        }
        this.finish();
    }


    // Ответ на ABF API запрос получен
    @Override
    public void onCommandExecuted(int commandId, boolean success) {
        switch (commandId) {
// -------- ABF API ------------
            case ABFQuery.ARCHES_QUERY:
                // TODO get arches
                if (success) {
                    arches = archesQuery.result;
                    setArchesList();
                    Toast.makeText(this, "Architectures were received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Architectures were not received", Toast.LENGTH_SHORT).show();
                }
                break;
            case ABFQuery.PLATFORMS_QUERY:
                // TODO get platforms
                if (success) {
                    platforms = platformsQuery.result;
                    setPlatformsList();
                    Toast.makeText(this, "Platforms were received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Platforms were not received", Toast.LENGTH_SHORT).show();
                }
                break;
            case ABFQuery.PROJECTREFS_QUERY:
                // TODO
                if (success) {
                    refs = refsQuery.result;
                    setRefsList();
                    Toast.makeText(this, "ProjectRefs were received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ProjectRefs were not received", Toast.LENGTH_SHORT).show();
                }
                break;
            case ABFQuery.PROJECTREPOS_QUERY:
                // TODO
                if (success) {
                    repos = reposQuery.result;
                    Toast.makeText(this, "ProjectRepos were received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ProjectRepos were not received", Toast.LENGTH_SHORT).show();
                }
                break;
            case ABFQuery.NEWBUILD_QUERY:
                // TODO process response
                break;
            default:
                break;
        }
    }
}
