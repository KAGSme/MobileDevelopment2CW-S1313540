package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

public class AddPodcastDialog extends DialogFragment{
    //contains code snippets found on developer.android.com

    private EditText urlEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //use the builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //inflate and set the layout for dialog
        builder.setView(inflater.inflate(R.layout.dialogue_addpodcast, null))
        //add action Buttons
                .setPositiveButton(R.string.Add, new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int id){
                    urlEditText = (EditText)getDialog().findViewById(R.id.podcastURL);
                    Toast.makeText(getActivity().getApplicationContext(), urlEditText.toString(), Toast.LENGTH_SHORT)
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                    AddPodcastDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

//Authored by Kieran Anthony Gallagher S1313540
