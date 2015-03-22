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


    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        this.finish(); // just exit
    }

}
