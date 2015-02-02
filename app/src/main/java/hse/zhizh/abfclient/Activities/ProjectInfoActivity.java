package hse.zhizh.abfclient.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hse.zhizh.abfclient.JGitCommands.JGitBranch;
import hse.zhizh.abfclient.JGitCommands.JGitClone;
import hse.zhizh.abfclient.JGitCommands.JGitCommand;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;

public class ProjectInfoActivity extends ActionBarActivity implements CommandResultListener {

    Repository repo;

    JGitBranch branchcom;

    TextView test_branchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        TextView textView = (TextView)findViewById(R.id.filesdir_label);
        textView.setText(getFilesDir().getAbsolutePath());

        test_branchlist = (TextView)findViewById(R.id.test_branchlist);

        repo = new Repository(this.getApplicationContext());
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
        if (repo.git == null) {
            JGitClone clonecom = new JGitClone(repo, this);
            clonecom.execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "Already cloned", Toast.LENGTH_LONG).show();
        }
    }

    // получить список веток
    public void onGetBranchesButtonClick(View v) {
        if (repo.git == null) {
            Toast.makeText(this.getApplicationContext(), "Not initialized", Toast.LENGTH_LONG).show();
        } else {
            branchcom = new JGitBranch(repo, this);
            branchcom.execute();
        }
    }

    @Override
    public void onCommandExecuted(int commandID, boolean success) {
        switch (commandID) {
            case JGitCommand.CLONE_COMMAND:
                if (success) {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Cloned", Toast.LENGTH_LONG);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "Clone Failed", Toast.LENGTH_LONG);
                    tst.show();
                }
                break;
            case JGitCommand.GETBRANCHES_COMMAND:
                if (success) {
                    String[] branches = branchcom.result;
                    String txt = "";
                    for (int i = 0; i< branches.length; ++i) {
                        txt += branches[i] + "\n";
                    }
                    test_branchlist.setText(txt);
                    Toast tst = Toast.makeText(this.getApplicationContext(), "GetBranches Successful", Toast.LENGTH_LONG);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "GetBranches Failed", Toast.LENGTH_LONG);
                    tst.show();
                }
                break;
            default:
                break;
        }
    }
}
