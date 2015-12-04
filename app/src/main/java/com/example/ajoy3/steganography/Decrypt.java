package com.example.ajoy3.steganography;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

public class Decrypt extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;
    private static Boolean success = false;
    private static Bitmap receivedBitmap;
    private static String imageName,encodedImageName,decodedImageName;
    private static String username;
    private static String chosenkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

//        Log.e("STEGANOS", extras.getString(Constants.USER_NAME));
//        Log.e("STEGANOS_KEY", extras.getString(Constants.CHOSEN_KEY));
        username = extras.getString(Constants.USER_NAME);
        chosenkey = extras.getString(Constants.CHOSEN_KEY);

        mImageView = (ImageView)findViewById(R.id.imageView);
        mTextView = (TextView)findViewById(R.id.textView);

        imageName = Long.toString(new Date().getTime());
    }

    public void Decrypt(View view) {
        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Received/"+username+"_received/");
        directory.mkdirs();

        int encodedWidth = receivedBitmap.getWidth();
        int encodedHeight = receivedBitmap.getHeight();
        int code = getHiddenInt(receivedBitmap.getPixel(encodedWidth - 1, encodedHeight - 1));

        success = false;
        if(code == Constants.TEXT_IN_IMAGE) {
            int pixelInt;
            int msgSize;
            char msgCharArray[] = null;
            for (int i = 0; (i < encodedHeight) && (!success); i++) {
                for (int j = 0; (j < encodedWidth) && (!success); j++) {
                    int pixel = receivedBitmap.getPixel(j, i);
                    pixelInt = getHiddenInt(pixel);
                    if ((i == 0 && j == 0)) {
                        msgSize = pixelInt;
                        msgCharArray = new char[msgSize];
                    }
                    else if ((pixelInt == Constants.TEXT_IN_IMAGE)) {
                        String msg = new String(msgCharArray);
                        Log.d("before",msg);
//                        String key = "0D900E9E007903B2128BFB0D99DEC719";
                        String key = getIntent().getExtras().getString(Constants.CHOSEN_KEY);
                        Log.e("THE STEGANOS KEY", "key: "+key);
                        try {
                            //key="0D900E9E007903B2128BFB0D99DEC719";
                            mTextView.setText(AES.decrypt(key,msg));
                            //mTextView.setText(msg);
                        } catch (Exception e) {
                            Toast.makeText(this,"Error in Decryption Key",Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                        success = true;
                    }
                    else {
                        msgCharArray[(i * encodedWidth) + j - 1] = (char)pixelInt;
                    }
                }
            }
        }
        else if(code == Constants.IMAGE_IN_IMAGE){
            Decode imageInImage = new Decode();
            imageInImage.Decode(getApplicationContext(),mImageView);

            Toast.makeText(getApplicationContext(),"Image Decoding Started",Toast.LENGTH_SHORT).show();
            imageInImage.execute(encodedImageName,decodedImageName);
        }
    }

    public static int getHiddenInt(int pixelColor){
        int[] digits = new int[4];
        digits[0] = intToDigits(Color.red(pixelColor))[2];
        digits[1] = intToDigits(Color.green(pixelColor))[2];
        digits[2] = intToDigits(Color.blue(pixelColor))[2];

        return digitsToInt(digits);
    }

    public static int digitsToInt(int[] digits){
        int a = (digits[0] * 100) + (digits[1] * 10) + digits[2];
        return (digits[0] * 100) + (digits[1] * 10) + digits[2];

    }

    public static int[] intToDigits(int aNumber){
        int[] digits = new int[3];
        digits[0] = aNumber / 100;
        digits[1] = (aNumber % 100) / 10;
        digits[2] = aNumber % 10;
        return digits;

    }

    public void OpenEncodedImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.REQUEST_SDCARD);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == Constants.REQUEST_SDCARD) {
            Uri carrierImageURI = intent.getData();
            try {
                InputStream carrierImageStream = getContentResolver().openInputStream(carrierImageURI);
                receivedBitmap = BitmapFactory.decodeStream(carrierImageStream);
                mImageView.setImageBitmap(receivedBitmap);

                String encodedImageNamePNG = imageName+"_msg.png";
                String decodedImageNamePNG = imageName+"_decoded.png";
                String state = Environment.getExternalStorageState();
                File file;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Received/"+username+"_received/");//change username to variable
                    directory.mkdirs();
                    file = new File(directory, encodedImageNamePNG);
                    encodedImageName = file.toString();

                    decodedImageName = (new File(directory,decodedImageNamePNG)).toString();

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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
