package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;

public class AsyncDownloadFile extends AsyncTask<String, Integer, Boolean> {

    private Context appContext;
    private String downloadLink;
    private String fileName;
    DownloadsMgr dMgr;

    showDialogListener mListener;

    DialogFragment dF;
    DownloadProgressDialogue dPDialog;

    public AsyncDownloadFile(Context context, String dLink, String fName, showDialogListener l){
        appContext = context;
        downloadLink = dLink;
        fileName = fName;
        dMgr = new DownloadsMgr(appContext);
        setShowDialogListener(l);

        dPDialog = new DownloadProgressDialogue();

        dPDialog.setCancelable(true);
        dPDialog.setOnCancelListener(new DownloadProgressDialogue.onCancelListener(){
            @Override
            public void OnCancel() {
                cancel(true);
            }
        });
    }

    @Override
    public void onCancelled(){
        dMgr.cancel();
    }

    @Override
    protected void onPreExecute(){
        //Toast.makeText(appContext, "Download started!", Toast.LENGTH_LONG).show();
        Log.d("s1313540", "Async Download Started");

        mListener.showDialog(dPDialog);
    }

    @Override
    protected void onProgressUpdate(Integer...progress){
        dPDialog.setProgress(progress[0]);
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        return dMgr.DownloadAudioFile(downloadLink, fileName, new DownloadsMgr.publishProgressInterface() {
            @Override
            public void PublishProgress(int progress){
                publishProgress(progress);
            }
        });
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        dPDialog.dismiss();
        if(result) {
            dMgr.PlayDownloadInExternalApp(fileName);
            ToneGenerator tGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tGenerator.startTone(ToneGenerator.TONE_CDMA_CONFIRM);

            Toast.makeText(appContext, "Download Finished!", Toast.LENGTH_SHORT).show();
            Log.d("s1313540", "Async Download Finished");
        }
        else
        {
            Toast.makeText(appContext, "Error: Download Failed", Toast.LENGTH_SHORT).show();
            Log.d("s1313540", "Download Failed");
        }
    }

    public interface showDialogListener{
        public void showDialog(DialogFragment dF);
    }

    public void setShowDialogListener(showDialogListener listener){
        mListener = listener;
    }
}
