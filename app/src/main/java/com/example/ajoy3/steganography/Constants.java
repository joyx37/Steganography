package com.example.ajoy3.steganography;

/**
 * Created by ajoy3 on 11/28/2015.
 */
public class Constants {
    public static final int REQUEST_SDCARD = 1;
    public static final int REQUEST_CAMERA = 2;
    public static final int TEXT_IN_IMAGE = 333;
    public static final int IMAGE_IN_IMAGE = 444;
    public static final float CARRIER_UP_SCALE = 2.0f;
    public static final float SECRET_DOWN_SCALE = 0.99f;
    public static final float DOWN_SCALE = 0.97f;
    public static final int FIVE_MP = 5000000;
    public static final int ONE_MP = 1000000;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Key storage
    public static final String KEY_LIST="/sdcard";

    public static final String CHOSEN_KEY = "sharedkey";
    public static final String USER_NAME = "username";
    public static final String BUNDLE_ID = "KEYVALUES";
}
