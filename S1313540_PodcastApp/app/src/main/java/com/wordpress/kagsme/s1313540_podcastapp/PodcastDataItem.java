package com.wordpress.kagsme.s1313540_podcastapp;

public class PodcastDataItem {

    //Declare Variables--------------------------------------------------------

    private String podcastTitle;
    private String podcastDesc;
    private String podcastLink;

    //Declare Constructors------------------------------------------------------

    public PodcastDataItem(String pTitle,String pDesc,String pLink){
        podcastTitle = pTitle;
        podcastDesc = pLink;
        podcastLink = pDesc;
    }

    public PodcastDataItem(){
        podcastTitle = "";
        podcastDesc = "";
        podcastLink = "";
    }

    //Getters and setters-------------------------------------------------------

    public String getPodcastTitle(){
        return this.podcastTitle;
    }

    public String getPodcastDesc(){
        return this.podcastDesc;
    }

    public String getPodcastLink(){
        return this.podcastLink;
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
}
