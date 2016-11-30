package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class PodcastsFragment extends Fragment {

    private ArrayAdapter podcastAdapter;
    private ListView podcastList;

    Context appContext;

    PodcastInfoDBMgr dbMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.actvity_main1, container, false);

        appContext = getContext();

        podcastList = (ListView)view.findViewById(R.id.PodcastListView);
        RetrieveTable();
        DisplayPodcastDatabaseTableAsList();

        podcastList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PodcastDataItem eda = (PodcastDataItem) parent.getItemAtPosition(position);

                        Intent podcastActivity = new Intent(appContext, PodcastEpisodesActivity.class);
                        podcastActivity.putExtra("pTitle", eda.getPodcastTitle());
                        podcastActivity.putExtra("pDesc", eda.getPodcastDesc());
                        podcastActivity.putExtra("pLink", eda.getPodcastLink());
                        //switch view
                        appContext.startActivity(podcastActivity);
                    }
                }
        );

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        RetrieveTable();
        DisplayPodcastDatabaseTableAsList();
        Log.d("s1313540", "PFragment Resumed");
    }

    public void RetrieveTable(){
        dbMgr = ((MainActivity) getActivity()).getDbMgr();
    }

    public void DisplayPodcastDatabaseTableAsList(){
        if(podcastAdapter != null) podcastAdapter.clear();
        podcastAdapter = new PodcastDisplayAdapter(appContext, dbMgr.getAllPodcastDataItems());
        podcastList.setAdapter(podcastAdapter);
        Log.d("s1313540", "displaying table as list");
    }
}
