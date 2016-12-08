package com.wordpress.kagsme.s1313540_podcastapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//contains code snippets from http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html modified by Kieran Gallagher

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence tabTitles[];
    int numOfTabs;

    Fragment[] registeredFragments = new Fragment[3];

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
                return registeredFragments[0] = new PodcastsFragment();
            case 1:
                return registeredFragments[1] = new DownloadsFragment();
            case 2:
                return registeredFragments[2] = new CanvasFragment();
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

    public Fragment getRgisteredFragment(int position){
        return registeredFragments[position];
    }
}
//Authored by Kieran Anthony Gallagher S1313540