package com.example.ajoy3.steganography;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ImageInImageStegnos extends AppCompatActivity {

    private static String imageName;
    private static String carrierPath,secretPath,msgPath;
    private static int clickedButton;
    private static Bitmap receivedBitmap;
    private ImageView mImageViewCarrier,mImageViewSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_in_image_stegnos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageViewCarrier = (ImageView)findViewById(R.id.imageView3);
        mImageViewSecret = (ImageView)findViewById(R.id.imageView4);
        imageName = Long.toString(new Date().getTime());
    }

    public void OpenDialogue(View view) {
        clickedButton = ((Button)view).getId();
        DialogFragment imgDialog = new ChooseImage();
        android.app.FragmentManager fm = getFragmentManager();
        imgDialog.show(fm,imageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CAMERA) {
            receivedBitmap = (Bitmap) data.getExtras().get("data");
            //for carrier
            if(clickedButton == 2131492976){
                mImageViewCarrier.setImageBitmap(receivedBitmap);
                String imageNamePNG = imageName+"_carrier.png";
                String state = Environment.getExternalStorageState();
                File file;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
                    directory.mkdirs();
                    file = new File(directory, imageNamePNG);
                    carrierPath = file.toString();
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //for secret
            else if(clickedButton == 2131492977){
                mImageViewSecret.setImageBitmap(receivedBitmap);
                String imageNamePNG = imageName+"_secret.png";
                String state = Environment.getExternalStorageState();
                File file;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
                    directory.mkdirs();
                    file = new File(directory, imageNamePNG);
                    secretPath = file.toString();
                    msgPath = (new File(directory,imageName+"_msg.png")).toString();
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void DoStegnos(View view) {
        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
        secretPath = (new File(directory,imageName+"_secret.png")).toString();
        carrierPath = (new File(directory,imageName+"_carrier.png")).toString();
        msgPath = (new File(directory,imageName+"_msg.png")).toString();

        Encode imageinimage = new Encode();
        imageinimage.Encode(getApplicationContext());
        imageinimage.execute(secretPath, carrierPath, msgPath);

    }
}
