package hse.zhizh.abfclient.Activities;

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
 *
 */
public class NewBuildActivity extends ActionBarActivity implements CommandResultListener {

    // input
    private Project project;
    private Platform[] platforms;
    private Architecture[] arches;
    private ProjectRef[] refs;
    private ProjectRepo[] repos;
    private String defaultBranch;
    private String[] buildprefs;

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
    private int countQueries;

    // interface
    EditText projectName;
    Spinner refsSpinner;
    Spinner reposSpinner;
    Spinner platformsSpinner;
    Spinner plReposSpinner;
    Spinner updateSpinner;
    Spinner archesSpinner;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("Start New Build");

        project = Settings.currentProject;
        defaultBranch = getIntent().getStringExtra("branchName");
        buildprefs = Settings.getBuildPrefs(project.getName());

        projectName = (EditText)findViewById(R.id.newb_projectname);
        refsSpinner = (Spinner)findViewById(R.id.newb_versionspinner);
        reposSpinner = (Spinner)findViewById(R.id.newb_repospinner);
        platformsSpinner = (Spinner)findViewById(R.id.newb_platfspinner);
        archesSpinner = (Spinner)findViewById(R.id.newb_archspinner);
        updateSpinner = (Spinner)findViewById(R.id.newb_updatespinner);
        plReposSpinner = (Spinner)findViewById(R.id.newb_platfrepospinner);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading...");
        projectName.setText(project.getFullname());

        // update spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, updateTypes);
        arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
        updateSpinner.setAdapter(arrayAdapter);
        if (buildprefs[5] != null) {
            for (int i = 0; i < updateTypes.length; ++i)
                if (updateTypes[i].equals(buildprefs[5])) {
                    updateSpinner.setSelection(i);
                    break;
                }
        }

        // empty spinners
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, new String[0]);
        archesSpinner.setAdapter(arrayAdapter);
        reposSpinner.setAdapter(arrayAdapter);
        platformsSpinner.setAdapter(arrayAdapter);
        plReposSpinner.setAdapter(arrayAdapter);
        refsSpinner.setAdapter(arrayAdapter);

        sendAPIRequests();

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (countQueries > 0) {
                    platformsQuery.cancel(true);
                    archesQuery.cancel(true);
                    refsQuery.cancel(true);
                    reposQuery.cancel(true);

                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void sendAPIRequests() {
        progressDialog.show();
        countQueries = 0;

        archesQuery = new ABFArches(this);
        archesQuery.execute();
        ++countQueries;

        platformsQuery = new ABFPlatforms(this);
        platformsQuery.execute();
        ++countQueries;

        refsQuery = new ABFProjectRefs(this, project.getId());
        refsQuery.execute();
        ++countQueries;

        reposQuery = new ABFProjectRepos(this, project.getId());
        reposQuery.execute();
        ++countQueries;
    }

    private void setArchesList() {
        try {
            String[] archesNames = new String[arches.length];
            for (int i = 0; i < arches.length; ++i)
                archesNames[i] = arches[i].getName();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, archesNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            archesSpinner.setAdapter(arrayAdapter);
            if (buildprefs[4] != null) {
                for (int i = 0; i < archesNames.length; ++i)
                    if (archesNames[i].equals(buildprefs[4])) {
                        archesSpinner.setSelection(i);
                        break;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, new String[0]);
            refsSpinner.setAdapter(arrayAdapter);
            Toast.makeText(getApplicationContext(),"Internal error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRefsList() {
        try {
            String[] refsNames = new String[refs.length];
            for (int i = 0; i < refs.length; ++i)
                refsNames[i] = refs[i].getRef();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, refsNames);
            arrayAdapter.setDropDownViewResource(R.layout.contents_list_element);
            refsSpinner.setAdapter(arrayAdapter);

            if (defaultBranch != null) {
                for (int i = 0; i < refsNames.length; ++i) {
                    if (refsNames[i].equals(defaultBranch)) {
                        refsSpinner.setSelection(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, new String[0]);
            refsSpinner.setAdapter(arrayAdapter);
            Toast.makeText(getApplicationContext(),"Internal error", Toast.LENGTH_SHORT).show();
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
            if (buildprefs[2] != null) {
                for (int i = 0; i < reposNames.length; ++i)
                    if (reposNames[i].equals(buildprefs[2])) {
                        reposSpinner.setSelection(i);
                        break;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Internal error", Toast.LENGTH_SHORT).show();
        }
    }

    // sets platform and platform repositories spinners
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
                    setPlReposList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedPlatform = null;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewBuildActivity.this, R.layout.contents_list_element, new String[0]);
                    refsSpinner.setAdapter(arrayAdapter);
                }
            });
            platformsSpinner.setAdapter(arrayAdapter);

            selectedPlatform = platforms[0];
            if (buildprefs[0] != null) {
                for (int i = 0; i < platformsNames.length; ++i)
                    if (platformsNames[i].equals(buildprefs[0])) {
                        platformsSpinner.setSelection(i);
                        selectedPlatform = platforms[i];
                        break;
                    }
            }
            setPlReposList();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Internal error", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPlReposList() {
        try {
            if (selectedPlatform != null) {
                String[] plReposNames = new String[selectedPlatform.getRepos().length];
                for (int i = 0; i < plReposNames.length; ++i)
                    plReposNames[i] = selectedPlatform.getRepos()[i].getName();

                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, plReposNames);
                aAdapter.setDropDownViewResource(R.layout.contents_list_element);
                plReposSpinner.setAdapter(aAdapter);
                if (buildprefs[1] != null) {
                    for (int i = 0; i < plReposNames.length; ++i)
                        if (plReposNames[i].equals(buildprefs[1])) {
                            plReposSpinner.setSelection(i);
                            break;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Internal error", Toast.LENGTH_SHORT).show();
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
                              "\nUpdateType: " + updateType
            );

            Settings.setBuildPrefs(project.getName(),
                    new String[] {
                    selectedPlatform.getMessage(),
                    selectedPlRepo.getName(),
                    selectedProjectRepo.getName(),
                    selectedRef.getRef(),
                    selectedArchitecture.getName(),
                    updateType});

            newBuildQuery = new ABFNewBuild(this,
                                            project.getId(),
                                            selectedRef.getSha(),
                                            updateType,
                                            selectedProjectRepo.getId(),
                                            selectedPlatform.getId(),
                                            new int[] { selectedPlRepo.getId() },
                                            selectedArchitecture.getId());
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Waiting for server response", Toast.LENGTH_SHORT).show();
                }
            });
            // Sends build request
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
                if (success) {
                    arches = archesQuery.result;
                    setArchesList();
                    --countQueries;
                    if (countQueries <= 0)
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(this, "Architectures have not been received", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    this.finish();
                }
                break;
            case ABFQuery.PLATFORMS_QUERY:
                if (success) {
                    platforms = platformsQuery.result;
                    setPlatformsList();
                    --countQueries;
                    if (countQueries <= 0)
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(this, "Platforms have not been received", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    this.finish();
                }
                break;
            case ABFQuery.PROJECTREFS_QUERY:
                if (success) {
                    refs = refsQuery.result;
                    setRefsList();
                    --countQueries;
                    if (countQueries <= 0)
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(this, "ProjectRefs have not been received", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    this.finish();
                }
                break;
            case ABFQuery.PROJECTREPOS_QUERY:
                if (success) {
                    repos = reposQuery.result;
                    setReposList();
                    --countQueries;
                    if (countQueries <= 0)
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(this, "ProjectRepos have not been received", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    this.finish();
                }
                break;
            case ABFQuery.NEWBUILD_QUERY:
                Intent resultIntent = new Intent();
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (success) {
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
