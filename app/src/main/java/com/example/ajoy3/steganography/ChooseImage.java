package com.example.ajoy3.steganography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;


/**
 * Created by ajoy3 on 11/25/2015.
 */
public class ChooseImage extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = {"Choose From SDcard","Capture Using Camera"};
        builder.setTitle("Image")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == 0) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            getActivity().startActivityForResult(photoPickerIntent, Constants.REQUEST_SDCARD);
                        }
                        else if (which == 1) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getActivity().startActivityForResult(cameraIntent, Constants.REQUEST_CAMERA);
                        }
                    }
                });
        return builder.create();
    }
}