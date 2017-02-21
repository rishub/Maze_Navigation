package com.example.kevinwu.maze_navigation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Connection extends AppCompatActivity {

    private BluetoothAdapter BA;
    private BluetoothSocket deviceConnection = null;
    Button on, off, visible, listBtn, scan;
    //private TextView statusText;
    private ListView listView;
    private Set<BluetoothDevice> pairedDevices;

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
    }

    public void startGame(View view) {
        Intent intent = new Intent(Connection.this, Game.class);
        startActivity(intent);
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(Connection.this, Menu.class);
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
        final ArrayList foundDevices = new ArrayList<>();

        BA.startDiscovery();

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

                    // Get the "RSSI" to get the signal strength as integer,
                    // but should be displayed in "dBm" units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                    // Create the device object and add it to the arrayList of devices
                    foundDevices.add(device.getName());

                    // 1. Pass context and data to the custom adapter
                    final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, foundDevices);

                    // 2. setListAdapter
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            device.createBond();
                            Toast.makeText(getApplicationContext(), "Attempting to pair with " + device.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }

    public void list(View v) {
        pairedDevices = BA.getBondedDevices();

        final ArrayList<BluetoothDevice> my_devices=  new ArrayList<>();
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Connection.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_action, null);
                Button unpairBtn = (Button) mView.findViewById(R.id.dialog_action_unpair);
                final Button connectBtn = (Button) mView.findViewById(R.id.dialog_action_connect);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                unpairBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unpairDevice(my_devices.get(mPosition));
                        Toast.makeText(getApplicationContext(), "Unpaired with " + my_devices.get(mPosition).getName(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                connectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectDevice(my_devices.get(mPosition).getAddress());
                        Toast.makeText(getApplicationContext(), "Connected with " + my_devices.get(mPosition).getName(), Toast.LENGTH_SHORT).show();
                        connectBtn.setText("Connected");
                        connectBtn.setClickable(false);
                    }
                });

                dialog.show();
            }
        });
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("KEVIN", e.getMessage());
        }
    }

    private void connectDevice(String address) {
        BluetoothDevice device = BA.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            tmp = (BluetoothSocket) m.invoke(device, 1);
        } catch (IOException e) {
            Log.d("KEVIN", "create() failed", e);
        } catch (Exception e) {
            Log.d("KEVIN", "failed to do reflection");
        }
        deviceConnection = tmp;
        try {
            deviceConnection.connect();
        } catch(Exception e) {
            Log.d("KEVIN", "failed to make connection with device");
        }
    }
}
