package com.example.ajoy3.steganography;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * Created by ajoy3 on 11/28/2015.
 */
public class Encode extends AsyncTask<String,Void,Void> {

    private static Boolean success = false;
    private static Context mContext;

    public void Encode(Context context){
        mContext = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        String hideThis = params[0];
        String hideIn = params[1];
        String writeTo = params[2];

        encodePicture(hideThis,hideIn,writeTo);
        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        if(success)
            Toast.makeText(mContext,"Stegnos Complete",Toast.LENGTH_SHORT).show();
    }

        public static void encodePicture(String hideThis,String hideIn,String writeTo){
        Bitmap imageToHide,placeToHide;

        imageToHide = BitmapFactory.decodeFile(hideThis);
        imageToHide = imageToHide.copy(imageToHide.getConfig(), true);
        placeToHide = BitmapFactory.decodeFile(hideIn);
        placeToHide = placeToHide.copy(placeToHide.getConfig(),true);

        int scaleFactor = 2;
        while(((3 * imageToHide.getWidth() * imageToHide.getHeight()) + 2 > ((scaleFactor * scaleFactor * placeToHide.getHeight() * placeToHide.getWidth())))){
            scaleFactor += 1;
        }

        placeToHide = scaleUp(placeToHide, scaleFactor);
        placeToHide = placeToHide.copy(placeToHide.getConfig(),true);

        int imageWidth = imageToHide.getWidth();
        int imageHeight = imageToHide.getHeight();
        int[] imagePixels = new int[3 * imageWidth * imageHeight];
        int[] currentPixelData;
        for (int j = 0; j < imageWidth; j++){
            for (int i = 0; i < imageHeight; i++){
                currentPixelData = getPixelData(imageToHide.getPixel(j,i));
                imagePixels[3 * ((j * imageHeight) + i)] = (currentPixelData[0]);
                imagePixels[3 * ((j * imageHeight) + i) + 1] = (currentPixelData[1]);
                imagePixels[3 * ((j * imageHeight) + i) + 2] = (currentPixelData[2]);
            }
        }

        byte[] key = AES.generateKey();
        imagePixels = convertToInt(AES.encrypt(key,convertToBytes(imagePixels)));

        int placeWidth = placeToHide.getWidth();
        int placeHeight = placeToHide.getHeight();
        int color;
        for(int j = 0; j < placeWidth; j++){
            for (int i = 0; i < placeHeight; i++){
                if((i == 0) && (j == 0)){
                    color = changeColor(placeToHide.getPixel(0,0),imageWidth);
                    placeToHide.setPixel(0,0,color);
                }
                else if((i == 1) && (j == 0)){
                    placeToHide.setPixel(0,1,changeColor(placeToHide.getPixel(0,1),imageHeight));
                }
                else if((j * placeHeight) + i == imagePixels.length + 2){
                    placeToHide.setPixel(j,i,changeColor(placeToHide.getPixel(j,i),423));
                    try{
                        FileOutputStream output = new FileOutputStream(writeTo);
                        placeToHide.compress(Bitmap.CompressFormat.PNG,100,output);
                        output.close();
                        success = true;
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    return;
                }
                else{
                    placeToHide.setPixel(j,i,changeColor(placeToHide.getPixel(j,i),imagePixels[(j * placeHeight) + i - 2]));
                }
            }
        }
        return;
    }

    public static int[] getPixelData(int color){

        int[] pixelData = new int[3];
        pixelData[0] = Color.red(color);
        pixelData[1] = Color.green(color);
        pixelData[2] = Color.blue(color);
        return pixelData;

    }

    public static byte[] convertToBytes(int[] ints){
        byte[] bytes = new byte[ints.length];

        for (int i=0; i<ints.length; i++) {
            bytes[i] = (byte) ((ints[i]) & 0xFF);
        }
        return bytes;

    }

    public static int[] convertToInt(byte[] bytes){
        int[] ints = new int[bytes.length];
        for (int i = 0; i < ints.length; i += 1){
            ints[i] = bytes[i]&0xFF;
        }
        return ints;

    }

    public static Bitmap scaleUp(Bitmap original,int scaleFactor){

        int[] color = new int[scaleFactor * scaleFactor * original.getWidth() * original.getHeight()];
        for (int i = 0; i < original.getHeight(); i += 1){
            for (int j = 0; j < original.getWidth(); j += 1){
                for (int k = 0; k < scaleFactor; k += 1){
                    for (int l = 0; l < scaleFactor; l += 1){
                        color[(((i * scaleFactor) + k) * original.getWidth()) + (j * scaleFactor) + l] = original.getPixel(j,i);
                    }
                }
            }
        }
        original.recycle();
        return Bitmap.createBitmap(color, scaleFactor * original.getWidth(), scaleFactor * original.getHeight(), original.getConfig());

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
