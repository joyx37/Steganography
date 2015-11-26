package com.example.ajoy3.steganography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ajoy3 on 11/25/2015.
 */
public class ChooseImage extends DialogFragment {
    private static final int REQUEST_CAMERA = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = {"Choose from SDcard","Capture Using Camera"};
        builder.setTitle("Carrier Image")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which){
                            case 0:{

                            }
                            case 1:{
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                getActivity().startActivityForResult(cameraIntent, REQUEST_CAMERA);
                            }
                        }
                    }
                });
        return builder.create();
    }
}