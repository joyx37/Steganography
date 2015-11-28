package com.example.ajoy3.steganography;

/**
 * Created by ajoy3 on 11/28/2015.
 */
public class Constants {
    public static final int REQUEST_CAMERA = 1;
    public static final int TEXT_IN_IMAGE = 10;//1010 - Code
    public static final int IMAGE_IN_IMAGE = 5;//0101 - Code
    public static final int CODE_MASK = 0xFFFFFFF0;
    public static final int PIXEL_MASK = 0xFFFFFF00;
    public static final int MSG_SIZE_MASK = 0xFFFFFF00;
}
