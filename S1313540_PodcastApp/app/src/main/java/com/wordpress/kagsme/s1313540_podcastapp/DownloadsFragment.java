package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {

    private Context appContext;
    private ArrayAdapter downloadAdapter;
    private ListView downloadList;

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
                    }
                }
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RetrieveDownloadsList();
    }

    public void RetrieveDownloadsList() {
        File[] files;
        if(appContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).listFiles() != null)
            files = appContext.getExternalFilesDir(null).listFiles();
        else files = null;
        ArrayList<DownloadItem> dItems = new ArrayList<DownloadItem>();

        if (files != null) {
            for (File f : files) {
                DownloadItem tmp = new DownloadItem();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(appContext, Uri.fromFile(f));
                tmp.setDownloadFileName(f.getName());
                tmp.setDownloadTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                tmp.setDownloadDuration(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                dItems.add(tmp);
            }

            if (downloadAdapter != null) downloadAdapter.clear();
            downloadAdapter = new DownloadDisplayAdapter(appContext, dItems);
            downloadList.setAdapter(downloadAdapter);
        }
    }
}