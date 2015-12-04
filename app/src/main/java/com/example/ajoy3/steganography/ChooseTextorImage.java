package com.example.ajoy3.steganography;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ChooseTextorImage extends AppCompatActivity {

    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_textor_image);

        extras = getIntent().getExtras();
        Log.e("STEGANOS", extras.getString(Constants.USER_NAME));
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    public void TextInImageStegnos(View view) {

        Intent intent = new Intent(this,TextInImageStegnos.class);
        intent.putExtra(Constants.USER_NAME, extras.getString(Constants.USER_NAME));
        intent.putExtra(Constants.CHOSEN_KEY, extras.getString(Constants.CHOSEN_KEY));
        startActivity(intent);
    }

    public void ImageInImageStegnos(View view) {
        Intent intent = new Intent(this,ImageInImageStegnos.class);
        intent.putExtra(Constants.USER_NAME, extras.getString(Constants.USER_NAME));
        startActivity(intent);
    }
}
