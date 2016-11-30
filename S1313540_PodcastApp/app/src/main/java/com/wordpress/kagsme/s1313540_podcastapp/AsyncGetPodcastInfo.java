package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;

public class AsyncGetPodcastInfo  extends AsyncTask<String, Integer, PodcastDataItem>
{
    private Context appContext;
    private String podcastUrlRSS;
    private PodcastInfoDBMgr dbMgr;

    public AsyncGetPodcastInfo(Context context, String podcastUrl, PodcastInfoDBMgr databaseMgr)
    {
        appContext = context;
        podcastUrlRSS = podcastUrl;
        dbMgr = databaseMgr;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected PodcastDataItem doInBackground(String... params)
    {
        PodcastRSSparser pParser = new PodcastRSSparser();
        try
        {
            pParser.ParseRSSData(podcastUrlRSS, true);
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
        Toast.makeText(appContext, "Parsing finished", Toast.LENGTH_SHORT).show();
        dbMgr.addPodcastInfo(result);
        //dbMgr.onUpgrade(dbMgr.getWritableDatabase(), 1, 2);
    }
}

