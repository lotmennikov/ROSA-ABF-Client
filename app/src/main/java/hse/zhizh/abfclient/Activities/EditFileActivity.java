package hse.zhizh.abfclient.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.text.method.KeyListener;

import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import hse.zhizh.abfclient.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditFileActivity extends ActionBarActivity {

    public static String TAG_FILE_NAME = "file_name";

    private String fileName;

    EditText fileEdit;
    File file;
    boolean editable = false;
    boolean startapp = false;

    MenuItem editSaveMenuButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.giticonabf1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fileEdit = (EditText) findViewById(R.id.editFileText);
        fileEdit.setTag(fileEdit.getKeyListener());
        fileEdit.setKeyListener(null);

        Bundle extras = getIntent().getExtras();
        fileName = extras.getString(TAG_FILE_NAME);
        String res = "";
        try {
            file = new File(fileName);
            setTitle(file.getName());
            FileInputStream fin = new FileInputStream(file);
            res = streamToString(fin);
            fin.close();

            getSupportActionBar().setTitle(file.getName());
        } catch (FileNotFoundException e) {
            Log.e("edit file activity", "File not found: " + e.toString());
            this.finish();
        } catch (IOException e) {
            Log.e("edit file activity", "Can not read file: " + e.toString());
            this.finish();
        }
        fileEdit.setText(res);

    }

    public void onEditSaveButtonClick() {
       editable = !editable;
       if (editable) {
           editSaveMenuButton.setIcon(android.R.drawable.ic_menu_save);
           fileEdit.setKeyListener((KeyListener) fileEdit.getTag());
           String displayString = "Now you can edit";
           Toast msg = Toast.makeText(getBaseContext(), displayString,
                   Toast.LENGTH_LONG);
           msg.show();
       } else {
           editSaveMenuButton.setIcon(android.R.drawable.ic_menu_edit);
           try {
               FileUtils.writeStringToFile(file, fileEdit.getText().toString());
               String displayString = "File was saved";
               Toast msg = Toast.makeText(getBaseContext(), displayString,
                       Toast.LENGTH_LONG);
               msg.show();
           } catch (IOException e) {
               Log.e("edit file activity", "saving failed: " + e.toString());
           }
           fileEdit.setKeyListener(null);
       }
    }

    public void showDiscardDialog() {
        AlertDialog discardDialog;
        AlertDialog.Builder blder = new AlertDialog.Builder(this);
        blder.setTitle("Discard changes?");

        // discard
        blder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                continueDiscard();
                dialog.dismiss();
            }
        });

        // cancel
        blder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        discardDialog = blder.create();
        discardDialog.show();
    }

    public void continueDiscard() {
        if (startapp) {
            try {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                Uri uri = Uri.parse(fileName);
                intent.setDataAndType(uri, "text/plain");
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No edit apps", Toast.LENGTH_SHORT).show();
            }
        } else {
            this.finish();
        }
    }

    private String streamToString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            Log.e("edit file activity", "Can not read file:" + e.toString());
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editfile, menu);
        editSaveMenuButton = menu.findItem(R.id.action_editfile);

        return true;
    }

// **** КНОПОЧКИ ****
    // обработчик кнопочек меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_editfile:
                onEditSaveButtonClick();
                return true;
            case R.id.action_editwithapp:
                startapp = true;
                if (editable) {
                    showDiscardDialog();
                } else {
                    continueDiscard();
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!editable) super.onBackPressed();
        else {
            startapp = false;
            showDiscardDialog();
        }
    }

    @Override
    protected void onActivityResult(int rcode, int rescode, Intent res_intent) {
        this.finish();
    }

}
