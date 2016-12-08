package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PodcastDisplayAdapter extends ArrayAdapter<PodcastDataItem> {

    PodcastDisplayAdapter(Context context, ArrayList<PodcastDataItem> items){
        super(context, R.layout.list_podcastepisode, items);
    }

    Map<String,Bitmap> myBitmap = new HashMap<String,Bitmap>();
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View podcastListView = inflater.inflate(R.layout.list_podcastinfo, parent, false);

        TextView titleView = (TextView)podcastListView.findViewById(R.id.podcastiTitle);
        TextView descView = (TextView)podcastListView.findViewById(R.id.podcastiDescription);
        TextView linkView = (TextView)podcastListView.findViewById(R.id.podcastiLink);
        ImageView imageView = (ImageView)podcastListView.findViewById(R.id.pImageView);

        PodcastDataItem pDataItem = new PodcastDataItem();
        if(getItem(position) != null)pDataItem = getItem(position);

        titleView.setText(pDataItem.getPodcastTitle());
        descView.setText(pDataItem.getPodcastDesc());
        linkView.setText(String.format(getContext().getResources().getString(R.string.linkText),pDataItem.getPodcastLink()));

        File file = DownloadsMgr.getImageStorageDir(getContext(), pDataItem.getPodcastImageFName());
        if(!file.exists())
        {
            Log.e("s1313540", "Need To Redownload Image File");
            AsyncTaskDownloadImage aTDI = new AsyncTaskDownloadImage(getContext(), pDataItem.getPodcastImageLink(), pDataItem.getPodcastImageFName());
            try {
                aTDI.execute().get();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //gets image to use in list, if image does not exist then it needs to be redownloaded
        if(file.exists()&& !myBitmap.containsKey(Integer.toString(position)))
            myBitmap.put(Integer.toString(position), BitmapFactory.decodeFile(file.getAbsolutePath()));
        if(myBitmap.get(Integer.toString(position)) != null)
            imageView.setImageBitmap(myBitmap.get(Integer.toString(position)));
        else imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_add_dark));

        return podcastListView;
    }
}
//Authored by Kieran Anthony Gallagher S1313540