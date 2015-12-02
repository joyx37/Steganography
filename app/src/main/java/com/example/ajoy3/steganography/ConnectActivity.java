package com.example.ajoy3.steganography;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ConnectActivity extends FragmentActivity {

    private static final String TAG = "ConnectActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter; //newly discovered devices
    ArrayAdapter<String> pairedDevicesArrayAdapter;

    LinearLayout LayoutPairedDeviceList; //to populate the list of already paired devices by bluetooth
    LinearLayout LayoutAvailableDeviceList; //to populate the list of newly discovered devices
    ListView pairedListView;
    ListView newDevicesListView;
    Button BtnFindNewDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_connect);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        //Initialize UI elements for the List Views and Buttons to add new devices
        pairedListView = (ListView) findViewById(R.id.pairedDevices);
        newDevicesListView = (ListView) findViewById(R.id.discoveredDevices);
        BtnFindNewDevices = (Button) findViewById(R.id.buttonScanNewDevices);
        LayoutPairedDeviceList = (LinearLayout) findViewById(R.id.pairedDeviceListLayout);
        LayoutAvailableDeviceList = (LinearLayout) findViewById(R.id.availableDeviceListLayout);

        //Set-up the search for the newly discovered devices via bluetooth
        BtnFindNewDevices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.listviewlayout);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.listviewlayout);

        // Find and set up the ListView for paired devices
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        //set bluetooth as enabled
        if(!MainActivity.isBluetoothEnabled)
            setBluetooth(true);

        // Register for broadcasts when a device is discovered
        IntentFilter iFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, iFilter);

       // iFilter.addAction(BluetoothDevice.ACTION_FOUND);
        iFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, iFilter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        getPairedDeviceList();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPairedDeviceList();
    }

    public void getPairedDeviceList(){
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.e(TAG, "Inside do discovery method");
        LayoutAvailableDeviceList.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
        setProgressBarIndeterminateVisibility(true);
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("STEGANOS", "Inside the onreceive method");
            String action = intent.getAction();
             // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Device found", Toast.LENGTH_SHORT).show();
                    }
                });
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = (BluetoothDevice)intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed alreadsy
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Discovery Finished!", Toast.LENGTH_SHORT).show();
                    }
                });
                //setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            BluetoothShareKeyFragment fragment = new BluetoothShareKeyFragment();
//            transaction.replace(R.id.sample_fragment_layout, fragment);
//            transaction.commit();

            //Share key fragment activity to be started as an alert dialog and send key to the paired or discovered device
            showFragmentForSharingKey();


            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent); //in the receiving activity, add the details to the database
            finish();
        }
    };

    private void showFragmentForSharingKey() {
        //Implement a dialogFragment which will let user enter the key for sharing and storing on to database

    }

    public boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
           // mBtAdapter.disable();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }


}

