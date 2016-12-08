package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.StrictMode;
import android.view.SurfaceHolder;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.File;

public class podcastDrawThread extends Thread{
    private int canvasWidth;
    private int canvasHeight;

    private float HalfAppletHeight;
    private float HalfAppletWidth;

    private boolean first = true;
    private boolean run = false;

    private SurfaceHolder shPodSurface;
    private Paint podPaint;
    private PodSurfaceView psv;

    private Context appContext;

    public podcastDrawThread(SurfaceHolder surfaceHolder, PodSurfaceView podsfv, Context context){
        this.shPodSurface = surfaceHolder;
        this.psv = podsfv;
        podPaint = new Paint();
        appContext = context;
    }

    public void doStart(){
        synchronized (shPodSurface){
            first = false;
        }
    }

    public void setRunning(boolean b){
        run = b;
    }

    public void setSurface(int width, int height){
        synchronized (shPodSurface){
            canvasHeight = height;
            canvasWidth = width;
            HalfAppletHeight = height/2;
            HalfAppletWidth = width/2;
            doStart();
        }
    }

    public void run() {
        while (run) {
            Canvas c = null;
            try {
                c = shPodSurface.lockCanvas(null);
                synchronized (shPodSurface) {
                    svDraw(c);
                }
            } finally {
                if (c != null) {
                    shPodSurface.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private void svDraw(Canvas canvas){
        if(canvas != null) {
            canvas.save();
            canvas.restore();
            canvas.drawColor(Color.WHITE);
            podPaint.setStyle(Paint.Style.FILL);
            drawCircles(canvas);
            drawLine(canvas);
            canvas.drawBitmap(BitmapFactory.decodeResource(appContext.getResources(),R.mipmap.ic_launcher),0, 0, podPaint);

            File[] files;
            if(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles().length > 0)
                files = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles();
            else files = null;
            if(files != null) {
                int i = 0;
                for (File f : files) {
                    drawPodcastImages(canvas, f.getPath(), i);
                    i++;
                }
            }
        }
    }

    public void drawLine(Canvas theCanvas) {
        podPaint.setColor(Color.BLACK);
        theCanvas.drawLine(0, HalfAppletHeight + 150, canvasWidth, HalfAppletHeight + 150,
                podPaint);
        theCanvas.drawLine(0, HalfAppletHeight - 150, canvasWidth, HalfAppletHeight - 150,
                podPaint);
        theCanvas.drawLine(HalfAppletWidth - 150, 0, HalfAppletWidth - 150, canvasHeight,
                podPaint);
        theCanvas.drawLine(HalfAppletWidth + 150, 0, HalfAppletWidth + 150, canvasHeight,
                podPaint);
    }

    public void drawCircles(Canvas theCanvas) {
        podPaint.setColor(Color.BLACK);
        theCanvas.drawCircle(HalfAppletWidth, HalfAppletHeight, 150,
                podPaint);
        podPaint.setColor(Color.RED);
        theCanvas.drawCircle(HalfAppletWidth, HalfAppletHeight, 100,
                podPaint);
    }

    public void drawPodcastImages(Canvas theCanvas, String filePath, int i){
        theCanvas.drawBitmap(BitmapFactory.decodeFile(filePath),null, new RectF(canvasWidth - 150, 100 * i, canvasWidth - 50, (100 * i) + 100), podPaint);
    }


}


//Authored by Kieran Anthony Gallagher S1313540
