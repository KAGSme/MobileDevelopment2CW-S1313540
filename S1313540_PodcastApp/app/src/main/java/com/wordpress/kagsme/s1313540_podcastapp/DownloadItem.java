package com.wordpress.kagsme.s1313540_podcastapp;

import android.graphics.Bitmap;

public class DownloadItem {

    //Declare Variables--------------------------------------------------------

    private String downloadTitle;
    private String downloadLength;
    private String downloadFileName;
    private Bitmap bitmapCover;

    //Declare Constructors------------------------------------------------------

    public DownloadItem(String dTitle,String dLength,String dFName){
        downloadTitle = dTitle;
        downloadLength = dLength;
        downloadFileName = dFName;
    }

    public DownloadItem(){
        downloadTitle = "";
        downloadLength = "";
        downloadFileName = "";
    }

    //Getters and setters-------------------------------------------------------

    public String getDownloadTitle(){
        return this.downloadTitle;
    }

    public String getDownloadLength(){
        return this.downloadLength;
    }

    public String getDownloadFileName(){
        return this.downloadFileName;
    }

    public Bitmap getBitmapCover(){
        return this.bitmapCover;
    }

    public void setDownloadTitle(String value){
        this.downloadTitle = value;
    }

    public void setDownloadDuration(String value){
        long durationMs = Long.parseLong(value);
        long duration = durationMs/1000;
        long h = duration / 3600;
        long m = (duration - h * 3600) / 60;
        long s = duration - (h * 3600 + m * 60);
        this.downloadLength = "Duration " + Long.toString(h) + ":" + Long.toString(m) + ":" + Long.toString(s);
    }

    public void setDownloadFileName(String value){
        this.downloadFileName = value;
    }

    public void setBitmapCover(Bitmap value){
        bitmapCover = value;
    }
}
