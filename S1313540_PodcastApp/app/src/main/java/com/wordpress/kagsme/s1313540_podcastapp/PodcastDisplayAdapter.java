package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PodcastDisplayAdapter extends ArrayAdapter<PodcastDataItem> {

    PodcastDisplayAdapter(Context context, ArrayList<PodcastDataItem> items){
        super(context, R.layout.list_podcastepisode, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View podcastListView = inflater.inflate(R.layout.list_podcastinfo, parent, false);

        TextView titleView = (TextView)podcastListView.findViewById(R.id.episodeTitle);
        TextView descView = (TextView)podcastListView.findViewById(R.id.episodeDescription);
        TextView linkView = (TextView)podcastListView.findViewById(R.id.episodeLink);
        ImageView imageView = (ImageView)podcastListView.findViewById(R.id.pImageView);

        PodcastDataItem pDataItem = new PodcastDataItem();
        if(getItem(position) != null)pDataItem = getItem(position);

        titleView.setText(pDataItem.getPodcastTitle());
        descView.setText(pDataItem.getPodcastDesc());
        linkView.setText(String.format(getContext().getResources().getString(R.string.linkText),pDataItem.getPodcastLink()));

        File file = new File(Environment.DIRECTORY_PICTURES, pDataItem.getPodcastImageFName());
        if(!file.exists())
        {
            DownloadsMgr.DownloadImageFile
                    (getContext() ,pDataItem.getPodcastImageLink(), pDataItem.getPodcastImageFName());
            file = new File(Environment.DIRECTORY_PICTURES, pDataItem.getPodcastImageFName());
        }
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);

        return podcastListView;
    }
}
