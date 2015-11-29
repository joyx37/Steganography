package com.example.ajoy3.steganography;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ajoy3 on 11/28/2015.
 */
public class AES {
    @SuppressWarnings("null")
    public static byte[] encrypt(byte [] key, byte[] input){
        byte[] output = input;
        SecretKeySpec test = new SecretKeySpec(key, "AES");
        try {
            Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
            aes.init(0, test);
            output = aes.doFinal(input);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

    @SuppressWarnings("null")
    public static byte[] decrypt(byte [] key, byte[] input){
        Cipher aes = null;
        byte[] output = null;
        SecretKeySpec test = new SecretKeySpec(key, "AES");
        try {
            aes.init(1, test);
            output = aes.doFinal(input);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

    public static byte[] generateKey(){
        KeyGenerator kgen = null;
        try {
            //Use AES encryption
            kgen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Use 192 bit AES encryption
        kgen.init(192);
        //Generate the Key then store it in aeskey
        SecretKey aeskey = kgen.generateKey();
        //Put that key in a byte[]
        byte [] encoded = aeskey.getEncoded();
        return encoded;
    }
}
