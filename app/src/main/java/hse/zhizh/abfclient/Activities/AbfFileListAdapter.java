package hse.zhizh.abfclient.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hse.zhizh.abfclient.Model.AbfFile;
import hse.zhizh.abfclient.R;

/**
 * .ABF.YML FileList Adapter
 *
 * Created by E-Lev on 10.03.2015.
 */
public class AbfFileListAdapter extends ArrayAdapter<AbfFile> {

        private ArrayList<AbfFile> abfFilesList;

        private ActionBarActivity activity;

        public AbfFileListAdapter(ActionBarActivity activity, int textViewResourceId,
                               List<AbfFile> abfList) {

            super(activity, textViewResourceId, abfList);

            this.activity = activity;
            this.abfFilesList = new ArrayList<AbfFile>();
            this.abfFilesList.addAll(abfList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.d("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.item_abfymllist, null);

                convertView.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;

                        AbfFile abffile = (AbfFile)cb.getTag();
                        Toast.makeText(activity.getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();

                    }
                });

            }
            else {

            }

            AbfFile abfFile = abfFilesList.get(position);
            ((CheckBox)convertView).setText(abfFile.getName());
            ((CheckBox)convertView).setChecked(false);
            convertView.setTag(abfFile);

            return convertView;

        }

    }