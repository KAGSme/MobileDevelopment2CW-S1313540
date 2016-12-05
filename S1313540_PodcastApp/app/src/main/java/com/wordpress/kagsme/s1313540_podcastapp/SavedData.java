package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.SharedPreferences;
import android.util.Log;

public class SavedData {

    //Declare Constructors------------------------------------------------------

    SharedPreferences sharedPreferences;

    private String favourite;
    private String lastPlayed;

    //Getters and setters-------------------------------------------------------

    public String getFavourite(){
        return this.favourite;
    }

    public String getLastPlayed(){
        return this.lastPlayed;
    }

    public void setFavourite(String value){
        this.favourite = value;
    }

    public void setLastPlayed(String value){
        this.lastPlayed = value;
    }

    public SavedData(SharedPreferences sharedPrefs){
        setFavourite("");
        setLastPlayed("");

        try
        {
            this.sharedPreferences = sharedPrefs;
        }
        catch (Exception e)
        {
            Log.e("s1313540", "Pref Manager is NULL");
        }
        //setDefaultPrefs();
    }

    public void savePreferences(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setDefaultPrefs(){
        savePreferences("favourite", "");
        savePreferences("lastPlayed", "");
    }
}
