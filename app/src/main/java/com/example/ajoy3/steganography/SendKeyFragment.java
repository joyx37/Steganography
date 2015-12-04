package com.example.ajoy3.steganography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Jiaxuan on 12/2/2015.
 */
public class SendKeyFragment extends DialogFragment {

    private TextView mSendDialog;
    public interface KeyCreateListener
    {
        void KeyCreateComplete(String mkey) throws IOException;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //View view = inflater.inflate(R.layout.fragment_sendkey_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Device Selected")
                // Add action buttons
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Toast.makeText(getActivity(), "Store device here", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }


}
