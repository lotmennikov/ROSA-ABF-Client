package hse.zhizh.abfclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        startActivityForResult(settings_intent, 1);
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

    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        if (res_intent != null) {
            String usr = res_intent.getStringExtra("Username");
            String pwd = res_intent.getStringExtra("Password");
            TextView usr_lab = (TextView)findViewById(R.id.username_label);
            TextView pass_lab = (TextView)findViewById(R.id.password_label);
            usr_lab.setText(usr);
            pass_lab.setText(pwd);
        }

    }

}
