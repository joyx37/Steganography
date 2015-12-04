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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

 public class MainActivity extends Activity {

     private static final int REQUEST_CAMERA = 1;
     public static final int PAIR_DEVICE_REQUEST = 2;
     public static boolean isBluetoothEnabled = false;
     BluetoothAdapter bluetoothAdapter;

     KeyPairDbHandler dbHandler;
     List<PairInformation> myPairedDevicesInfo;
     List<Model> list;
     ListView PairedDeviceListView;

     String UserName;
     String sharedKey;


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
                 newIntent.setClass(MainActivity.this, ShareKeyActivity.class);
                 startActivity(newIntent);

             }
         });

         //Lauch Steganos
         BtnLaunchSteganos.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ChooseTextOrImage(v);
             }
         });

         //Start the decrypt activity
         BtnStartDecryption.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 StartDecryptActivity(v);
             }
         });
    }

     TextView label;
     CheckBox checkbox;

      @Override
     public void onResume()
     {
         super.onResume();
         fetchPairedDeviceData(myPairedDevicesInfo);
     }

     private void fetchPairedDeviceData(List<PairInformation> _myPairedDevicesInfo) {

//         dbHandler.clearDatabase();
         if(_myPairedDevicesInfo.isEmpty()){
             Log.e("STEGANOS", "The List is Empty");
//             Toast.makeText(this, "NO CONNECTIONS TO DEVICES YET! Hit PAIR TO SHARE KEY & CREATE CONNECTIONS!", Toast.LENGTH_LONG).show();
//             dbHandler.clearDatabase(); //adding temporary values to the Db
         }
         else{

             list = new ArrayList<Model>();

             String _deviceNames[] = new String[dbHandler.getPairedDeviceCount()];

             for (int index = 0; index < dbHandler.getPairedDeviceCount(); index++) {
                Log.e("STEGANOS", String.valueOf(_myPairedDevicesInfo.get(index).getId()) + ", " + _myPairedDevicesInfo.get(index).get_nameofpairer() +
                        " , " + _myPairedDevicesInfo.get(index).get_nickname() + " , " + _myPairedDevicesInfo.get(index).get_sharedkey());
                 _deviceNames[index] = _myPairedDevicesInfo.get(index).get_nameofpairer();
             }
             ArrayAdapter<Model> _myData = new MyListAdapter(this, getModel(_deviceNames));

//             ArrayAdapter<String> _myData = new ArrayAdapter<String>(MainActivity.this, R.layout.listviewlayout,_deviceNames);
             PairedDeviceListView.setAdapter(_myData);
             PairedDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Log.e("STEGANOS", "Inside on item click method");

                     label = (TextView) view.getTag(R.id.listlabel);
                     checkbox = (CheckBox) view.getTag(R.id.checkitem);
                     Toggle(checkbox);

                     // TODO: Get name of device and if enabled from each of the selected entries
                     UserName = label.getText().toString();
                     Toast.makeText(MainActivity.this, "Selected Recepient is: " +UserName, Toast.LENGTH_SHORT).show();
                 }
             });

         }
     }

     private void Toggle(CheckBox checkbox) {
        checkbox.setChecked(!checkbox.isChecked());
     }

     private boolean isCheckedOrNot(CheckBox checkBox) {
         return (checkBox.isChecked());
     }

     private List<Model> getModel(String[] stringVals) {
         for (int index = 0; index < stringVals.length; index++){
             list.add(new Model(stringVals[index]));
         }
         return list;
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
         //TODO: Add UserName inside the intent to pass on to the activity, do it only if checked;

         if(isCheckedOrNot(checkbox)){
            //get the String from the checked textview
             Log.e("STEGANOS", "Entering the choose text r image method");

            // Get Key from the database;
             for(int index = 0; index<dbHandler.getPairedDeviceCount(); index++) {
                if( (dbHandler.getAllMyPairedDeviceInfo().get(index).get_nameofpairer()).equals(UserName))
                 {

                     sharedKey = dbHandler.getAllMyPairedDeviceInfo().get(index).get_sharedkey();
//                     Toast.makeText(this, "The shared Key is: " + sharedKey, Toast.LENGTH_SHORT).show();
                     Toast.makeText(this, "The key for "+ UserName +" is chosen!", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(this,ChooseTextorImage.class);
                     intent.putExtra(Constants.CHOSEN_KEY, sharedKey);
                     intent.putExtra(Constants.USER_NAME, UserName);
                     startActivity(intent);
                 }
             }


         }
         else
         {
             Toast.makeText(this, "INVALID SELECTION", Toast.LENGTH_SHORT).show();
         }
     }

     public void StartDecryptActivity(View view) {

         Intent intent = new Intent(this,Decrypt.class);
         intent.putExtra(Constants.USER_NAME, UserName);
         intent.putExtra(Constants.CHOSEN_KEY, sharedKey);
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
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         try {
             super.onActivityResult(requestCode, resultCode, data);

             if (requestCode == PAIR_DEVICE_REQUEST) {
                 if(resultCode == RESULT_OK){
                     //Refresh the paired list devices with the newly paired device
                 }

             }
         } catch (Exception ex) {
             Toast.makeText(this, ex.toString(),
                     Toast.LENGTH_SHORT).show();
         }

     }
 }
