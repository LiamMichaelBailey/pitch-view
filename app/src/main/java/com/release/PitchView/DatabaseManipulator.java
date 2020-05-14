package com.release.PitchView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/* Liam Bailey
*  SQLite database to store sessions and methods to manipulate db.*/
public class DatabaseManipulator {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "newtable";
    private static Context context;
    static SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    // column key added to implement delete method.
    private static final String KEY_ID = "id";

    private static final String INSERT = "insert into " + TABLE_NAME
            + " (title,notes,image) values (?,?,?)";

    public DatabaseManipulator(Context context) {
        DatabaseManipulator.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        DatabaseManipulator.db = openHelper.getWritableDatabase();
        this.insertStmt = DatabaseManipulator.db.compileStatement(INSERT);
    }

    public long insert(String title, String notes, String image)
    {
        this.insertStmt.bindString(1, title);
        this.insertStmt.bindString(2, notes);
        this.insertStmt.bindString(3, image);
        return this.insertStmt.executeInsert();
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }


    public List<String[]> selectAll() {
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"id", "title", "notes", "image"}, null, null, null, null, "");
        int x = 0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1 = new String[]{cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)};
                list.add(b1);
                x++;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return list;
    }

    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, title TEXT, notes TEXT, image TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            DATABASE_VERSION = newVersion;
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    // This method deletes a session by id.
    public void deleteEntry(int selection)
    {
        db.delete(TABLE_NAME, KEY_ID + "=" + selection, null);
    }
}
