package hse.zhizh.abfclient.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import hse.zhizh.abfclient.ABFQueries.ABFQuery;
import hse.zhizh.abfclient.Model.Architecture;
import hse.zhizh.abfclient.Model.BuildResponse;
import hse.zhizh.abfclient.Model.Platform;
import hse.zhizh.abfclient.Model.Project;
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

    // selection results
    private Platform selectedPlatform;
    private Architecture selectedArchitecture;
    private Repo selectedPlRepo;
    private ProjectRepo selectedProjectRepo;

    // final response
    private BuildResponse buildResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
        getSupportActionBar().setIcon(R.drawable.giticonabf);
        setTitle("Start New Build");

        project = Settings.currentProject;
        getArches();
        getPlatforms();
        getProjectRepos();
    }

    private void getArches() {

    }

    private void getPlatforms() {

    }

    private void getProjectRepos() {

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

                } else {

                }
                break;
            case ABFQuery.PLATFORMS_QUERY:
                // TODO get platforms
                break;
            case ABFQuery.NEWBUILD_QUERY:
                // TODO process response
                break;
            // TODO project repos
            default:
                break;
        }
    }
}
