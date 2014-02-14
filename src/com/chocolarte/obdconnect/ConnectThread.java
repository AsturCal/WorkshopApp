package com.chocolarte.obdconnect;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class ConnectThread extends Thread {
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	//private static final UUID MY_UUID = UUID.fromString("00021101-0000-1000-8000-00805f9b34fb");
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private String mResponse;
	private ObdDebug obdDebugInput;
 
    public ConnectThread(BluetoothDevice device) {
    	
    	this.obdDebugInput = new ObdDebug ();
    	
    	//this.obdDebugInput.add("Constructing ConnectThread");
    	//Get the deault Bluetooth adapter
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        this.mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { 
        	this.obdDebugInput.add("Exception catched: " + e.toString());
        }
        this.mmSocket = tmp;
    }
 
    public void run() {
    	
    	//this.obdDebugInput.add("Running  ConnectThread");
    			
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
       		mmSocket.connect();
            
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
        	this.obdDebugInput.add("Exception catched: " + connectException.toString());       	
            try {
                mmSocket.close();
            } catch (IOException closeException) {
            	this.obdDebugInput.add("Exception catched: " + closeException.toString());
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket);
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        	this.obdDebugInput.add("Exception catched: " + e.toString());
        }
    }
    
    public void testConnection(TextView text) {
    	text.setText(mResponse );
    
    }
    
    private void manageConnectedSocket (BluetoothSocket socket){
    	ManageThread tmp = new ManageThread(socket);
    	Handler h = new Handler();
    	tmp.write(new byte[] { (byte)0x41, (byte)0x54, (byte)0x20, (byte)0x5A, (byte)0x0D });
    	tmp.run();
    	tmp.write(new byte[] { (byte)0x0D });
    	tmp.run();
       	tmp.write(new byte[] {(byte)0x41, (byte)0x54, (byte)0x20, (byte)0x53, (byte)0x50, (byte)0x20, (byte)0x30, (byte)0x0D });
       	tmp.run();
       	tmp.write(new byte[] { (byte)0x0D });
    	tmp.run();
       	tmp.write(new byte[] {(byte)0x30, (byte)0x31, (byte)0x20, (byte)0x30, (byte)0x43, (byte)0x0D });
      	tmp.run();
      	tmp.write(new byte[] { (byte)0x0D });
    	tmp.run();
    }
}
