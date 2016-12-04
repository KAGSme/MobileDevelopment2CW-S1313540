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

    //Set up Dialog

    //contains code snippets found on developer.android.com
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
                        mListener.OnCancel();
                        DownloadProgressDialogue.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Downloading...");
        builder.setIcon(R.drawable.ic_add_dark);

        pBar = (ProgressBar) v.findViewById(R.id.downloadProgressBar);
        pBar.setMax(100);
        dStatus = (TextView) v.findViewById(R.id.ProgressTextView);

        return builder.create();
    }

    public void setOnCancelListener(DownloadProgressDialogue.onCancelListener listener){
        mListener = listener;
    }

    public interface onCancelListener{
        public void OnCancel();
    }

    public void setProgress(int progress){
        pBar.setProgress(progress);
        dStatus.setText(Integer.toString(progress) + "%");
    }

}
