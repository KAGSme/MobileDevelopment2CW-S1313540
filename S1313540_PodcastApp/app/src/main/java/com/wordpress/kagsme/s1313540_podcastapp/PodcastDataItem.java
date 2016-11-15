package com.wordpress.kagsme.s1313540_podcastapp;

/**
 * Created by kags1 on 13/11/2016.
 */

public class PodcastDataItem {

    private String episodeTitle;
    private String episodeDesc;
    private String episodeLink;

    public PodcastDataItem(String eTitle,String eDesc,String eLink){
        episodeTitle = eTitle;
        episodeDesc = eLink;
        episodeDesc = eDesc;
    }

    public String getEpisodeTitle(){
        return this.episodeTitle;
    }

    public String getEpisodeDesc(){
        return this.episodeDesc;
    }

    public String getEpisodeLink(){
        return this.episodeLink;
    }
}
