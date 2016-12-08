package com.wordpress.kagsme.s1313540_podcastapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CanvasFragment extends Fragment {

    PodSurfaceView view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = new PodSurfaceView(getContext());

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        //view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume(){
        super.onResume();
        //required to create new thread when resuming;
        view.resetDrawingThead();
    }


}

//Authored by Kieran Anthony Gallagher S1313540
