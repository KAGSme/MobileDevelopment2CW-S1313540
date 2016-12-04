package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class AddPodcastDialog extends DialogFragment{

    //Declare Variables-------------------------------------------------

    private EditText urlEditText;
    private String urlOutputText;

    private OnCompleteListener mListener;

    //Set up Dialog

    //contains code snippets found on developer.android.com
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
                    setUrlOutputText(urlEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                    AddPodcastDialog.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Add Podcast");
        builder.setIcon(R.drawable.ic_add_dark);
        return builder.create();
    }
    private void setUrlOutputText(String value){
        urlOutputText = value;
        this.mListener.onComplete(urlOutputText);
    }

    public String getUrlOutputText(){
        return urlOutputText;
    }
//Contains modified code snippets from developer.android.com & http://stackoverflow.com/questions/15121373/returning-string-from-dialog-fragment-back-to-activity
    public static interface OnCompleteListener{
        public abstract void onComplete(String podcastURL);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            this.mListener = (OnCompleteListener)getActivity();
        }
        catch (final ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }
    }
}


//Authored by Kieran Anthony Gallagher S1313540
