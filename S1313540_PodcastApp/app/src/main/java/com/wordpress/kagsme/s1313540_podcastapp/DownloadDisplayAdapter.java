package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView iView = (ImageView) DownlodListView.findViewById(R.id.imageView);

        DownloadItem dItem = new DownloadItem();
        if(getItem(position) != null)dItem = getItem(position);
        //checks if the download was marked as either last played or as favourite then colours the appropriate view
        titleView.setText(dItem.getDownloadTitle());
        if(dItem.getIsFavourite()) titleView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        lenView.setText(dItem.getDownloadLength());
        if(dItem.getIsLastPlayed()) lenView.setTextColor(getContext().getResources().getColor(R.color.podRed));
        iView.setImageBitmap(dItem.getBitmapCover());

        return DownlodListView;
        }
}
//Authored by Kieran Anthony Gallagher S1313540