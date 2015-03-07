package hse.zhizh.abfclient.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hse.zhizh.abfclient.Model.Project;

/**
 * Created by EvgenyMac on 23.02.15.
 */
public class FeedProjectsDbHelper extends SQLiteOpenHelper {
    Context mContext;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProjectsContract.FeedProjects.TABLE_NAME + " (" +
                    ProjectsContract.FeedProjects._ID + " INTEGER PRIMARY KEY," +
                    ProjectsContract.FeedProjects.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID + "INTEGER" + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION +  TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID + "INTEGER"+COMMA_SEP+
                    ProjectsContract.FeedProjects.COLUMN_NAME_IS_LOCAL + " BOOLEAN "+ ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProjectsContract.FeedProjects.TABLE_NAME;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedProjects.db";

    public FeedProjectsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Добавление проекта в базу
    public long addProject(Project project,boolean is_local)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID,project.getId() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME,project.getFullname() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_NAME,project.getName() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID,project.getOwnerId() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,project.getGitUrl() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION,project.getDescription() );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_IS_LOCAL,is_local );

        long newRowId;
        newRowId = db.insert(
                ProjectsContract.FeedProjects.TABLE_NAME,
                "null",
                values);
        return newRowId;
    }

    private String[] toStringArrConverterHelper(int[] ids){
        String str="";
        for(int el:ids){
            str+=el+",";
        }
        return new String[]{str.substring(0, str.length()-1)};
    }

    //ids=new String[] {"2,3"} as example
    public HashMap<Integer,Project> readProjectsWithIds(int[] int_ids){
        String[] ids = toStringArrConverterHelper(int_ids);
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                ProjectsContract.FeedProjects._ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION,
                ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,
                ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_NAME,
                ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME
        };
        String sortOrder =
                ProjectsContract.FeedProjects._ID + " DESC";

        Cursor cursor = db.query(ProjectsContract.FeedProjects.TABLE_NAME,
                projection, "id IN (?)", ids, null, null, sortOrder);
        cursor.moveToFirst();

        HashMap<Integer,Project> projects = new HashMap<Integer,Project>();


        while(cursor.moveToNext()){
            Integer project_id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID));
            String git_url=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL));
            String fullname=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME));
            String description=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION));
            String name =cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_NAME));
            Integer owner_id=cursor.
                    getInt(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID));
            Project to_add = new Project(project_id,name,fullname,git_url,description,owner_id, true);

            projects.put(project_id,to_add);
        }

        return projects;
    }

    //считывает проекты, возвращает последний
    public ArrayList<Project> readProjects(){
        SQLiteDatabase db = this.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                ProjectsContract.FeedProjects._ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION,
                ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,
                ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_NAME,
                ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProjectsContract.FeedProjects._ID + " DESC";

        Cursor cursor = db.query(
                ProjectsContract.FeedProjects.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        ArrayList<Project> projects = new ArrayList<Project>();
        while(cursor.moveToNext()){
            Integer project_id = Integer.parseInt(cursor.getString(
                    cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID)));
            String git_url=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL));
            String fullname=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME));
            String description=cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION));
            String name =cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_NAME));
            Integer owner_id=Integer.parseInt(cursor.
                    getString(cursor.getColumnIndexOrThrow(ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID)));
            Project to_add = new Project(project_id,name,fullname,git_url,description,owner_id, true);

            projects.add(to_add);
        }
        return projects;
    }



    //Удаляет проект по его project_id
   public void deleteProject(String project_id){
       SQLiteDatabase db = this.getReadableDatabase();

       String[] projection = {
               ProjectsContract.FeedProjects._ID,
               ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION,
               ProjectsContract.FeedProjects.COLUMN_NAME_NAME,
               ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID
       };
       String selection = ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID + " IS ?";
       String[] selectionArgs = {project_id};

       db.delete(ProjectsContract.FeedProjects.TABLE_NAME, selection, selectionArgs);
   }

    public void deleteAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ProjectsContract.FeedProjects.TABLE_NAME, null, null);
    }


    //Обновляет ряд с project_id, возвращает кол-во обноленных записей
    public int updateProject(String project_id,String fullname, String name, String description,String git_url,String owner_id,boolean is_local){
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME,fullname );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_NAME,name );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_OWNER_ID,owner_id );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,git_url );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_DESCRIPTION,description );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_IS_LOCAL,is_local );


// Which row to update, based on the ID
        String selection = ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID + " IS ?";
        String[] selectionArgs = { project_id};

        int count = db.update(
                ProjectsContract.FeedProjects.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

}