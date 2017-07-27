package com.example.pankaj.trackpath.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by pankaj on 7/26/17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackpath";
    private static final String TABLE_NAME = "coordinates";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_RAWDATA = "rawdata";

    private static final LatLng KEY_START = new LatLng(0,0);
    private static final LatLng KEY_END = new LatLng(0,0);
    private static final ArrayList<LatLng> KEY_LATLNG = new ArrayList<>();

    private static Context CONTEXT;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CONTEXT = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_RAWDATA + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEntry(Coordinates ll) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE,  ll.getDate());
        values.put(KEY_RAWDATA, ll.getRawdata());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public Coordinates getCoordinate(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                        KEY_ID,
                        KEY_DATE,
                        KEY_RAWDATA }, KEY_ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
            Coordinates ll = new Coordinates(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2));
//            cursor.getString(3),
//            cursor.getString(4),
//            cursor.getString(5),
//            cursor.getString(6));
    return ll;
    }

    // Getting All Coordinates
    public ArrayList<Coordinates> getAllCoordinates() {
        ArrayList<Coordinates> coordinatesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Coordinates ll = new Coordinates();
                ll.setId(Integer.parseInt(cursor.getString(0)));
                ll.setDate(cursor.getString(1));
                ll.setRawdata(cursor.getString(2));
                // Adding entry to list
                coordinatesList.add(ll);
            } while (cursor.moveToNext());
        }
        return coordinatesList;
    }

    // Updating single Coordinate
    public int updateCoordinate(Coordinates ll) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, ll.getDate());
        values.put(KEY_RAWDATA, ll.getRawdata());
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ll.getId()) });
    }

    // Deleting single Coordinate
    public void deleteCoordinate(Coordinates ll) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(ll.getId()) });
        db.close();
    }

    // Getting Coordinates Count
    public long getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

}