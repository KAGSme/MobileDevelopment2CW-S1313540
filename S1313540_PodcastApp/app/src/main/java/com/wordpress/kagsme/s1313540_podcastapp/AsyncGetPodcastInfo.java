package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;
//asyncTask class that specialises in retrieving information about a podcast
public class AsyncGetPodcastInfo  extends AsyncTask<String, Integer, PodcastDataItem>
{
    private Context appContext;
    private String podcastUrlRSS;
    private PodcastInfoDBMgr dbMgr;
    private PodcastsFragment pFragment;
    private boolean isSuccessful = false;

    public AsyncGetPodcastInfo(Context context, String podcastUrl, PodcastInfoDBMgr databaseMgr, PodcastsFragment pF)
    {
        appContext = context;
        podcastUrlRSS = podcastUrl;
        dbMgr = databaseMgr;
        pFragment = pF;
    }

    @Override
    protected void onPreExecute(){
        //Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected PodcastDataItem doInBackground(String... params)
    {
        PodcastRSSparser pParser = new PodcastRSSparser(appContext);
        try
        {
            isSuccessful = pParser.ParseRSSData(podcastUrlRSS, true);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return pParser.getpDataItem();
    }

    @Override
    protected void onPostExecute(PodcastDataItem result)
    {
        //add podcast to database then refresh list
        if(isSuccessful)Toast.makeText(appContext, "Added Podcast", Toast.LENGTH_SHORT).show();
        else Toast.makeText(appContext, "Error Adding Podcast", Toast.LENGTH_SHORT).show();
        dbMgr.addPodcastInfo(result);
        pFragment.RetrieveTable();
        pFragment.DisplayPodcastDatabaseTableAsList();
    }
}

//Authored by Kieran Anthony Gallagher S1313540