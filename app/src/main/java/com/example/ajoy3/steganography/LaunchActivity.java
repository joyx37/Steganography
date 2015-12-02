package com.example.ajoy3.steganography;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class LaunchActivity extends Activity {

    private Handler mHandler = new Handler();
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        Log.e("LocatioNinja", "In oncreate of launch activity");

        mHandler.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(LaunchActivity.this, "WELCOME!", Toast.LENGTH_SHORT).show();
//                Log.e("LocatioNinja", "Out of launch activity");

                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
            }
        }, 2500);
    }

}
