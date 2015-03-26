package hse.zhizh.abfclient.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;


public class SettingsActivity extends ActionBarActivity {

    boolean showBuild;
    private RadioGroup radioGroupBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle("Settings");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        showBuild = Settings.showBuildMessage;

        radioGroupBuild = (RadioGroup)findViewById(R.id.radiogroup_buildstatus);

        if (showBuild)
            radioGroupBuild.check(R.id.radio_showmessage);
        else
            radioGroupBuild.check(R.id.radio_showcode);
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
        if (radioGroupBuild.getCheckedRadioButtonId() == R.id.radio_showcode)
            showBuild = false;
        else
            showBuild = true;
        Settings.showBuildMessage = showBuild;

        Settings.saveUserPrefs();

        this.finish();
    }
}
