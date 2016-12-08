package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.io.File;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {

    private Context appContext;
    private ArrayAdapter downloadAdapter;
    private ListView downloadList;
    private DownloadItem dSelectedItem;

    private SharedPreferences sharedPrefs;
    private SavedData savdat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.actvity_main2, container, false);

        appContext = getContext();

        downloadList = (ListView)view.findViewById(R.id.DownloadListView);

        downloadList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DownloadsMgr downloadsMgr = new DownloadsMgr(appContext);
                        DownloadItem dItem = (DownloadItem)parent.getItemAtPosition(position);
                        downloadsMgr.PlayDownloadInExternalApp(dItem.getDownloadFileName());
                        savdat.savePreferences("lastPlayed", dItem.getDownloadTitle());
                        RetrieveDownloadsList();
                    }
                }
        );

        downloadList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, (DownloadItem) parent.getItemAtPosition(position));
                return true;
            }
        });

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        savdat = new SavedData(sharedPrefs);
        //savdat.setDefaultPrefs();

        //Toast.makeText(appContext, "Favourited: " + sharedPrefs.getString("favourite", "None") , Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //RetrieveDownloadsList();
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveDownloadsList();
    }
    //create list based on files from the DIRECTORY_PODCASTS in the apps podcast directory
    public void RetrieveDownloadsList() {
        File[] files;
        if(appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).listFiles().length > 0)
            files = appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).listFiles();
        else files = null;
        ArrayList<DownloadItem> dItems = new ArrayList<DownloadItem>();

        if (files != null) {
            for (File f : files) {
                DownloadItem tmp = new DownloadItem();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(appContext, Uri.fromFile(f));
                tmp.setDownloadFileName(f.getName());

                String tempStr = "";
                tempStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                if(tempStr == null) tmp.setDownloadTitle(tmp.getDownloadFileName());
                else tmp.setDownloadTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

                tmp.setDownloadDuration(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                //check preferences for favourited/ lastplayed podcast
                try {
                    if (tmp.getDownloadTitle().equals(sharedPrefs.getString("favourite", "None")))
                        tmp.setFavourite(true);
                    if (tmp.getDownloadTitle().equals(sharedPrefs.getString("lastPlayed", "None")))
                        tmp.setLastPlayed(true);
                }
                catch (Exception e){
                    Log.e("s1313540", "got null bool");
                }
                //display embedded cover art for episode, if none exists then use a default icon
                if(retriever.getEmbeddedPicture() != null)
                    tmp.setBitmapCover(BitmapFactory.decodeByteArray(retriever.getEmbeddedPicture(), 0, retriever.getEmbeddedPicture().length));
                else tmp.setBitmapCover(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_action_play));

                dItems.add(tmp);
            }
        }

        if (downloadAdapter != null) downloadAdapter.clear();
        downloadAdapter = new DownloadDisplayAdapter(appContext, dItems);
        downloadList.setAdapter(downloadAdapter);
    }

    //Create popup menu-------------------------------------------------------
    public void showPopup(View v, DownloadItem dItem) {
        PopupMenu popup = new PopupMenu(appContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions_downloadsmenu, popup.getMenu());
        dSelectedItem = dItem;
        popup.show();

        //Popup menu item selection--------------------------------------------
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        if(dSelectedItem != null)
                            if(!dSelectedItem.getDownloadFileName().equals(""))
                            {
                                try
                                {
                                    //remove episode from preferences if it was there
                                    if (dSelectedItem.getDownloadTitle().equals(sharedPrefs.getString("favourite", "None")))
                                        savdat.savePreferences("favourite", "None");
                                    if (dSelectedItem.getDownloadTitle().equals(sharedPrefs.getString("lastPlayed", "None")))
                                        savdat.savePreferences("lastPlayed", "None");
                                }
                                catch(Exception e)
                                {
                                    Log.e("s1313540", "got null bool");
                                }

                                DownloadsMgr.deleteFile(appContext, dSelectedItem.getDownloadFileName(), Environment.DIRECTORY_PODCASTS);
                                Toast.makeText(appContext, "File Deleted", Toast.LENGTH_SHORT).show();
                                RetrieveDownloadsList();
                            }
                        RetrieveDownloadsList();
                        return true;
                    case R.id.favourite:
                        //set favourite preference on selected download item
                        savdat.savePreferences("favourite", dSelectedItem.getDownloadTitle());
                        RetrieveDownloadsList();
                        return true;
                    default:
                        return false;
                }
            }

        });
    }
}
//Authored by Kieran Anthony Gallagher S1313540