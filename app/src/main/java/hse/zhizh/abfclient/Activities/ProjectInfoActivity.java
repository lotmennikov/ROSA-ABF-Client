package hse.zhizh.abfclient.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.JGitCommands.JGitClone;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;

public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        TextView textView = (TextView)findViewById(R.id.filesdir_label);
        textView.setText(getFilesDir().getAbsolutePath());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_info, menu);
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

    // TODO универсализировать
    // Тест клонирования объекта
    public void onCloneButtonClick(View v) {
        Repository rep = new Repository(this.getApplicationContext());
        JGitClone clonecom = new JGitClone(rep, this);
        clonecom.execute();
    }

    @Override
    public void onCommandExecuted(boolean success) {
        if (success) {
            Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_LONG);
            tst.show();
        } else {
            Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_LONG);
            tst.show();
        }
    }
}
