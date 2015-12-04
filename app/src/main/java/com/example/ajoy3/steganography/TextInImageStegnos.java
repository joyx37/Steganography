package com.example.ajoy3.steganography;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

public class TextInImageStegnos extends FragmentActivity {
    private ImageView mImageView;
    private EditText mEditText;
    private static String imageName;
    private static Bitmap carrierBitmap;
    private static Boolean success = false;
    public static String chosenKey = null;
    public static String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_image_stegnos);

        mImageView = (ImageView)findViewById(R.id.imageView2);
        mEditText = (EditText) findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();

        Log.e("STEGANOS", extras.getString(Constants.USER_NAME));
        Log.e("STEGANOS_KEY", extras.getString(Constants.CHOSEN_KEY));
        username = extras.getString(Constants.USER_NAME);
        chosenKey= extras.getString(Constants.CHOSEN_KEY);
    }

    public void OpenDialogue(View view) {

        DialogFragment imgDialog = new ChooseImage();
        android.app.FragmentManager fm = getFragmentManager();
        imgDialog.show(fm, imageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Constants.REQUEST_CAMERA) {
            carrierBitmap = (Bitmap) intent.getExtras().get("data");
            mImageView.setImageBitmap(carrierBitmap);
            imageName = Long.toString(new Date().getTime());

        }
        else if(requestCode == Constants.REQUEST_SDCARD){
            Uri carrierImageURI = intent.getData();
            try {
                InputStream carrierImageStream = getContentResolver().openInputStream(carrierImageURI);
                carrierBitmap = BitmapFactory.decodeStream(carrierImageStream);

                //make bitmap less than 5MP
                int w = carrierBitmap.getWidth();
                int h = carrierBitmap.getHeight();
                int size = w*h;
                while(size > Constants.FIVE_MP ){
                    w = (int)(w*Constants.DOWN_SCALE);
                    h = (int)(h*Constants.DOWN_SCALE);
                    size = w*h;
                }
                carrierBitmap = Bitmap.createScaledBitmap(carrierBitmap,w,h,true);

                mImageView.setImageBitmap(carrierBitmap);
                imageName = Long.toString(new Date().getTime());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void DoStegnos(View view) {
        int carrierWidth = carrierBitmap.getWidth();
        int carrierHeight = carrierBitmap.getHeight();
        Bitmap msgBitmap = carrierBitmap.copy(carrierBitmap.getConfig(), true);

        String key;
        String msg = mEditText.getText().toString();

        try {
//            key= AES.randomkey();key = "0D900E9E007903B2128BFB0D99DEC719";
            Log.d("key: ", chosenKey);
            key = chosenKey;
            Log.d("read msg",msg);
            //key="0D900E9E007903B2128BFB0D99DEC719";
            msg = (AES.encrypt(key, msg));
            Log.d("encrypted msg",msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        char msgArray[] = msg.toCharArray();
        int msgSize = msgArray.length;

        int color;

        //store code for text in image in last pixel
        color = changeColor(msgBitmap.getPixel(carrierWidth-1,carrierHeight-1),Constants.TEXT_IN_IMAGE);
        msgBitmap.setPixel(carrierWidth - 1, carrierHeight - 1, color);

        success = false;
        for(int i = 0; (i < carrierHeight)&&(!success); i++){
            for (int j = 0; (j < carrierWidth)&&(!success); j++){
                if((i == 0) && (j == 0)){
                    color = changeColor(msgBitmap.getPixel(j,i),msgSize);
                    msgBitmap.setPixel(j,i,color);
                }
                else if((((i * carrierWidth) + j) == msgSize +1)){
                    color = changeColor(msgBitmap.getPixel(j,i),Constants.TEXT_IN_IMAGE);
                    msgBitmap.setPixel(j, i,color);
                    String state = Environment.getExternalStorageState();
                    File file;
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/"+username+"_sent/");
                        directory.mkdirs();
                        //imageName = "msg";
                        String msgImage = imageName+"_msg"+".png";
                        file = new File(directory, msgImage);
                        try {
                            file.createNewFile();
                            FileOutputStream out = new FileOutputStream(file);
                            msgBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                            success = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(success) {
                            Toast.makeText(this, "Message Image Stored", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(this, "Message Image Store Failed", Toast.LENGTH_SHORT).show();
                        }
                        MediaScannerConnection.scanFile(getApplicationContext(),
                                new String[]{file.toString()}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> uri=" + uri);
                                    }
                                });
                    }
                }
                else{
                    msgBitmap.setPixel(j,i, changeColor(msgBitmap.getPixel(j,i), msgArray[(i * carrierWidth) + j - 1]));
                }
            }
        }
    }

    public static int changeColor(int currentColor,int inputNumber){

        int[] number = intToDigits(inputNumber);

        int alpha = Color.alpha(currentColor);

        int[] red = intToDigits(Color.red(currentColor));
        red[2] = number[0];

        int[] green = intToDigits(Color.green(currentColor));
        green[2] = number[1];

        int[] blue = intToDigits(Color.blue(currentColor));
        blue[2] = number[2];

        if(number[2] > 5){
            if(blue[0]==2 && blue[1]==5){
                blue[1] = 4;
            }
        }
        if(number[1] > 5){
            if (red[0] == 2 && red[1] == 5) {
                red[1] = 4;
            }
        }
        if(number[0] > 5){
            if(green[0]==2 && green[1]==5){
                green[1] = 4;
            }
        }

        return Color.argb(alpha, digitsToInt(red),digitsToInt(green),digitsToInt(blue));

    }

    public static int[] intToDigits(int aNumber){

        int[] digits = new int[3];
        digits[0] = aNumber / 100;
        digits[1] = (aNumber % 100) / 10;
        digits[2] = aNumber % 10;
        return digits;

    }

    public static int digitsToInt(int[] digits){

        return (digits[0] * 100) + (digits[1] * 10) + digits[2];

    }


}
