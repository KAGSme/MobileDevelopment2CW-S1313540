package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
//very similar to labs with some new classes
public class PodcastInfoDBMgr extends SQLiteOpenHelper{

    private static final int DB_VER = 1;
    private static String DB_PATH = "/data/data/com.wordpress.kagsme.s1313540_podcastapp/databases/";
    private static final String DB_NAME = "savedPodcasts.s3db";
    private static final String TBL_PODCASTINFO = "podcastInfo";

    public static final String COL_PODCASTID = "podcastID";
    public static final String COL_PODCASTTITLE = "podcastTitle";
    public static final String COL_PODCASTDESC = "podcastDesc";
    public static final String COL_PODCASTRSSLINK = "podcastRSSLink";
    public static final String COL_PODCASTIMAGELINK = "podcastImageLink";

    private final Context appContext;

    public PodcastInfoDBMgr(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.appContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_PODCASTINFO_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_PODCASTINFO + "("
                + COL_PODCASTID + " INTEGER PRIMARY KEY,"
                + COL_PODCASTTITLE + " TEXT,"
                + COL_PODCASTDESC + " TEXT,"
                + COL_PODCASTRSSLINK + " TEXT,"
                + COL_PODCASTIMAGELINK + " TEXT" + ")";
        db.execSQL(CREATE_PODCASTINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_PODCASTINFO);
            onCreate(db);
        }
    }
    //checks if dbExists, if not copy it from assets
    public void dbCreate() throws IOException{
        boolean dbExist = dbCheck();
        if(!dbExist)
        {
            this.getReadableDatabase();
            try
            {
                copyDBFromAssets();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }
    //checks if db exists
    static private boolean dbCheck(){
        SQLiteDatabase db = null;

        try
        {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
        }
        catch (SQLiteException e)
        {
            Log.e("SQLiteHelper", "Database not Found");
        }

        if(db != null){

            db.close();

        }

        return db != null ? true : false;
    }
    //copies db from assets folder
    private void copyDBFromAssets() throws IOException {

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try
        {
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0)
            {
                dbOutput.write(buffer, 0, length);
            }

            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        }
        catch(IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }
    //adds podcast item to database
    public  void addPodcastInfo(PodcastDataItem pDataItem){
        if(pDataItem != null) {
            if(pDataItem.getPodcastTitle() != "")
            {
                ContentValues values = new ContentValues();
                values.put(COL_PODCASTTITLE, pDataItem.getPodcastTitle());
                values.put(COL_PODCASTDESC, pDataItem.getPodcastDesc());
                values.put(COL_PODCASTRSSLINK, pDataItem.getPodcastLink());
                values.put(COL_PODCASTIMAGELINK, pDataItem.getPodcastImageLink());
                values.put(COL_PODCASTIMAGELINK, pDataItem.getPodcastImageLink());

                SQLiteDatabase db = this.getWritableDatabase();

                db.insert(TBL_PODCASTINFO, null, values);
                Log.d("SQLiteHelper", "Added Podcast " + pDataItem.getPodcastTitle());
                db.close();
            }
        }
        else Log.e("SQLiteHelper", "Podcast item is null");
    }

    public PodcastDataItem findPodcastDataItem(String aPodcast)
    {
        String query = "select * FROM " + TBL_PODCASTINFO + " WHERE " + COL_PODCASTTITLE + " = \"" + aPodcast + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        PodcastDataItem pDataItem = new PodcastDataItem();

        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            pDataItem.setPodcastID(Integer.parseInt(cursor.getString(0)));
            pDataItem.setPodcastTitle(cursor.getString(1));
            pDataItem.setPodcastDesc(cursor.getString(2));
            pDataItem.setPodcastLink(cursor.getString(3));
            pDataItem.setPodcastImageLink(cursor.getString(4));
            cursor.close();
        }
        else
        {
            pDataItem = null;
        }
        db.close();
        return pDataItem;
    }
    //removes podcast item from database
    public boolean removePodcastDataItem(String aPodcast) {

        boolean result = false;

        String query = "Select * FROM " + TBL_PODCASTINFO + " WHERE " + COL_PODCASTTITLE + " =  \"" + aPodcast + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        PodcastDataItem podcastDataItem = new PodcastDataItem();

        if (cursor.moveToFirst()) {
            podcastDataItem.setPodcastID(Integer.parseInt(cursor.getString(0)));
            db.delete(TBL_PODCASTINFO, COL_PODCASTID + " = ?",
                    new String[] { String.valueOf(podcastDataItem.getPodcastID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    //returns arraylist that contains all items in the database table
    public ArrayList<PodcastDataItem> getAllPodcastDataItems(){

        String query = "Select * FROM " + TBL_PODCASTINFO;

        ArrayList<PodcastDataItem> pDataItems = new ArrayList<PodcastDataItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            if(cursor != null) {
                PodcastDataItem pDataItem = new PodcastDataItem();
                pDataItem.setPodcastID(Integer.parseInt(cursor.getString(0)));
                pDataItem.setPodcastTitle(cursor.getString(1));
                pDataItem.setPodcastDesc(cursor.getString(2));
                pDataItem.setPodcastLink(cursor.getString(3));
                pDataItem.setPodcastImageLink(cursor.getString(4));
                pDataItems.add(pDataItem);
            }
            cursor.moveToNext();
        }
        if(!cursor.moveToFirst())cursor.close();
        db.close();
        return pDataItems;
    }

}
//Authored by Kieran Anthony Gallagher S1313540