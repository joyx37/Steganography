 package com.example.ajoy3.steganography;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

 public class MainActivity extends FragmentActivity {

     private static final int REQUEST_CAMERA = 1;
     public static final int PAIR_DEVICE_REQUEST = 2;
     public static boolean isBluetoothEnabled = false;
     BluetoothAdapter bluetoothAdapter;

     KeyPairDbHandler dbHandler;
     List<PairInformation> myPairedDevicesInfo;
     ListView PairedDeviceListView;


     Button BtnPairDevices;
     Button BtnLaunchSteganos;
     Button BtnStartDecryption;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main_layout);

         //Intilialisation of UI elements
         PairedDeviceListView = (ListView) findViewById(R.id.deviceListView);
         BtnPairDevices = (Button) findViewById(R.id.pairDevicesButton);
         BtnLaunchSteganos = (Button) findViewById(R.id.launchSteganosButton);
         BtnStartDecryption = (Button) findViewById(R.id.decryptImageButton);

         bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         isBluetoothEnabled = bluetoothAdapter.isEnabled();

         //Start thread to fetch the paired devices already saved in the database
         //Update List View with the information fetched from the database
         dbHandler = new KeyPairDbHandler(MainActivity.this, null, null, 1);
         myPairedDevicesInfo = dbHandler.getAllMyPairedDeviceInfo();
         fetchPairedDeviceData(myPairedDevicesInfo);

         //On clicking the pair button, launch the activity to fetch the nearby bluetooth devices and update database
         BtnPairDevices.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Launch the activity for obtaining the device list
                 Intent newIntent = new Intent();
                 newIntent.setClass(MainActivity.this, ConnectActivity.class);
                 startActivityForResult(newIntent, PAIR_DEVICE_REQUEST);

             }
         });

         //Lauch Steganos
         BtnLaunchSteganos.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //ChooseTextOrImage(v);
             }
         });

         //Start the decrypt activity
         BtnStartDecryption.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //StartDecryptActivity(v);
             }
         });
    }

     /**
      * The on-click listener for all devices in the ListViews
      */
     private AdapterView.OnItemClickListener mDeviceClickListener
             = new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
             // Cancel discovery because it's costly and we're about to connect
             bluetoothAdapter.cancelDiscovery();
//
//             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//             BluetoothShareKeyFragment fragment = new BluetoothShareKeyFragment();
//             transaction.replace(R.id.sample_fragment_layout, fragment);
//             transaction.commit();

//             // Get the device MAC address, which is the last 17 chars in the View
//             String info = ((TextView) v).getText().toString();
//             String address = info.substring(info.length() - 17);
//
//             // Create the result Intent and include the MAC address
//             Intent intent = new Intent();
//             intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
//             // Set result and finish this Activity
//             setResult(Activity.RESULT_OK, intent); //in the receiving activity, add the details to the database
//             finish();
         }
     };
     @Override
     public void onResume()
     {
         super.onResume();
         fetchPairedDeviceData(myPairedDevicesInfo);
     }

     private void fetchPairedDeviceData(List<PairInformation> _myPairedDevicesInfo) {

         if(_myPairedDevicesInfo.isEmpty()){
             Log.e("STEGANOS", "The List is Empty");
             dbHandler.testInitialization(); //adding temporary values to the Db
         }
         else{
             String _deviceNames[] = new String[dbHandler.getPairedDeviceCount()];

             for (int index = 0; index < dbHandler.getPairedDeviceCount(); index++) {
                Log.e("STEGANOS", String.valueOf(_myPairedDevicesInfo.get(index).getId()) + ", " + _myPairedDevicesInfo.get(index).get_nameofpairer() +
                        " , " + _myPairedDevicesInfo.get(index).get_nickname() + " , " + _myPairedDevicesInfo.get(index).get_sharedkey());
                 _deviceNames[index] = _myPairedDevicesInfo.get(index).get_nameofpairer();
             }
             ArrayAdapter<String> _myData = new ArrayAdapter<String>(this, R.layout.listviewlayout, _deviceNames);
             PairedDeviceListView.setAdapter(_myData);
             PairedDeviceListView.setOnItemClickListener(mDeviceClickListener);
         }
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

     public void ChooseTextOrImage(View view) {
         Intent intent = new Intent(this,ChooseTextorImage.class);
         startActivity(intent);
     }

     public void StartDecryptActivity(View view) {
         Intent intent = new Intent(this,Decrypt.class);
         startActivity(intent);
     }

     @Override
     public void onPause()
     {
         super.onPause();
         //save the information in a bundle
     }

     @Override
     public void onDestroy()
     {
         super.onDestroy();

         if(bluetoothAdapter.isEnabled())
             bluetoothAdapter.disable();
     }
 }
