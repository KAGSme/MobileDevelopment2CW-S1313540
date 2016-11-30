package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadsMgr {

    private Context appContext;

    public DownloadsMgr(Context context){
        appContext = context;
    }

    //code was obtained from http://stackoverflow.com/questions/7351940/how-can-i-download-audio-file-from-server-by-url
    //Modified by Kieran A. Gallagher
    public void downloadFile(String hostUrl, String filename) {
        try {
            File file = new File(filename);
            URL server = new URL(hostUrl);

            HttpURLConnection connection = (HttpURLConnection) server.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.addRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, audio/mpeg3 */*");
            connection.addRequestProperty("Accept-Language", "en-us,zh-cn;q=0.5");
            connection.addRequestProperty("Accept-Encoding", "gzip, deflate");

            connection.connect();
            InputStream is = connection.getInputStream();
            FileOutputStream os = appContext.openFileOutput(filename, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int byteReaded = is.read(buffer);
            while (byteReaded != -1) {
                os.write(buffer, 0, byteReaded);
                byteReaded = is.read(buffer);
            }

            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
