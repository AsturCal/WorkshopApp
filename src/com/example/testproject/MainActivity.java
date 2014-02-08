package com.example.testproject;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        Toast.makeText(this,"Hello!!", Toast.LENGTH_LONG).show();
    }
    //Respond to button
    public void bluetClicked(View view){
    	//TODO add button here. 
    	//Get Bluetooth Adapter. 
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null){
    		//Device does not support bluetooth... 
    		//Toast and exit
    		Toast.makeText(this, "Bluetooth not Supported", Toast.LENGTH_LONG).show();
    		finish();
    	}
    	//if here then continue.
    	if (!mBluetoothAdapter.isEnabled()) {
    		Toast.makeText(this,"Bluetooth not enabled...enabling",Toast.LENGTH_LONG).show();
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	}
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	textView.setText(device.getName() + "\n" + device.getAddress());
		    	
		    	ConnectThread tmp= new ConnectThread(device);
		    	tmp.run();
		    	tmp.testConnection(textView);
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
