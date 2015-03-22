package hse.zhizh.abfclient.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

public class FileChooserActivity extends ActionBarActivity {

    private ListView fileList;
    private File parentDir;
    private File currentDir;
    private File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        fileList =  (ListView)findViewById(R.id.browserList);
        parentDir = new File("mnt/sdcard/");
        currentDir = parentDir;
        setFileList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_chooser, menu);
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

// File Chooser

    // получение списка файлов в текущей папке
    private File[] getFileArray() {
        File[] files = currentDir.listFiles();
        if (files == null)
            files = new File[0];
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
                    Intent intent = new Intent();
                    intent.putExtra("Filename", files[position].getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    FileChooserActivity.this.finish();
                }
            }
        });
        getSupportActionBar().setTitle(currentDir.getAbsolutePath());
    }



    @Override
    public void onBackPressed() {
        if (!currentDir.getAbsolutePath().equals(parentDir.getAbsolutePath())) {
            currentDir = currentDir.getParentFile();
            setFileList();
        }
        else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            this.finish();
        }
    }





}
