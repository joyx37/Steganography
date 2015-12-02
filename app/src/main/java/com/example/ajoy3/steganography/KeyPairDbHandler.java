package com.example.ajoy3.steganography;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malabika on 11/28/2015.
 */
public class KeyPairDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "MySteganosInfo.db",
            TABLE_DOCS = "pairedDevicesInfo",
            KEY_ID = "id",
            KEY_PAIRER = "phonename",
            KEY_NICKNAME = "nickname",
            KEY_SHAREDKEY = "sharedkey";


    public KeyPairDbHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_DOCS + "(" + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+KEY_PAIRER
                + " TEXT NOT NULL," + KEY_NICKNAME + " TEXT NOT NULL," + KEY_SHAREDKEY + " TEXT NOT NULL" + ")";


//        Log.e("DATABSE HELPER", "the query is: " + CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);
        onCreate(db);
    }

    public void createPairInfoEntry(PairInformation _pairInformation ){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PAIRER,_pairInformation.get_nameofpairer());
        values.put(KEY_NICKNAME, _pairInformation.get_nickname());
        values.put(KEY_SHAREDKEY, _pairInformation.get_sharedkey());

        db.insert(TABLE_DOCS, null, values);
    }

    public void clearDatabase()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DOCS, null, null);
    }

    public int getPairedDeviceCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOCS, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public List<PairInformation> getAllMyPairedDeviceInfo(){

        List<PairInformation> myPairInfoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOCS, null);
        if(cursor.moveToFirst())
        {
            do{
                PairInformation _pInformation = new PairInformation(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                myPairInfoList.add(_pInformation);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return myPairInfoList;
    }

    public void testInitialization()
    {
        createPairInfoEntry(new PairInformation(1, "nexus 5", "malabika", "hsdfhshdgfygsdhfg"));
        createPairInfoEntry(new PairInformation(2, "nexus 5 -d", "malabika2", "dfrgghyhhjnghjgjdfgh"));
    }

}
