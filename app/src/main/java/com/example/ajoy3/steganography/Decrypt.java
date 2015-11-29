package com.example.ajoy3.steganography;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Decrypt extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView)findViewById(R.id.imageView);
        mTextView = (TextView)findViewById(R.id.textView);
    }

    public void Decrypt(View view) {
        File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
        directory.mkdirs();
        String imageName = "msg";
        String msgImage = imageName+"_msg"+".png";
        File file = new File(directory, msgImage);
        Bitmap receivedBitmap = BitmapFactory.decodeFile(file.toString());
        mImageView.setImageBitmap(receivedBitmap);
        //decode
        int w = receivedBitmap.getWidth();
        int h = receivedBitmap.getHeight();
        int msgSize = receivedBitmap.getPixel(w-1,h-1)&(~Constants.MSG_SIZE_MASK);
        int code = receivedBitmap.getPixel(w-2,h-2)&(~Constants.CODE_MASK);

        if(code == Constants.TEXT_IN_IMAGE) {
            Toast.makeText(this,"Decrypting For Text In Image",Toast.LENGTH_SHORT).show();
            char msgCharArray[] = new char[msgSize];
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    if (((h + 1) * i) + j < msgSize) {
                        int pixel = receivedBitmap.getPixel(i, j);
                        int temp = (pixel & (~Constants.COLOR_MASK));
                        msgCharArray[((h + 1) * i) + j] = (char) temp;
                    } else
                        break;
                }
            }
            Toast.makeText(this,"Decrypting Complete",Toast.LENGTH_SHORT).show();
            String msg = new String(msgCharArray);
            mTextView.setText(msg);
        }
    }
}
