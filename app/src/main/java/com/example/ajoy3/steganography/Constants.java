package com.example.ajoy3.steganography;

/**
 * Created by ajoy3 on 11/28/2015.
 */
public class Constants {
    public static final int REQUEST_CAMERA = 1;
    public static final int TEXT_IN_IMAGE = 10;//1010 - Code
    public static final int IMAGE_IN_IMAGE = 5;//0101 - Code
    public static final int CODE_MASK = 0xFFFFFFF0;
    public static final int COLOR_MASK = 0xFFFFFF00;
    public static final int MSG_SIZE_MASK = 0xFFFFFF00;

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
}
