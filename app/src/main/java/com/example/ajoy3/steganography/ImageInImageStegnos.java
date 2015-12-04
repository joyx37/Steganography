package com.example.ajoy3.steganography;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ImageInImageStegnos extends AppCompatActivity {

    private static String imageName;
    private static String carrierPath,secretPath,msgPath;
    private static int clickedButton;
    private static Bitmap receivedBitmap;
    private ImageView mImageViewCarrier,mImageViewSecret;
    private static int cW,cH,sW,sH;

    public static String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_in_image_stegnos);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        Log.e("STEGANOS", extras.getString(Constants.USER_NAME));
        username = extras.getString(Constants.USER_NAME);

        mImageViewCarrier = (ImageView)findViewById(R.id.imageView3);
        mImageViewSecret = (ImageView)findViewById(R.id.imageView4);
        imageName = Long.toString(new Date().getTime());
    }

    public void OpenDialogue(View view) {
        clickedButton = ((Button)view).getId();
        DialogFragment imgDialog = new ChooseImage();
        android.app.FragmentManager fm = getFragmentManager();
        imgDialog.show(fm, imageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        File file = null;
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.REQUEST_CAMERA) {
            receivedBitmap = (Bitmap) intent.getExtras().get("data");
            //for carrier
            if(clickedButton == R.id.button6){
                mImageViewCarrier.setImageBitmap(receivedBitmap);
                String imageNamePNG = imageName+"_carrier.png";
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Tmp/");
                    directory.mkdirs();
                    file = new File(directory, imageNamePNG);
                    carrierPath = file.toString();
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        receivedBitmap = Bitmap.createScaledBitmap(receivedBitmap,(int)(Constants.CARRIER_UP_SCALE*receivedBitmap.getWidth()),
                                (int)(Constants.CARRIER_UP_SCALE*receivedBitmap.getHeight()),true);
                        receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            //for secret
            else if(clickedButton == R.id.button11){
                mImageViewSecret.setImageBitmap(receivedBitmap);
                String imageNamePNG = imageName+"_secret.png";
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Tmp/");
                    directory.mkdirs();
                    file = new File(directory, imageNamePNG);
                    secretPath = file.toString();

                    File msgImgdirectory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/"+username+"_sent/");//change username to variable
                    msgImgdirectory.mkdirs();
                    msgPath = (new File(msgImgdirectory,imageName+"_msg.png")).toString();
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        receivedBitmap = Bitmap.createScaledBitmap(receivedBitmap,(int)(Constants.SECRET_DOWN_SCALE*receivedBitmap.getWidth()),
                                (int)(Constants.SECRET_DOWN_SCALE*receivedBitmap.getHeight()),true);
                        receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(requestCode == Constants.REQUEST_SDCARD){
            Uri carrierImageURI = intent.getData();
            try {
                InputStream receivedImageStream = getContentResolver().openInputStream(carrierImageURI);
                receivedBitmap = BitmapFactory.decodeStream(receivedImageStream);

                //for carrier
                if(clickedButton == R.id.button6){
                    //make bitmap less than 5MP
                    int w = receivedBitmap.getWidth();
                    int h = receivedBitmap.getHeight();
                    int size = w*h;
                    while(size > Constants.FIVE_MP ){
                        w = (int)(w*Constants.DOWN_SCALE);
                        h = (int)(h*Constants.DOWN_SCALE);
                        size = w*h;
                    }
                    receivedBitmap = Bitmap.createScaledBitmap(receivedBitmap, w, h, true);

                    mImageViewCarrier.setImageBitmap(receivedBitmap);
                    String imageNamePNG = imageName+"_carrier.png";
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Tmp/");
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
                else if(clickedButton == R.id.button11){
                    //make bitmap less than 1MP
                    int w = receivedBitmap.getWidth();
                    int h = receivedBitmap.getHeight();
                    int size = w*h;
                    while(size > Constants.ONE_MP ){
                        w = (int)(w*Constants.DOWN_SCALE);
                        h = (int)(h*Constants.DOWN_SCALE);
                        size = w*h;
                    }
                    receivedBitmap = Bitmap.createScaledBitmap(receivedBitmap, w, h, true);

                    mImageViewSecret.setImageBitmap(receivedBitmap);
                    String imageNamePNG = imageName+"_secret.png";
                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Tmp/");
                        directory.mkdirs();
                        file = new File(directory, imageNamePNG);
                        secretPath = file.toString();

                        File msgImgdirectory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/"+username+"_sent/");//change username to variable
                        msgImgdirectory.mkdirs();
                        msgPath = (new File(msgImgdirectory,imageName+"_msg.png")).toString();
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
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void DoStegnos(View view) {

        Encode imageinimage = new Encode();
        imageinimage.Encode(getApplicationContext());

        Toast.makeText(getApplicationContext(),"Stegnos Started",Toast.LENGTH_SHORT).show();
        imageinimage.execute(secretPath, carrierPath, msgPath);

    }
}
