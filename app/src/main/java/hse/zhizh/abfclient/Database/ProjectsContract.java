package hse.zhizh.abfclient.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Class contract for database
 */
public final class ProjectsContract {
    //Пустой конструктор, для того чтобы невозможно было инстанцировать класс БД
    public ProjectsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedProjects implements BaseColumns {
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_NAME_PROJECT_ID = "project_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_FULLNAME = "fullname";
        public static final String COLUMN_NAME_GIT_URL = "git_url";
        public static final String COLUMN_NAME_OWNER_ID = "owner_id";
        public static final String COLUMN_NAME_IS_LOCAL = "is_local";

    }


}


