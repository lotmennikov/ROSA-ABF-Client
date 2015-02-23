package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import hse.zhizh.abfclient.JGitCommands.JGitBranch;
import hse.zhizh.abfclient.JGitCommands.JGitCommand;
import hse.zhizh.abfclient.JGitCommands.JGitSetBranch;
import hse.zhizh.abfclient.Model.Project;
import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

public class ProjectContentsActivity extends ActionBarActivity implements CommandResultListener {

//    String[] files = new String[] {".git", "spec.yml", ".abf.yml" };

    ListView fileList;
    File currentDir;

    String[] branches;
    AlertDialog branchDialog;

    Repository repo;
    File[] files;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contents);

        fileList = (ListView)findViewById(R.id.fileList);

        repo = Settings.currentProject.getRepo();

        if (repo.git == null) {
            Log.d("ABF Client ProjectContentsActivity", "Not initialized!");
            this.finish();
        }
        currentDir = repo.getDir();

        getBranches();
        setFileList();
    }

    // получение списка файлов в текущей папке
    private File[] getFileArray() {
        File[] files = currentDir.listFiles();
        return files;
    }

    // установка списка файлов
    private void setFileList() {
        files = getFileArray();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; ++i) {
            fileNames[i] = files[i].getName() + (files[i].isDirectory() ? "/" : "");
        }

        ArrayAdapter<String> filesAdapter = new ArrayAdapter<String>(this, R.layout.contents_list_element, fileNames);
        fileList.setAdapter(filesAdapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (files[position].isDirectory()) {
                    // проходим в другую папку
                    currentDir = files[position];
                    setFileList();
                }
                else {
                    // редактирование файла другим приложением
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    Uri uri = Uri.fromFile(files[position]);
                    intent.setDataAndType(uri, "text/plain");
                    startActivity(intent);
                }
            }
        });
    }

    private void getBranches() {
        JGitBranch brcom = new JGitBranch(repo);
        if (brcom.execute())
            branches = brcom.result;
        else
            this.finish();
    }

    public void onBranchButtonClick(View v) {
        ArrayAdapter<String> branchesAdapter = new ArrayAdapter<String>(this, R.layout.dialog_menu_item, branches);
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Set branch");
        blder.setAdapter(branchesAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JGitSetBranch setBranch = new JGitSetBranch(repo, ProjectContentsActivity.this, branches[which]);
                setBranch.execute();
            }
        });
        branchDialog = blder.create();
        branchDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_contents, menu);
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

    @Override
    public void onCommandExecuted(int commandId, boolean success) {
        switch (commandId) {
            case JGitCommand.SETBRANCH_COMMAND:

                if (success) {
                    if (currentDir.exists()) {
                        setFileList();
                    } else {
                        currentDir = repo.getDir();
                        setFileList();
                    }
                    Toast tst = Toast.makeText(this.getApplicationContext(), "new branch", Toast.LENGTH_LONG);
                    tst.show();
                } else {
                    Toast tst = Toast.makeText(this.getApplicationContext(), "branch set failed", Toast.LENGTH_LONG);
                    tst.show();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
