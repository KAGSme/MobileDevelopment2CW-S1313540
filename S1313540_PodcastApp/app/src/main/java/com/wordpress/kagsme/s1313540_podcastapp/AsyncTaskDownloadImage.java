package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncTaskDownloadImage  extends AsyncTask<String, Integer, Boolean> {

    private Context appContext;
    private String downloadLink;
    private String fileName;
    DownloadsMgr dMgr;

    public AsyncTaskDownloadImage(Context context, String dLink, String fName){
        appContext = context;
        downloadLink = dLink;
        fileName = fName;
        dMgr = new DownloadsMgr(appContext);
    }

    @Override
    public void onCancelled(){
        dMgr.cancel();
    }

    @Override
    protected void onPreExecute(){
        Log.d("s1313540", "Async Download Started");
    }

    @Override
    protected void onProgressUpdate(Integer...progress){
        //update
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        return DownloadsMgr.DownloadImageFile(appContext, downloadLink, fileName);
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        if(result) {
            Log.d("s1313540", "Async Download Image Finished");
        }
        else
        {
            Log.d("s1313540", "Async Download Image Failed");
        }
    }
}
//Authored by Kieran Anthony Gallagher S1313540