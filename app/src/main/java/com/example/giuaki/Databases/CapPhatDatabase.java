package com.example.giuaki.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.giuaki.Entities.CapPhat;

import java.util.ArrayList;
import java.util.List;

public class CapPhatDatabase extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GiuaKi";

    // Table name: Note.
    private static final String TABLE_NAME = "CAPPHAT";

    public static final String COLUMN_ID ="ID";
    public static final String COLUMN_SOPHIEU ="SOPHIEU";
    public static final String COLUMN_NGAYCAP = "NGAYCAP";
    public static final String COLUMN_MAVPP = "MAVPP";
    public static final String COLUMN_MANV = "MANV";
    public static final String COLUMN_SOLUONG = "SOLUONG";

    public CapPhatDatabase(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String script = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
//                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_SOPHIEU + " TEXT PRIMARY KEY,"
                + COLUMN_NGAYCAP + " TEXT NOT NULL,"
                + COLUMN_MAVPP + "TEXT NOT NULL,"
                + COLUMN_MANV + "TEXT NOT NULL,"
                + COLUMN_SOLUONG + "INTEGER NOT NULL)";
        // Execute script.
        db.execSQL(script);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate
        onCreate(db);
    }

    public void insert(CapPhat capPhat){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOPHIEU, capPhat.getSoPhieu());
        values.put(COLUMN_NGAYCAP, capPhat.getNgayCap());
        values.put(COLUMN_MAVPP, capPhat.getMaVpp());
        values.put(COLUMN_MANV, capPhat.getMaNv());
        values.put(COLUMN_SOLUONG, capPhat.getSl());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
    }

    public List<CapPhat> select(){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_SOPHIEU,
                COLUMN_NGAYCAP,
                COLUMN_MAVPP,
                COLUMN_MANV,
                COLUMN_SOLUONG
        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder = PhongBanDatabase.COLUMN_ID + " DESC";
        String sortOrder = null;

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<CapPhat> list_capphat = new ArrayList<>();

        while(cursor.moveToNext()){
            list_capphat.add(new CapPhat(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            ));
        }

        return list_capphat;
    }
}
