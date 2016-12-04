package com.wordpress.kagsme.s1313540_podcastapp;

import android.media.Image;
import android.util.Log;

public class PodcastDataItem {

    //Declare Variables--------------------------------------------------------

    private int podcastID;
    private String podcastTitle;
    private String podcastDesc;
    private String podcastLink;
    private String podcastImageLink;
    private String podcastImageFName;

    //Declare Constructors------------------------------------------------------

    public PodcastDataItem(String pTitle,String pDesc,String pLink){
        podcastTitle = pTitle;
        podcastDesc = pLink;
        podcastLink = pDesc;
    }

    public PodcastDataItem(){
        podcastID = 0;
        podcastTitle = "";
        podcastDesc = "";
        podcastLink = "";
        podcastImageLink = "";
        podcastImageFName = "";
    }

    //Getters and setters-------------------------------------------------------
    public int getPodcastID(){
        return this.podcastID;
    }

    public String getPodcastTitle(){
        return this.podcastTitle;
    }

    public String getPodcastDesc(){
        return this.podcastDesc;
    }

    public String getPodcastLink(){
        return this.podcastLink;
    }

    public String getPodcastImageLink(){
        return this.podcastImageLink;
    }

    public String getPodcastImageFName(){
        if(this.podcastImageFName == "") createImageFName(this.podcastImageLink);
        return this.podcastImageFName;
    }

    public void setPodcastID(int value){
        this.podcastID = value;
    }

    public void setPodcastTitle(String value){
        this.podcastTitle = value;
    }

    public void setPodcastDesc(String value){
        this.podcastDesc = value;
    }

    public void setPodcastLink(String value){
        this.podcastLink = value;
    }

    public void setPodcastImageLink(String value){
        this.podcastImageLink = value;
        createImageFName(this.podcastImageLink);
    }

    public void setPodcastImageFName(String value){
        this.podcastImageFName = value;
    }

    private void createImageFName(String link){
        String[] parts = link.split("/");
        String tmp = parts[parts.length-1];
        setPodcastImageFName(tmp);
    }
}
