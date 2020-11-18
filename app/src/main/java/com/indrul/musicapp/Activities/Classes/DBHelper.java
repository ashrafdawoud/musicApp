package com.indrul.musicapp.Activities.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "music";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_Artist = "artist_name";
    public static final String CONTACTS_COLUMN_Image = "image";
    public static final String CONTACTS_COLUMN_Audio = "audio";
    public static final String CONTACTS_COLUMN_DownloadedAudio = "downloaded_audio";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table music " +
                        "(id integer primary key, name text,artist_name text,image text, audio text,downloaded_audio text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS music");
        onCreate(sqLiteDatabase);
    }
    public boolean insertContact (String name, String artist_name, String image, String audio,String downloaded_audio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("artist_name", artist_name);
        contentValues.put("image", image);
        contentValues.put("audio", audio);
        contentValues.put("downloaded_audio", downloaded_audio);
        db.insert("music", null, contentValues);
        return true;
    }

    public Cursor getData(String name) {
       /* SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from music where name="+"Эфемерный Окуловский Май", null );*/
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("music", new String[] { CONTACTS_COLUMN_ID,
                        CONTACTS_COLUMN_NAME, CONTACTS_COLUMN_Artist,CONTACTS_COLUMN_Image,CONTACTS_COLUMN_Audio,CONTACTS_COLUMN_DownloadedAudio }, CONTACTS_COLUMN_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("music",
                "name = ? ",
                new String[] { name });
    }

    public ArrayList<ArrayList<String>> getAllCotacts() {
        ArrayList<ArrayList<String>> all_records = new ArrayList<ArrayList<String>>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> artist_name = new ArrayList<String>();
        ArrayList<String> image = new ArrayList<String>();
        ArrayList<String> audio = new ArrayList<String>();
        ArrayList<String> downloaded_audio = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from music", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            name.add(res.getString(res.getColumnIndex("name")));
            artist_name.add(res.getString(res.getColumnIndex("artist_name")));
            image.add(res.getString(res.getColumnIndex("image")));
            audio.add(res.getString(res.getColumnIndex("audio")));
            downloaded_audio.add(res.getString(res.getColumnIndex("downloaded_audio")));
            res.moveToNext();
        }
        if (name.size()!=0)
        all_records.add(name);
        if (artist_name.size()!=0)
            all_records.add(artist_name);
        if (image.size()!=0)
            all_records.add(image);
        if (audio.size()!=0)
            all_records.add(audio);
        if (downloaded_audio.size()!=0)
            all_records.add(downloaded_audio);

        return all_records;
    }
}
