package com.wordpress.kagsme.s1313540_podcastapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence tabTitles[];
    int numOfTabs;


    public TabsPagerAdapter(FragmentManager fm, CharSequence[] titles, int noTitles)
    {
        super(fm);
        this.tabTitles = titles;
        this.numOfTabs = noTitles;
    }

    @Override
    public Fragment getItem(int index){
        switch(index){
            case 0:
                return new PodcastsFragment();
            case 1:
                return new DownloadsFragment();
        }
        return null;
    }

    @Override
    public int getCount(){
        //get item count - equal to the number of tabs
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
