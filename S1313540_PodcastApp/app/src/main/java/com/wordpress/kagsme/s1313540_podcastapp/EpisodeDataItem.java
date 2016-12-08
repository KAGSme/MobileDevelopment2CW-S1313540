package com.wordpress.kagsme.s1313540_podcastapp;

public class EpisodeDataItem {
    private String episodeTitle;
    private String episodeDesc;
    private String episodeLink;
    private String filename = "";

    //Declare Constructors------------------------------------------------------

    public EpisodeDataItem(String eTitle, String eDesc, String eLink){
        episodeTitle = eTitle;
        episodeDesc = eLink;
        episodeLink = eDesc;
    }

    public EpisodeDataItem(){
        episodeTitle = "";
        episodeDesc = "";
        episodeLink = "";
    }

    //Declare Getters & Setters-------------------------------------------------

    public String getEpisodeTitle(){
        return this.episodeTitle;
    }

    public String getEpisodeDesc(){
        return this.episodeDesc;
    }

    public String getEpisodeLink(){
        return this.episodeLink;
    }

    public String getFilename(){
        return this.filename;
    }

    public void setEpisodeTitle(String value){
        this.episodeTitle = value;
    }

    public void setEpisodeDesc(String value){
        this.episodeDesc = value;
    }

    public void setEpisodeLink(String value){
        //episode file name can be derived from link effectively, stuff must be removed though from string
        this.episodeLink = value;
        String[] parts = value.split("/");
        String tmp = parts[parts.length-1].replaceAll("[?]", ",");
        String[] otherParts = tmp.split(",");
        this.filename = otherParts[0];
    }
}
//Authored by Kieran Anthony Gallagher S1313540