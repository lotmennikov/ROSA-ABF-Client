package hse.zhizh.abfclient.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

import hse.zhizh.abfclient.Model.Repository;
import hse.zhizh.abfclient.R;
import hse.zhizh.abfclient.common.Settings;

/**
 * File browser fragment
 *
 *
 *
 */
public class ContentsFragment extends Fragment implements ProjectActivityEventListener {

    private ListView fileList;
    private File currentDir;

    private Repository repo;
    private File[] files;

    private View fragmentView;

    private Context context;

    public ContentsFragment() {
        setRepository();
    }

    public void setRepository() {
        repo = Settings.currentProject.getRepo();
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

        final ArrayAdapter<String> filesAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.contents_list_element, fileNames);
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
                    // ------------------------------------------------------------------------------------------------------------
                    // редактировать файл
                    File file = files[position];//new File(filesAdapter.getItem(position));
                    Intent fileEditIntent = new Intent(getActivity(), EditFileActivity.class);
                    Uri uri = Uri.fromFile(files[position]);
                    fileEditIntent.putExtra(EditFileActivity.TAG_FILE_NAME,
                            file.getAbsolutePath());
                    startActivity(fileEditIntent);
                    /*// редактирование файла другим приложением
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    Uri uri = Uri.fromFile(files[position]);
                    intent.setDataAndType(uri, "text/plain");
                    startActivity(intent);
                    // ------------------------------------------------------------------------------------------------------------
                    */
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_contents, container, false);
        fileList =  (ListView)fragmentView.findViewById(R.id.fileList);
        currentDir = repo.getDir();
        setFileList();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentDir == null && repo != null) {
            currentDir = repo.getDir();
            setFileList();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (fragmentView != null && !currentDir.getAbsolutePath().equals(repo.getDir().getAbsolutePath())) {
            currentDir = currentDir.getParentFile();
            setFileList();
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onBranchChanged() {
        return false;
    }

    @Override
    public boolean onRefreshed() {
        return false;
    }

    public void refresh() {
        if (currentDir.exists()) {
            setFileList();
        } else {
            currentDir = repo.getDir();
            setFileList();
        }
    }
}
