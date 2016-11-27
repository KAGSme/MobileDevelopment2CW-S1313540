package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EpisodeDisplayAdapter extends ArrayAdapter<EpisodeDataItem> {

    EpisodeDisplayAdapter(Context context, ArrayList<EpisodeDataItem> items){
        super(context, R.layout.list_podcastepisode, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View episodeListView = inflater.inflate(R.layout.list_podcastepisode, parent, false);

        TextView titleView = (TextView)episodeListView.findViewById(R.id.episodeTitle);
        TextView descView = (TextView)episodeListView.findViewById(R.id.episodeDescription);
        TextView linkView = (TextView)episodeListView.findViewById(R.id.episodeLink);

        EpisodeDataItem eDataItem = new EpisodeDataItem();
        if(getItem(position) != null)eDataItem = getItem(position);

        titleView.setText(eDataItem.getEpisodeTitle());
        descView.setText(eDataItem.getEpisodeDesc());
        linkView.setText(eDataItem.getEpisodeLink());

        return episodeListView;
    }
}
