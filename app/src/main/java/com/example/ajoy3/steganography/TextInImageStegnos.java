package com.example.ajoy3.steganography;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class TextInImageStegnos extends FragmentActivity {
    private ImageView mImageView;
    private EditText mEditText;
    private static String imageName;
    private static Bitmap carrierBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_image_stegnos);

        mImageView = (ImageView)findViewById(R.id.imageView2);
        mEditText = (EditText) findViewById(R.id.editText);

    }

    public void OpenDialogue(View view) {

        DialogFragment imgDialog = new ChooseImage();
        android.app.FragmentManager fm = getFragmentManager();
        imgDialog.show(fm,imageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CAMERA) {
            carrierBitmap = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(carrierBitmap);
            String state = Environment.getExternalStorageState();
            File file;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
                directory.mkdirs();
                imageName = Long.toString(new Date().getTime());
                String imageNamePNG = imageName+".png";
                file = new File(directory, imageNamePNG);
                try {
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    carrierBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void DoStegnos(View view) {
        int w = carrierBitmap.getWidth();
        int h = carrierBitmap.getHeight();
        Bitmap msgBitmap = Bitmap.createBitmap(w, h, carrierBitmap.getConfig());
        String msg = mEditText.getText().toString();
        char msgArray[] = msg.toCharArray();
        int msgSize = msgArray.length;

        for(int i=0;i<w-2;i++)
            for(int j=0;j<h-2;j++){
                if(((h+1)*i)+j < msgSize){
                    int pixelColor = carrierBitmap.getPixel(i, j);
                    int temp = (pixelColor&Constants.COLOR_MASK)+msgArray[((h+1)*i)+j];
                    msgBitmap.setPixel(i, j, temp);
                }
                else
                    msgBitmap.setPixel(i, j, carrierBitmap.getPixel(i, j));
            }
        msgBitmap.setPixel(w-2,h-2,(carrierBitmap.getPixel(w-2,h-2)&Constants.CODE_MASK)+Constants.TEXT_IN_IMAGE);
        msgBitmap.setPixel(w - 1, h - 1, (carrierBitmap.getPixel(w - 1, h - 1) & Constants.MSG_SIZE_MASK) + msgSize);

        String state = Environment.getExternalStorageState();
        File file;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
            directory.mkdirs();
            imageName = "msg";
            String msgImage = imageName+"_msg"+".png";
            file = new File(directory, msgImage);
            try {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                msgBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,"Message Image Stored",Toast.LENGTH_SHORT).show();
    }


}
