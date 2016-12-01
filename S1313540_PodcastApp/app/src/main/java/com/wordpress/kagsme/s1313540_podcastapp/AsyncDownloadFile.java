package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;

public class AsyncDownloadFile extends AsyncTask<String, Integer, Boolean> {

    private Context appContext;
    private String downloadLink;
    private String fileName;

    public AsyncDownloadFile(Context context, String dLink, String fName){
        appContext = context;
        downloadLink = dLink;
        fileName = fName;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(appContext, "Download started!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        DownloadsMgr dMgr = new DownloadsMgr(appContext);
        return dMgr.downloadFile(downloadLink, fileName);
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        Toast.makeText(appContext, "Download Finish", Toast.LENGTH_SHORT).show();
    }
}
