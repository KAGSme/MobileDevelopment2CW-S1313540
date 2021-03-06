package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class AsyncRSSparser extends AsyncTask<String, Integer, ArrayList<EpisodeDataItem>>
{
    private Context appContext;
    private String podcastUrlRSS;
    private boolean isSuccessful = false;

    private sendEDataItemsListener mListener;

    public AsyncRSSparser(Context context, String podcastUrl)
    {
        appContext = context;
        podcastUrlRSS = podcastUrl;
    }

    @Override
    protected void onPreExecute(){
        //Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ArrayList<EpisodeDataItem> doInBackground(String... params)
    {
        PodcastRSSparser pParser = new PodcastRSSparser(appContext);
        try
        {
            isSuccessful = pParser.ParseRSSData(podcastUrlRSS, false);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            isSuccessful = false;
        }
        return pParser.geteDataItems();
    }

    @Override
    protected void onPostExecute(ArrayList<EpisodeDataItem> result)
    {
        //Toast.makeText(appContext, "Parsing finished", Toast.LENGTH_SHORT).show();
        if(!isSuccessful)Toast.makeText(appContext, "Error: Retrieving RSS feed, please check internet connection!", Toast.LENGTH_LONG).show();
        this.mListener = (sendEDataItemsListener) appContext;
        this.mListener.SendEDataItem(result);
    }

    public interface sendEDataItemsListener{
        public void SendEDataItem(ArrayList<EpisodeDataItem> eDatas);
    }
}

//Authored by Kieran Anthony Gallagher S1313540