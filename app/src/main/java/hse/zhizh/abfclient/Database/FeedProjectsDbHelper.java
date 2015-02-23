package hse.zhizh.abfclient.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                    ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_VISIBILITY +  TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL + TEXT_TYPE + COMMA_SEP +
                    ProjectsContract.FeedProjects.COLUMN_NAME_SSH_URL + TEXT_TYPE+ ")";
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
    public long addProject(String project_id,String fullname, String name, String ssh_url,String git_url,String visibility)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID,project_id );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME,fullname );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_NAME,name );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_SSH_URL,ssh_url );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,git_url );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_VISIBILITY,visibility );

        long newRowId;
        newRowId = db.insert(
                ProjectsContract.FeedProjects.TABLE_NAME,
                "null",
                values);
        return newRowId;
    }


    //считывает проекты, возвращает последний
    public long readProjects(){
        SQLiteDatabase db = this.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                ProjectsContract.FeedProjects._ID,
                ProjectsContract.FeedProjects.COLUMN_NAME_VISIBILITY,
                ProjectsContract.FeedProjects.COLUMN_NAME_NAME,
                ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProjectsContract.FeedProjects._ID + " DESC";

        Cursor c = db.query(
                ProjectsContract.FeedProjects.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToLast();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(ProjectsContract.FeedProjects._ID)
        );
        return itemId;
    }

    //Удаляет проект по его project_id
   public void deleteProject(String project_id){
       SQLiteDatabase db = this.getReadableDatabase();

       String[] projection = {
               ProjectsContract.FeedProjects._ID,
               ProjectsContract.FeedProjects.COLUMN_NAME_VISIBILITY,
               ProjectsContract.FeedProjects.COLUMN_NAME_NAME,
               ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID
       };
       String selection = ProjectsContract.FeedProjects.COLUMN_NAME_PROJECT_ID + " IS ?";
       String[] selectionArgs = {project_id};

       db.delete(ProjectsContract.FeedProjects.TABLE_NAME, selection, selectionArgs);
   }


    //Обновляет ряд с project_id, возвращает кол-во обноленных записей
    public int updateProject(String project_id,String fullname, String name, String ssh_url,String git_url,String visibility){
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();;
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_FULLNAME,fullname );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_NAME,name );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_SSH_URL,ssh_url );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_GIT_URL,git_url );
        values.put(ProjectsContract.FeedProjects.COLUMN_NAME_VISIBILITY,visibility );


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