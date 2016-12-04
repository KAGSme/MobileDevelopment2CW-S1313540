package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DownloadDisplayAdapter extends ArrayAdapter<DownloadItem> {

    DownloadDisplayAdapter(Context context, ArrayList<DownloadItem> items){
        super(context, R.layout.list_podcastepisode, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View DownlodListView = inflater.inflate(R.layout.list_download, parent, false);

        TextView titleView = (TextView)DownlodListView.findViewById(R.id.downloadTitle);
        TextView lenView = (TextView)DownlodListView.findViewById(R.id.downloadDuration);

        DownloadItem dItem = new DownloadItem();
        if(getItem(position) != null)dItem = getItem(position);

        titleView.setText(dItem.getDownloadTitle());
        lenView.setText(dItem.getDownloadLength());

        return DownlodListView;
        }
}
