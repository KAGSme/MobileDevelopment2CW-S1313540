package com.wordpress.kagsme.s1313540_podcastapp;

import android.*;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements AddPodcastDialog.OnCompleteListener {

    //Declare Variables ----------------------------------------------------
    AsyncGetPodcastInfo aPodcastInfo;

    Toolbar toolbar;
    ViewPager pager;
    TabsPagerAdapter tabsAdapter;
    SlidingTabLayout tabs;
    CharSequence tabTitles[]={"Podcasts", "Downloads", "Welcome"};
    int numberOfTabs = 3;

    private PodcastInfoDBMgr dbMgr;

    FragmentManager fmAboutDialog;

    public PodcastInfoDBMgr getDbMgr() {
        return dbMgr;
    }

    //set up main activity--------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);

        CreateDatabase();

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

        fmAboutDialog = this.getFragmentManager();

        RequestPermissions();
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
            case R.id.about:
                aboutDialog();
                return true;
            case R.id.map:
                Intent mapsActivity = new Intent(this, MapsActivity.class);
                startActivity(mapsActivity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Create the dialog for adding podcasts
    public void AddPodcast() {
        DialogFragment newAddPodcastFragment = new AddPodcastDialog();
        newAddPodcastFragment.show(getFragmentManager(), "Add Podcast");
    }

    //when addPodcastURL dialog has closed this callback is called
    @Override
    public void onComplete(String podcastUrl) {
        aPodcastInfo = new AsyncGetPodcastInfo(this, podcastUrl, dbMgr, (PodcastsFragment) tabsAdapter.getRgisteredFragment(0));
        aPodcastInfo.execute();
    }

    //Create the about dialog
    public void aboutDialog()
    {
        DialogFragment aboutDig = new AboutDialogue();
        aboutDig.show(fmAboutDialog, "menu");
    }
    //create the database
    public void CreateDatabase() {
        dbMgr = new PodcastInfoDBMgr(this, "savedPodcasts.s3db", null, 1);
        try
        {
            dbMgr.dbCreate();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //request permission dialog for fine location
    @TargetApi(23)
    public void RequestPermissions(){
        int result = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, result);
        Log.e("s1313540", "Permission requested");
    }
}
//Authored by Kieran Anthony Gallagher S1313540