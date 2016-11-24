package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements AddPodcastDialog.OnCompleteListener {

    //Declare Variables ----------------------------------------------------
    AsyncRSSparser aRSSParser;
    //private ArrayAdapter episodeAdapter;
    //private ListView episodeList;

    Toolbar toolbar;
    ViewPager pager;
    TabsPagerAdapter tabsAdapter;
    SlidingTabLayout tabs;
    CharSequence tabTitles[]={"Podcasts", "Downloads"};
    int numberOfTabs =2;

    //set up main activity--------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabTitles, numberOfTabs);

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(tabsAdapter);

        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);

        //episodeList = (ListView)findViewById(R.id.MainListView);
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
        //episodeAdapter = new EpisodeDisplayAdapter(this, episodeDataItems);
        //episodeList.setAdapter(episodeAdapter);
    }
}