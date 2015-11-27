package com.example.ajoy3.steganography;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class TextInImageStegnos extends FragmentActivity {
    private ImageView mImageView;
    private static final int REQUEST_CAMERA = 1;
    private static String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_image_stegnos);

        mImageView = (ImageView)findViewById(R.id.imageView2);

    }

    public void OpenDialogue(View view) {

        DialogFragment imgDialog = new ChooseImage();
        android.app.FragmentManager fm = getFragmentManager();
        imgDialog.show(fm,imageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(bitmap);
            String state = Environment.getExternalStorageState();
            File file;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/UserName/");
                directory.mkdirs();
                imageName = new Date().getTime()+".jpg";
                file = new File(directory, imageName);
                try {
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void DoStegnos(View view) {

    }


}
