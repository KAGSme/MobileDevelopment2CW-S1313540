package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
//contains code snippets from https://developer.android.com/training/basics/data-storage/files.html, modified by Kieran Gallagher
//contains code snippets from http://stackoverflow.com/questions/7351940/how-can-i-download-audio-file-from-server-by-url, heavily modified by Kieran Gallagher
//This was used to help with downloading images http://stackoverflow.com/questions/29625655/best-way-to-download-image-from-url-and-save-it-into-internal-storage-memory
public class DownloadsMgr {

    private Context appContext;
    private boolean isCancel = false;

    public DownloadsMgr(Context context){
        appContext = context;
    }

    //code was obtained from http://stackoverflow.com/questions/7351940/how-can-i-download-audio-file-from-server-by-url
    //Modified by Kieran A. Gallagher
    public boolean DownloadAudioFile(String hostUrl, String filename, publishProgressInterface listener) {
        File file = new File(appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) ,filename);
        try {
            Log.d("s1313540", "Started Download" + hostUrl);
            URL server = new URL(hostUrl);

            HttpURLConnection connection = (HttpURLConnection) server.openConnection();
            long completeFileSize = connection.getContentLength();
            long downloadedFileSize = 0;

            connection.setRequestMethod("GET");

            connection.connect();

            int response = connection.getResponseCode();

            if(response == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                FileOutputStream os = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int byteReaded = is.read(buffer);
                int tmpCurrentProgress = -1;
                while (byteReaded != -1 && !isCancel) {
                    downloadedFileSize += byteReaded;
                    //get progress to be used in a dialog later
                    final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                    if((currentProgress/1000) > ((tmpCurrentProgress/1000)+2))
                    {
                        Log.d("s1313540", "Download Percentage: " + Integer.toString(currentProgress/1000));
                        tmpCurrentProgress = currentProgress;
                    }
                    os.write(buffer, 0, byteReaded);
                    byteReaded = is.read(buffer);
                    listener.PublishProgress(currentProgress/1000);
                }
                Log.d("s1313540", "Download manager Finished Download " + filename);
                os.flush();
                os.close();
                is.close();
            }
        }
        catch (IOException e)
        {
                e.printStackTrace();
                boolean deleted = file.delete();
                Log.d("s1313540", "file deleted:" + Boolean.toString(deleted));
            return false;
        }
        return checkAudioFile(file);
    }
    //check that audiofile exists
    public boolean checkAudioFile(File file){
        boolean deleted;
        if(file.exists())
        {
            Log.d("s1313540", "File exists: " + file.getName());

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(appContext, Uri.fromFile(file));
            //used to confirm that this is an audio file
            String isAudio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
            if(isAudio.equals("yes")) {
                Log.d("s1313540", "is audio: " + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                return true;
            }
            else
            {
                Log.d("s1313540", "isn't audio:" + isAudio);
                return false;
            }
        }
        else return false;
    }
    //some of the code is from http://stackoverflow.com/questions/3114471/android-launching-music-player-using-intent
    public void PlayDownloadInExternalApp(String fileName){
        File file = new File(appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS), fileName);
        Log.d("s1313540", "getting file");

        checkAudioFile(file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        appContext.startActivity(intent);
    }

    public void cancel(){
        isCancel = true;
    }

    //interface that publishes progress by sending the status of download
    public interface publishProgressInterface{
        public void PublishProgress(int progress);
    }

    public static boolean DownloadImageFile(Context context, String hostUrl, String filename){

        File file = getImageStorageDir(context, filename);

        try {
            Log.d("s1313540", "Started Download" + filename);
            URL server = new URL(hostUrl);

            InputStream is = new BufferedInputStream(server.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int byteReaded = is.read(buffer);
            while (byteReaded != -1) {
                out.write(buffer, 0, byteReaded);
                byteReaded = is.read(buffer);
            }
            Log.d("s1313540", "Download manager: Finished Image Download");
            out.close();
            is.close();

            byte[] response = out.toByteArray();
            FileOutputStream os = new FileOutputStream(file);
            os.write(response);
            os.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            boolean deleted = file.delete();
            Log.d("s1313540", "file deleted:" + Boolean.toString(deleted));
            return false;
        }
        return true;
    }
    //get specific image file
    public static File getImageStorageDir(Context context, String filename) {
        // Get the directory for the app's private pictures directory.
        String filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(filePath);
        if (!file.mkdirs()) {
            Log.e("s1313540", "Directory not created");
        }
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
    }
    //get specific audio file
    public  static File getDownloads(Context context, String filename) {
        String filePath = context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
        File file = new File(filePath);
        if (!file.mkdirs()) {
            Log.e("s1313540", "Directory not created");
        }
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS), filename);
    }
    //remove file from storage
    public static boolean deleteFile(Context context, String filename, String externalSubDir){
        File file = new File(context.getExternalFilesDir(externalSubDir), filename);
        return    file.delete();
    }

    public static boolean deleteFile(File file){
        return file.delete();
    }
}
//Authored by Kieran Anthony Gallagher S1313540