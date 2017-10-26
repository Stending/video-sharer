package com.example.android.networkconnect;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ShareWithPairedDevicesActivity extends FragmentActivity{

    private static final int REQUEST_ENABLE_BT = 1;
    private Button onBtn;
    private Button offBtn;
    private Button listBtn;

    private String defaultRepertory = "/data/user/0/com.example.android.networkconnect/cache/";

    private TextView text;
    private TextView textFileName;
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    private static String DEBUGING = "COUCOUBT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUGING, "entering onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_paired_main);

        // take an instance of BluetoothAdapter - Bluetooth radio
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            listBtn.setEnabled(false);

            text.setText("Status: not supported");

            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
        } else {
            text = (TextView) findViewById(R.id.text);
            textFileName = (TextView)findViewById(R.id.text_file_name);

            onBtn = (Button)findViewById(R.id.turnOn);
            onBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    on(v);
                }
            });

            offBtn = (Button)findViewById(R.id.turnOff);
            offBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    off(v);
                }
            });

            listBtn = (Button)findViewById(R.id.paired);
            listBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    list(v);
                }
            });


            myListView = (ListView)findViewById(R.id.listView1);

            // create the arrayAdapter that contains the BTDevices, and set it to the ListView
            BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            for(int i=0 ; i<BTArrayAdapter.getCount();i++){
                Log.d(DEBUGING,BTArrayAdapter.getItem(i));
            }
            myListView.setAdapter(BTArrayAdapter);
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });


            Intent intent = getIntent();
            Log.d("COUCOU", "entering onResume");
            if(intent.getExtras()!=null) {
                Log.d("COUCOU", "Extras not null");
                if ((intent.getStringExtra("GetFilePath")!=null)) {
                    textFileName.setText("File to share :"+intent.getStringExtra("GetFilePath"));
                }
            }
        }
    }

    public void on(View view){
        Log.d(DEBUGING, "entering on");
        if (!myBluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

            Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
                    Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(DEBUGING, "entering onActivityResult");
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            if(myBluetoothAdapter.isEnabled()) {
                text.setText("Status: Enabled");
            } else {
                text.setText("Status: Disabled");
            }
        }
    }

    public void list(View view){
        Log.d(DEBUGING, "entering list");
        // get paired devices
        pairedDevices = myBluetoothAdapter.getBondedDevices();

        // put it's one to the adapter
        for(BluetoothDevice device : pairedDevices)
            BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());

        Toast.makeText(getApplicationContext(),"Show Paired Devices",
                Toast.LENGTH_SHORT).show();

    }

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(DEBUGING, "entering bReceiver.onReceive");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void off(View view){
        Log.d(DEBUGING, "entering off");
        myBluetoothAdapter.disable();
        text.setText("Status: Disconnected");

        Toast.makeText(getApplicationContext(),"Bluetooth turned off",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        Log.d(DEBUGING, "entering onDestroy");
        // TODO Auto-generated method stub
        super.onDestroy();
        try{
            unregisterReceiver(bReceiver);
        }catch(IllegalArgumentException e){
            Log.e(DEBUGING, " là ça peut planter si on a pas fait de recherche d'appareils, si Vincent voyait ça il me mettrait une claque");
        }

    }

}



