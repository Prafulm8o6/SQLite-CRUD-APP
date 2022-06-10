package com.example.sqlite_crud_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyDBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "studsDB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "stud_mst";
    public static final String COL_S_ID = "s_id";
    public static final String COL_S_NAME = "s_name";
    public static final String COL_S_DOB = "dob";

    public MyDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( "
                + COL_S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_S_NAME + " TEXT, "
                + COL_S_DOB + " TEXT "
                + " ); ";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public int Insert(String S_NAME, String S_DOB) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(this.COL_S_NAME, S_NAME);
        cv.put(this.COL_S_DOB, S_DOB);

        long i = db.insert(this.TABLE_NAME, null, cv);

        db.close();

        if (i != -1) {
            return 1;
        } else {
            return 0;
        }
    }

    public int Update(int sid, String S_NAME, String S_DOB) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(this.COL_S_NAME, S_NAME);
        cv.put(this.COL_S_DOB, S_DOB);

        long i = db.update(this.TABLE_NAME, cv, this.COL_S_ID + "=?", new String[]{String.valueOf(sid)});

        db.close();

        if (i != -1) {
            return 1;
        } else {
            return 0;
        }
    }

    public int Delete(int sid) {

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.delete(this.TABLE_NAME, this.COL_S_ID + "=?", new String[]{String.valueOf(sid)});

        db.close();

        if (i != -1) {
            return 1;
        } else {
            return 0;
        }
    }

    public JSONArray getArray() {
        SQLiteDatabase db = this.getWritableDatabase();

        JSONArray jsonArray = new JSONArray();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    JSONObject object = new JSONObject();
                    object.put("sid", cursor.getString(0).toString());
                    object.put("s_name", cursor.getString(1).toString());
                    object.put("s_dob", cursor.getString(2).toString());
                    jsonArray.put(object);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return jsonArray;
    }
}
