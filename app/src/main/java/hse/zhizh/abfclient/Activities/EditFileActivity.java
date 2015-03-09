package hse.zhizh.abfclient.Activities;

/**
 * Created by omkol_000 on 09.03.2015.
 */

import android.app.Activity;

import android.util.Log;
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

public class EditFileActivity extends Activity {

    public static String TAG_FILE_NAME = "file_name";

    Button editFileBtn;
    EditText fileEdit;
    File file;
    boolean editable = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        fileEdit = (EditText) findViewById(R.id.editFileText);
        fileEdit.setTag(fileEdit.getKeyListener());
        fileEdit.setKeyListener(null);

        Bundle extras = getIntent().getExtras();
        String fileName = extras.getString(TAG_FILE_NAME);
        String res = "";
        try {
            file = new File(fileName);
            setTitle(file.getName());
            FileInputStream fin = new FileInputStream(file);
            res = streamToString(fin);
            fin.close();
        }
        catch(Exception e) {
            Log.e("edit file activity", "Some problem: " + e.toString());
        }
        fileEdit.setText(res);

        editFileBtn = (Button) findViewById(R.id.buttonEditSave);
        editFileBtn.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                   editable = !editable;
                   if (editable) {
                       editFileBtn.setText("Save");
                       //fileEdit.setEnabled(editable);
                       fileEdit.setKeyListener((KeyListener) fileEdit.getTag());
                       String displayString = "Now you can edit";
                       Toast msg = Toast.makeText(getBaseContext(), displayString,
                               Toast.LENGTH_LONG);
                       msg.show();
                   } else {
                       editFileBtn.setText("Edit");
                       try {
                           FileUtils.writeStringToFile(file, fileEdit.getText().toString());
                       } catch (IOException e) {
                           Log.e("edit file activity", "saving failed: " + e.toString());
                       }
                       fileEdit.setKeyListener(null);
                   }
               }
           }
        );
    }

    private String getStringFromFile(String filePath) {
        String res = "";
        try {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            res = streamToString(fin);
            fin.close();
        } catch (FileNotFoundException e) {
            Log.e("edit file activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("edit file activity", "Can not read file: " + e.toString());
        } catch (Exception e) {
            Log.e("edit file activity", "Some problem: " + e.toString());
        }
        return res;
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
        } catch (Exception e) {
            Log.e("edit file activity", "Some problem in StreamToString: " + e.toString());
        }
        return "";
    }
}
