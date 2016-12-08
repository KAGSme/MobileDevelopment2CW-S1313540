package com.wordpress.kagsme.s1313540_podcastapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.StrictMode;
import android.view.SurfaceHolder;

public class podcastDrawThread extends Thread{
    private int canvasWidth;
    private int canvasHeight;
    private float xPos = 0.0f;
    private float yPos = 0.0f;
    private int i;

    private float HalfAppletHeight;
    private float HalfAppletWidth;

    private boolean first = true;
    private boolean run = false;

    private SurfaceHolder shPodSurface;
    private Paint podPaint;
    private PodSurfaceView psv;

    public podcastDrawThread(SurfaceHolder surfaceHolder, PodSurfaceView podsfv){
        this.shPodSurface = surfaceHolder;
        this.psv = podsfv;
        podPaint = new Paint();
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
            HalfAppletWidth = width/32;
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
            drawAxes(canvas);
            podPaint.setColor(Color.RED);
            drawWave(canvas, 23);
            podPaint.setColor(Color.GREEN);
            drawWave(canvas, 28);
            podPaint.setColor(Color.BLUE);
            drawWave(canvas, 33);
        }
    }

    public void drawWave(Canvas theCanvas, int period)
    {
        float xPosOld = 0.0f;
        float yPosOld = 0.0f;
        int dStart = -15;
        int sDate = 0;
        int tDate = 0;

        sDate = 6 + dStart;
        for (i=0;i<=30;i++)
        {
            xPos = i * HalfAppletWidth;
            tDate = sDate + i;
            yPos = (float)(-100.0f * (Math.sin((tDate%period)*2*Math.PI/period)));
            if ( i == 0)
                podPaint.setStyle(Paint.Style.FILL);
            else
                theCanvas.drawLine(xPosOld, (yPosOld + HalfAppletHeight), xPos,
                        (yPos + HalfAppletHeight), podPaint);
            xPosOld = xPos;
            yPosOld = yPos;
        }
    }


    public void drawAxes(Canvas theCanvas)
    {
        podPaint.setColor(Color.BLACK);
        theCanvas.drawLine(0,HalfAppletHeight,30*HalfAppletWidth,HalfAppletHeight,
                podPaint); // Horizontal X Axes
        theCanvas.drawLine(15* HalfAppletWidth,0,15* HalfAppletWidth,canvasHeight,
                podPaint); // Vertical Y Axes
    }


}


//Authored by Kieran Anthony Gallagher S1313540
