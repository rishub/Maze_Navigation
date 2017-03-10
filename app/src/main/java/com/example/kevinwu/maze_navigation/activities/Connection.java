package com.example.kevinwu.maze_navigation.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kevinwu.maze_navigation.R;
import com.example.kevinwu.maze_navigation.services.BluetoothService;
import com.example.kevinwu.maze_navigation.utils.ConnectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Connection extends AppCompatActivity {

    public static BluetoothAdapter BA;
    Button on, off, visible, listBtn, scan;
    private ListView listView;
    private Set<BluetoothDevice> pairedDevices;
    public static BluetoothSocket clientSocket;
    public static BluetoothSocket serverSocket;
    public static Boolean multiplayerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        BA = BluetoothAdapter.getDefaultAdapter();
        on = (Button) findViewById(R.id.connection_on);
        off = (Button) findViewById(R.id.connection_off);
        listBtn = (Button) findViewById(R.id.connection_list_devices);
        visible = (Button) findViewById(R.id.connection_visible);
        scan = (Button) findViewById(R.id.connection_scan);
        listView = (ListView) findViewById(R.id.myListView);

        clientSocket = null;
        serverSocket = null;
        multiplayerMode = false;
    }

    public void startGame(View view) {
        if(multiplayerMode) {
            Intent serviceIntent = new Intent(this, BluetoothService.class);
            this.startService(serviceIntent);
        }
        Intent intent = new Intent(Connection.this, Game.class);
        startActivity(intent);
    }

    public void on(View view) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View view) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    public void visible(View view) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void scan(View view) {

        Toast.makeText(getApplicationContext(), "Scanning", Toast.LENGTH_LONG).show();

        if(BA.isDiscovering()){
            BA.cancelDiscovery();
        }

        // stores all the found devices into an arraylist, map
        // adds them each time the receiver calls back
        final ArrayList foundDevices = new ArrayList<>();
        final Map<String, BluetoothDevice> devices = new HashMap<>();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                // When discovery finds a device

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the bluetoothDevice object from the Intent
                    // might have to remove final, used for testing purposes
                    final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device != null) {
                        // Get the "RSSI" to get the signal strength as integer,
                        // but should be displayed in "dBm" units
                        int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                        // Create the device object and add it to the arrayList of devices
                        if(device.getName() != null && (device.getName().length() > 0)) {
                            foundDevices.add(device.getName());
                            devices.put(device.getName(), device);

                            // 1. Pass context and data to the custom adapter
                            final ArrayAdapter adapter = new ArrayAdapter(Connection.this, android.R.layout.simple_list_item_1, foundDevices);

                            // 2. setListAdapter
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String deviceName = (String) parent.getItemAtPosition(position);
                                    devices.get(deviceName).createBond();
                                    Toast.makeText(getApplicationContext(), "Attempting to pair with " + deviceName, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    if (foundDevices.isEmpty()) {
                        Toast.makeText(Connection.this, "No Devices", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //goes to app settings and turns on the location so that you can scan.
        int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        BA.startDiscovery();
    }

    public void list(View v) {
        // returns arraylist of paired devices
        pairedDevices = BA.getBondedDevices();

        // copy of paired devices to access in this function for more isolation
        final ArrayList<BluetoothDevice> my_devices = new ArrayList<>();

        // string arraylist for the listview
        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            my_devices.add(bt);
        }

        if (list.size() == 0) {
            Toast.makeText(getApplicationContext(), "No Paired Devices", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int mPosition = position;

                // create an alert dialog for the user to choose which option
                // instantiate buttons for the dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Connection.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_action, null);
                Button unpairBtn = (Button) mView.findViewById(R.id.dialog_action_unpair);
                final Button connectBtn = (Button) mView.findViewById(R.id.dialog_action_connect);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                unpairBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectionUtils.unpairDevice(my_devices.get(mPosition));
                        Toast.makeText(getApplicationContext(), "Unpaired with " + my_devices.get(mPosition).getName(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                connectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        multiplayerMode = true;
                        clientSocket = ConnectionUtils.connectDevice(my_devices.get(mPosition).getAddress(),
                                                                                        getApplicationContext());
                    }
                });

                dialog.show();
            }
        });
    }

}
