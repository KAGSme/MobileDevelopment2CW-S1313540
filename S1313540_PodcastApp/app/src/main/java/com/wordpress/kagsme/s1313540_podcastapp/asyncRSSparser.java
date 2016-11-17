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

    public AsyncRSSparser(Context context, String podcastUrl)
    {
        appContext = context;
        podcastUrlRSS = podcastUrl;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected ArrayList<EpisodeDataItem> doInBackground(String... params)
    {
        PodcastRSSparser pParser = new PodcastRSSparser();
        try
        {
            pParser.ParseRSSData(podcastUrlRSS);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return pParser.geteDataItems();
    }

    @Override
    protected void onPostExecute(ArrayList<EpisodeDataItem> result)
    {
        Toast.makeText(appContext, "Parsing finished", Toast.LENGTH_SHORT).show();
    }
}

