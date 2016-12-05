package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PodcastEpisodesActivity extends AppCompatActivity
        implements AsyncRSSparser.sendEDataItemsListener, AsyncDownloadFile.showDialogListener{

    AsyncRSSparser aRSSParser;
    private ArrayAdapter episodeAdapter;
    private ListView episodeList;

    private TextView pTitleV;
    private TextView pDescV;
    private String pLink;
    private ImageView pImageV;

    private Toolbar toolbar;

    private ProgressBar pBar;

    boolean refreshDone = true;

    private EpisodeDataItem selectedEpisode;

    Bitmap myBitmap;

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

        episodeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, (EpisodeDataItem) parent.getItemAtPosition(position));
                return true;
            }
        });

        pTitleV = (TextView)findViewById(R.id.podcastTitle);
        pDescV =  (TextView) findViewById(R.id.podcastDescription);

        pTitleV.setText(iMainAct.getStringExtra("pTitle"));
        pDescV.setText(iMainAct.getStringExtra("pDesc"));

        pLink = iMainAct.getStringExtra("pLink");

        pBar = (ProgressBar) findViewById(R.id.progressBarEpisodes);
        pBar.setVisibility(View.INVISIBLE);

        pImageV = (ImageView) findViewById(R.id.pImageViewBig);

        File file = DownloadsMgr.getImageStorageDir(this, iMainAct.getStringExtra("pImageFName"));
        if(file.exists()&& myBitmap == null) myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if(myBitmap != null)pImageV.setImageBitmap(myBitmap);
        else pImageV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

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
    //Create popup menu-------------------------------------------------------
    public void showPopup(View v, EpisodeDataItem eSelectedData) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions_episodemenu, popup.getMenu());
        selectedEpisode = eSelectedData;
        popup.show();

        //Popup menu item selection--------------------------------------------
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        if(selectedEpisode != null)
                            if(selectedEpisode.getFilename() != "")
                                DownloadEpisode(selectedEpisode.getFilename(), selectedEpisode.getEpisodeLink());
                        return true;
                    default:
                        return false;
                }
            }

        });
    }

    @Override
    public void onResume(){
        super.onResume();

        RefreshList(pLink);
    }

    private void RefreshList(String podcastURL)
    {
        if(refreshDone)
        {
            pBar.setVisibility(View.VISIBLE);
            refreshDone = false;
            aRSSParser = new AsyncRSSparser(this, podcastURL);
            aRSSParser.execute();
        }
        else
        {
            Log.e("s1313540", "Currently refreshing");
        }
    }

    @Override
    public void SendEDataItem(ArrayList<EpisodeDataItem> eDatas){

        refreshDone = true;
        pBar.setVisibility(View.INVISIBLE);

        ArrayList<EpisodeDataItem> episodeDataItems = eDatas;
        if(!eDatas.isEmpty())
        {
            if (episodeAdapter != null) episodeAdapter.clear();
            episodeAdapter = new EpisodeDisplayAdapter(this, episodeDataItems);
            episodeList.setAdapter(episodeAdapter);
        }
    }

    @Override
    public void showDialog(DialogFragment dF){
        dF.show(getFragmentManager(), "downloadProgress");
    }

    private void DownloadEpisode(String fName, String dLink){
        AsyncDownloadFile asyncDownloadFile = new AsyncDownloadFile(getApplicationContext(), dLink, fName, this);
        asyncDownloadFile.execute();
    }

}
