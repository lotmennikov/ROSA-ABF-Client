package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set default application context
        Settings.appContext = getApplicationContext();

        // move to login activity
        onLoginButtonClick(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onLoginButtonClick(View v) {
        Intent login_intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(login_intent, 1);
    }

    public void onSettingsButtonClick(View v) {
        Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settings_intent);
    }

    public void onProjectInfoButtonClick(View v) {
        Intent project_intent = new Intent(MainActivity.this, ProjectInfoActivity.class);
        startActivity(project_intent);
    }

    public void onProjectContentsButtonClick(View v) {
    }

    public void onProjectsButtonClick(View v) {
        Intent projects_intent = new Intent(MainActivity.this, ProjectsActivity.class);
        startActivity(projects_intent);
    }

    public void onCommitsButtonClick(View v) {
    }

    public void onBuildInfoButtonClick(View v) {
        Intent buildinfo_intent = new Intent(MainActivity.this, BuildInfoActivity.class);
        startActivity(buildinfo_intent);
    }

    public void onBuildsButtonClick(View v) {
    }

    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        if (res_intent != null) {
            String usr = res_intent.getStringExtra("Username");
            String pwd = res_intent.getStringExtra("Password");
            TextView usr_lab = (TextView)findViewById(R.id.username_label);
            TextView pass_lab = (TextView)findViewById(R.id.password_label);
            Settings.repo_password = pwd;
            Settings.repo_username = usr;
            usr_lab.setText(usr);
            pass_lab.setText(pwd);

            // перейти к проектам
            onProjectsButtonClick(null);
        }

    }

}
