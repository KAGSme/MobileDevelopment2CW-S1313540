package com.wordpress.kagsme.s1313540_podcastapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AboutDialogue extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder mcAboutDialog = new AlertDialog.Builder(getActivity());
        mcAboutDialog.setMessage(R.string.aboutdialog_text)
                .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        mcAboutDialog.setTitle("About");
        mcAboutDialog.setIcon(R.drawable.ic_about_dark);
        // Create the AlertDialog object and return it
        return mcAboutDialog.create();
    }
}
//Authored by Kieran Anthony Gallagher S1313540