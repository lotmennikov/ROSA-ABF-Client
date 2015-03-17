package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private String[] updateTypes = new String[]{ "recommended", "bugfix", "security", "enhancement", "newpackage" };

    // selection results
    private Platform selectedPlatform;
    private Architecture selectedArchitecture;
    private Repo selectedPlRepo;
    private ProjectRepo selectedProjectRepo;
    private ProjectRef selectedRef;
    private String updateType;

    // final response
    private BuildResponse buildResponse;

    // queries
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Start New Build");

        project = Settings.currentProject;

        projectName = (EditText)findViewById(R.id.newb_projectname);
        refsSpinner = (Spinner)findViewById(R.id.newb_versionspinner);
        reposSpinner = (Spinner)findViewById(R.id.newb_repospinner);
        platformsSpinner = (Spinner)findViewById(R.id.newb_platfspinner);
        archesSpinner = (Spinner)findViewById(R.id.newb_archspinner);
        updateSpinner = (Spinner)findViewById(R.id.newb_updatespinner);
        plReposSpinner = (Spinner)findViewById(R.id.newb_platfrepospinner);

        projectName.setText(project.getFullname());

        // update spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, updateTypes);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        updateSpinner.setAdapter(arrayAdapter);

        // empty spinners
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, new String[0]);
        archesSpinner.setAdapter(arrayAdapter);
        reposSpinner.setAdapter(arrayAdapter);
        platformsSpinner.setAdapter(arrayAdapter);
        plReposSpinner.setAdapter(arrayAdapter);
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

        reposQuery = new ABFProjectRepos(this, project.getId());
        reposQuery.execute();
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
        try {
            String[] refsNames = new String[refs.length];
            for (int i = 0; i < refs.length; ++i)
                refsNames[i] = refs[i].getRef();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, refsNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            refsSpinner.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, new String[0]);
            refsSpinner.setAdapter(arrayAdapter);
            Toast.makeText(getApplicationContext(),"Internal error", Toast.LENGTH_SHORT).show();
//            this.finish();
        }
    }

    private void setReposList() {
        try {
            String[] reposNames = new String[repos.length];
            for (int i = 0; i < repos.length; ++i)
                reposNames[i] = repos[i].getName();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, reposNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            reposSpinner.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Internal error", Toast.LENGTH_SHORT).show();
//            this.finish();
        }
    }

    // setting platform and platform repositories spinners
    private void setPlatformsList() {
        try {
            // setting platform spinner
            final String[] platformsNames = new String[platforms.length];
            for (int i = 0; i < platforms.length; ++i)
                platformsNames[i] = platforms[i].getMessage();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, platformsNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            platformsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedPlatform = platforms[platformsSpinner.getSelectedItemPosition()];
                    String[] plReposNames = new String[selectedPlatform.getRepos().length];
                    for (int i = 0; i < plReposNames.length; ++i)
                        plReposNames[i] = selectedPlatform.getRepos()[i].getName();

                    ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(NewBuildActivity.this, R.layout.contents_list_element, plReposNames);
                    aAdapter.setDropDownViewResource(R.layout.contents_list_element);
                    plReposSpinner.setAdapter(aAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedPlatform = null;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewBuildActivity.this, R.layout.contents_list_element, new String[0]);
                    refsSpinner.setAdapter(arrayAdapter);
                }
            });
            platformsSpinner.setAdapter(arrayAdapter);

            // setting selected platform repositories spinner
            selectedPlatform = platforms[0];
            String[] plReposNames = new String[selectedPlatform.getRepos().length];
            for (int i = 0; i < plReposNames.length; ++i)
                plReposNames[i] = selectedPlatform.getRepos()[i].getName();

            arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, plReposNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            plReposSpinner.setAdapter(arrayAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Internal error", Toast.LENGTH_SHORT).show();
//            this.finish();
        }
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
        try {
            int platformInd = platformsSpinner.getSelectedItemPosition();
            int plreposInd = plReposSpinner.getSelectedItemPosition();
            int refsInd = refsSpinner.getSelectedItemPosition();
            int reposInd = reposSpinner.getSelectedItemPosition();
            int archesInd = archesSpinner.getSelectedItemPosition();
            int updateInd = updateSpinner.getSelectedItemPosition();
            if (platformInd >= 0 && plreposInd>=0 && refsInd>=0 && reposInd >=0 && archesInd >=0 && updateInd >= 0) {
                selectedPlatform = platforms[platformInd];
                selectedArchitecture = arches[archesInd];
                selectedProjectRepo = repos[reposInd];
                selectedRef = refs[refsInd];
                selectedPlRepo = selectedPlatform.getRepos()[plreposInd];
                updateType = updateTypes[updateInd];
            }
        } catch (Exception e) {
            Log.d(Settings.TAG, "Something is not selected");
        }
        // TODO start build
        if (selectedArchitecture != null &&
            selectedPlatform != null &&
            selectedPlRepo != null &&
            selectedProjectRepo != null &&
            selectedRef != null &&
            project != null &&
            updateType != null) {

            Log.d(Settings.TAG, "New Build:" +
                              "\nArchitecture:" + selectedArchitecture.getName() +
                              "\nPlatform: " + selectedPlatform.getMessage() +
                              "\nPlatformRepo: " + selectedPlRepo.getName() +
                              "\nProjectRepo: " + selectedProjectRepo.getName() +
                              "\nProjectRef: " + selectedRef.getRef() +
                              "\nUpdateType: " + selectedPlatform.getMessage()
            );

            newBuildQuery = new ABFNewBuild(this,
                                            project.getId(),
                                            selectedRef.getSha(),
                                            updateType,
                                            selectedProjectRepo.getId(),
                                            selectedPlatform.getId(),
                                            new int[] { selectedPlRepo.getId() },
                                            selectedArchitecture.getId());

            newBuildQuery.execute();

            Toast.makeText(this, "Sending Build Request...", Toast.LENGTH_SHORT).show();
        }
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
                    setReposList();
                    Toast.makeText(this, "ProjectRepos were received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ProjectRepos were not received", Toast.LENGTH_SHORT).show();
                }
                break;
            case ABFQuery.NEWBUILD_QUERY:
                Intent resultIntent = new Intent();
                if (success) {
                    // TODO Replace with dialog
                    Toast.makeText(getApplicationContext(), "BuildID: " + newBuildQuery.result.getBuildId() + "\n"
                                                                        + newBuildQuery.result.getMessage(),
                                                                        Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, resultIntent);
                    this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "BuildRequest Failed", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, resultIntent);
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
