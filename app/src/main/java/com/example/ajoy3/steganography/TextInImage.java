 package com.example.ajoy3.steganography;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

 public class TextInImage extends AppCompatActivity {

     private static final int REQUEST_CAMERA = 1;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_in_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_in_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         // TODO Auto-generated method stub
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == REQUEST_CAMERA) {
             Bitmap bitmap = (Bitmap) data.getExtras().get("data");
             String imageName = Long.toString(new Date().getTime()) + ".jpg";
             String state = Environment.getExternalStorageState();
             File file;
             if (Environment.MEDIA_MOUNTED.equals(state)) {
                 File directory = new File(Environment.getExternalStorageDirectory()+"/Steganos/Images/Sent/");
                 directory.mkdirs();
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


     public void SettingOnClick(MenuItem item) {

    }

     public void PairDevicesOnClick(MenuItem item) {
     }
 }
