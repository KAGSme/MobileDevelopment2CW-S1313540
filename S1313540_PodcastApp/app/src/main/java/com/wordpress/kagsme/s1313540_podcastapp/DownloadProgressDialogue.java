package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadProgressDialogue extends DialogFragment {

    //Declare Variables-------------------------------------------------

    private ProgressBar pBar;
    private TextView dStatus;

    private DownloadProgressDialogue.onCancelListener mListener;

    //Set up Dialog----------------------------------------------------

    //contains code snippets found on developer.android.com, was heavily modified by me
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //use the builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogue_progress, null);
        //inflate and set the layout for dialog
        builder.setView(v)
                //add action Buttons
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        dismiss();
                    }
                });
        builder.setTitle("Downloading...");
        builder.setIcon(R.drawable.ic_add_dark);

        pBar = (ProgressBar) v.findViewById(R.id.downloadProgressBar);
        pBar.setMax(100);
        dStatus = (TextView) v.findViewById(R.id.ProgressTextView);

        return builder.create();
    }
    //set listener for interface when the dialog is cancelled
    public void setOnCancelListener(DownloadProgressDialogue.onCancelListener listener){
        mListener = listener;
    }

    public interface onCancelListener{
        public void OnCancel();
    }
    //display progress
    public void setProgress(int progress){
        pBar.setProgress(progress);
        dStatus.setText(Integer.toString(progress) + "%");
    }
 //call cancel listener when the dialog is dismissed
    @Override
    public void onDismiss(final DialogInterface dialog) {
        mListener.OnCancel();
    }

}
//Authored by Kieran Anthony Gallagher S1313540