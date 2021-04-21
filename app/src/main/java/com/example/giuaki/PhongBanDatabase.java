package com.example.giuaki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class PhongBanDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GiuaKi";

    // Table name: Note.
    private static final String TABLE_NAME = "PHONGBAN";

    private static final String COLUMN_ID ="ID";
    private static final String COLUMN_MAPB ="MAPB";
    private static final String COLUMN_TENPB = "TENPB";

    public PhongBanDatabase(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String script = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + COLUMN_MAPB + " TEXT NOT NULL UNIQUE,"
                + COLUMN_TENPB + " TEXT NOT NULL" + ")";
        // Execute script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate
        onCreate(db);
    }

    public Cursor select(){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                PhongBanDatabase.COLUMN_MAPB,
                PhongBanDatabase.COLUMN_TENPB
        };

        // Filter results WHERE "mapb" = '......'
//        String selection = PhongBanDatabase.COLUMN_MAPB + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                PhongBanDatabase.COLUMN_ID + " DESC";

        Cursor cursor = db.query(
                PhongBanDatabase.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        return cursor;
    }

    public void insert(PhongBan phongban){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PhongBanDatabase.COLUMN_MAPB, phongban.getMapb());
        values.put(PhongBanDatabase.COLUMN_TENPB, phongban.getTenpb());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(PhongBanDatabase.TABLE_NAME, null, values);
    }
}
