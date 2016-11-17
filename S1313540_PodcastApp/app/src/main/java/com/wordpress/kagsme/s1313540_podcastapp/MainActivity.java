package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements AddPodcastDialog.OnCompleteListener {

    //Declare Variables ----------------------------------------------------
    AsyncRSSparser aRSSParser;
    private ArrayAdapter episodeAdapter;

    private TextView pTitleText;
    private TextView pDescText;
    private TextView pLinkText;
    private ListView episodeList;

    //set up main activity--------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pTitleText = (TextView)findViewById(R.id.PodcastTitleTxt);
        pDescText = (TextView)findViewById(R.id.PodcastDescriptionTxt);
        pLinkText = (TextView)findViewById(R.id.PodcastLinkTxt);
        episodeList = (ListView)findViewById(R.id.MainListView);

    }
    //set up menu-----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    //menu item selection---------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addPodcastRSS:
                AddPodcast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddPodcast() {
        DialogFragment newAddPodcastFragment = new AddPodcastDialog();
        newAddPodcastFragment.show(getFragmentManager(), "Add Podcast");
    }
    //when addPodcastURL dialog has closed this callback is called
    @Override
    public void onComplete(String podcastUrl) {
        aRSSParser = new AsyncRSSparser(this, podcastUrl);
        ArrayList<EpisodeDataItem> episodeDataItems = new ArrayList<EpisodeDataItem>();
        try
        {
            episodeDataItems = aRSSParser.execute().get();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(ExecutionException e)
        {
            e.printStackTrace();
        }
        episodeAdapter = new EpisodeDisplayAdapter(this, episodeDataItems);
        episodeList.setAdapter(episodeAdapter);
    }
}