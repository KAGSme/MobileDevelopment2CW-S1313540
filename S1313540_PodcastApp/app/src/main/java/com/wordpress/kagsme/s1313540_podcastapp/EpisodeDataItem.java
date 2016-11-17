package com.wordpress.kagsme.s1313540_podcastapp;

public class EpisodeDataItem {
    private String episodeTitle;
    private String episodeDesc;
    private String episodeLink;

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

    public void setEpisodeTitle(String value){
        this.episodeTitle = value;
    }

    public void setEpisodeDesc(String value){
        this.episodeDesc = value;
    }

    public void setEpisodeLink(String value){
        this.episodeLink = value;
    }
}
