package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class PodcastsFragment extends Fragment {

    private ArrayAdapter podcastAdapter;
    private ListView podcastList;

    Context appContext;

    PodcastDataItem pSelectedItem;

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
                        podcastActivity.putExtra("pImageLink", eda.getPodcastImageLink());
                        podcastActivity.putExtra("pImageFName", eda.getPodcastImageFName());
                        //switch view
                        appContext.startActivity(podcastActivity);
                    }
                }
        );

        podcastList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, (PodcastDataItem) parent.getItemAtPosition(position));
                return true;
            }
        });

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
    //retrieve podcasts from database and put them in display adapter for list
    public void DisplayPodcastDatabaseTableAsList(){
        if(podcastAdapter != null) podcastAdapter.clear();
        podcastAdapter = new PodcastDisplayAdapter(appContext, dbMgr.getAllPodcastDataItems());
        podcastList.setAdapter(podcastAdapter);
        Log.d("s1313540", "displaying table as list");
    }

    //Create popup menu-------------------------------------------------------
    public void showPopup(View v, PodcastDataItem pItem) {
        PopupMenu popup = new PopupMenu(appContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions_podcastinfo, popup.getMenu());
        pSelectedItem = pItem;
        popup.show();

        //Popup menu item selection--------------------------------------------
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                        if(pSelectedItem != null)
                            if(!pSelectedItem.getPodcastTitle().equals(""))
                            {
                                //remove podcast item from database
                                dbMgr.removePodcastDataItem(pSelectedItem.getPodcastTitle());
                                Toast.makeText(appContext, "Podcast Removed", Toast.LENGTH_SHORT).show();
                            }
                        //redisplay table of podcasts
                        DisplayPodcastDatabaseTableAsList();
                        return true;
                    default:
                        return false;
                }
            }

        });
    }
}
//Authored by Kieran Anthony Gallagher S1313540