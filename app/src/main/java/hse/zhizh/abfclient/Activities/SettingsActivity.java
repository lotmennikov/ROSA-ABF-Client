package hse.zhizh.abfclient.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import hse.zhizh.abfclient.R;


public class SettingsActivity extends ActionBarActivity {

    EditText groupText;
    EditText projectText;
    EditText branchText;
    String dgroup, dproject, dbranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences shp = getApplicationContext().getSharedPreferences(getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        dgroup = shp.getString("DefaultGroup", "");
        dproject = shp.getString("DefaultProject", "");
        dbranch = shp.getString("DefaultBranch", "");

        groupText = (EditText)findViewById(R.id.defaultGroupText);
        projectText = (EditText)findViewById(R.id.defaultProjectText);
        branchText = (EditText)findViewById(R.id.defaultBranchText);

        groupText.setText(dgroup);
        projectText.setText(dproject);
        branchText.setText(dbranch);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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


    public void onSaveSettingsButtonClick(View v) {
        dgroup = groupText.getText().toString();
        dproject = projectText.getText().toString();
        dbranch =  branchText.getText().toString();

        SharedPreferences shp = this.getApplicationContext().getSharedPreferences(getString(R.string.app_preferences_file), Context.MODE_PRIVATE);
        shp.edit()
                .putString("DefaultGroup", dgroup)
                .putString("DefaultProject", dproject)
                .putString("DefaultBranch", dbranch)
                .commit();
        this.finish();
    }
}
