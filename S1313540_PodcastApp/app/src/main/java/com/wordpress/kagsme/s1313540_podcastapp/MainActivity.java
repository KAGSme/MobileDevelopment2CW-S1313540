package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements AddPodcastDialog.OnCompleteListener {
    //set up main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //set up menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    //menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addPodcastRSS:
                AddPodcast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddPodcast() {
        DialogFragment newAddPodcastFragment = new AddPodcastDialog();
        newAddPodcastFragment.show(getFragmentManager(), "Add Podcast");
    }
    //when addPodcastURL dialog has closed this callback is called
    @Override
    public void onComplete(String podcastUrl) {
        Toast.makeText(getApplicationContext(), podcastUrl + " passed to activity", Toast.LENGTH_SHORT).show();
    }
}