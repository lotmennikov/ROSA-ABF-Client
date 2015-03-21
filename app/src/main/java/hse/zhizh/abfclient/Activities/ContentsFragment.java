package hse.zhizh.abfclient.Activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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

    private CharSequence[] fileMenu = new CharSequence[] { "Add to stage" , "Delete" };

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
        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!files[position].isDirectory()) {
                    final File selectedFile = files[position];
                    AlertDialog fileDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(fileMenu, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    if (which == 0) {
                                        Log.d(Settings.TAG, "ContentsFragment" + " Add to stage file " + selectedFile.getName());
                                    }
                                    if (which == 1) {
                                        showDeleteDialog(selectedFile);
                                    }
                                }
                            });
                    fileDialog = builder.create();
                    fileDialog.show();

                }

                return true;
            }

        });

    }

    private void showDeleteDialog(File file) {

        AlertDialog deleteDialog;
        final File delFile = file;
        // проходим в другую папку
        AlertDialog.Builder blder = new AlertDialog.Builder(ContentsFragment.this.getActivity());
        blder.setTitle("Delete File?");

        // delete file
        blder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delFile.delete();
                setFileList();

                Log.d(Settings.TAG, "ContentsFragment" + " Deleting file " + delFile.getName());
                Toast.makeText(ContentsFragment.this.getActivity(), "File has been deleted", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        // cancel deletion
        blder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialog = blder.create();
        deleteDialog.show();

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

    public File getCurrentDir() {
        return currentDir;
    }
}
