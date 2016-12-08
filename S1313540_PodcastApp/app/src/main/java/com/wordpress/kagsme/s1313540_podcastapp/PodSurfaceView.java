package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PodSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder shPodSurface;
    podcastDrawThread drawingThread = null;

    public PodSurfaceView(Context context)
    {
        super(context);
        shPodSurface = getHolder();
        shPodSurface.addCallback(this);
        setFocusable(true);
    }

    public podcastDrawThread getThread()
    {
        return drawingThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        drawingThread.setRunning(true);
        drawingThread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawingThread.setSurface(width,height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        drawingThread.setRunning(false);
        while(retry)
        {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetDrawingThead(){
        drawingThread = new podcastDrawThread(getHolder(), this, getContext());
    }
}

//Authored by Kieran Anthony Gallagher S1313540
