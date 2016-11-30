package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PodcastEpisodesActivity extends AppCompatActivity implements AsyncRSSparser.sendEDataItemsListener{

    AsyncRSSparser aRSSParser;
    private ArrayAdapter episodeAdapter;
    private ListView episodeList;

    private TextView pTitleV;
    private TextView pDescV;
    private String pLink;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcastepisodes);
        setTheme(R.style.AppTheme);

        Intent iMainAct = getIntent();

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)getSupportActionBar().show();
        else Log.e("s1313540", "no actionbar???");

        episodeList = (ListView)findViewById(R.id.EpisodeListView);

        pTitleV = (TextView)findViewById(R.id.podcastTitle);
        pDescV =  (TextView) findViewById(R.id.podcastDescription);

        pTitleV.setText(iMainAct.getStringExtra("pTitle"));
        pDescV.setText(iMainAct.getStringExtra("pDesc"));

        pLink = iMainAct.getStringExtra("pLink");
    }

    //set up menu-----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.podcastmenu, menu);
        return true;
    }
    //menu item selection---------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.refreshList:
                RefreshList(pLink);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        RefreshList(pLink);
    }

    private void RefreshList(String podcastURL)
    {
        aRSSParser = new AsyncRSSparser(this, podcastURL);
        aRSSParser.execute();
    }

    @Override
    public void SendEDataItem(ArrayList<EpisodeDataItem> eDatas){
        ArrayList<EpisodeDataItem> episodeDataItems = eDatas;

        if(episodeAdapter != null)episodeAdapter.clear();
        episodeAdapter = new EpisodeDisplayAdapter(this, episodeDataItems);
        episodeList.setAdapter(episodeAdapter);
    }
}
