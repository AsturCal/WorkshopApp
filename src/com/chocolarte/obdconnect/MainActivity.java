package com.chocolarte.obdconnect;

import java.util.ArrayList;
import java.util.Set;

import com.example.testproject.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private ObdDebug obdDebugInput;
    private Button on;
    private Button off;
    private Button visible;
    private Button list;
    private Button initOBD;
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		this.obdDebugInput = new ObdDebug (this);
		
        this.on = (Button)findViewById(R.id.button1);
        this.off = (Button)findViewById(R.id.button2);
        this.visible = (Button)findViewById(R.id.button3);
        this.list = (Button)findViewById(R.id.button4);
        this.initOBD = (Button)findViewById(R.id.button5);
        
        btAdapter = BluetoothAdapter.getDefaultAdapter();
				
		this.obdDebugInput.add("Welcome to the test project.");
    }
	
     public void on(View view){
        if (!btAdapter.isEnabled()) {
           Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(turnOn, 0);
           Toast.makeText(getApplicationContext(),"Turned on" 
           ,Toast.LENGTH_LONG).show();
        }
        else{
           Toast.makeText(getApplicationContext(),"Already on",
           Toast.LENGTH_LONG).show();
           }
     }
     
     public void list(View view){
        pairedDevices = btAdapter.getBondedDevices();

        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : pairedDevices)
           list.add(bt.getName()+ '\t' + bt.getAddress());

        Toast.makeText(getApplicationContext(),"Showing Paired Devices",
        Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter
        (this,android.R.layout.simple_list_item_1, list);
        this.obdDebugInput.add("Adapter found: " + adapter);

     }
     
     public void off(View view){
    	 btAdapter.disable();
        Toast.makeText(getApplicationContext(),"Turned off" ,
        Toast.LENGTH_LONG).show();
     }
     
     public void visible(View view){
        Intent getVisible = new Intent(BluetoothAdapter.
        ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
     }
    
     public void initOBD(View view){
	    
		if (btAdapter == null) {
		    // Device does not support Bluetooth
			this.obdDebugInput.add("Bluetooth not supported!");
		}
		else if (!btAdapter.isEnabled()) {
			this.obdDebugInput.add("Bluetooth not enabled, enabling");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
	    
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			this.obdDebugInput.add("Amount of paired devices found: " + pairedDevices.size());
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	this.obdDebugInput.add("Device name: " + device.getName());
		    	this.obdDebugInput.add("Device address: " + device.getAddress());
		    			    	
		    	ConnectThread tmp= new ConnectThread(device);
		    	tmp.run();
		    }
		}
     }    
    
     @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
    	 // Inflate the menu; this adds items to the action bar if it is present.
	     getMenuInflater().inflate(R.menu.main, menu);
	     return true;
	 }
    
}

//Just testing Github, forget you've ever seen this
//Still testing it... 
