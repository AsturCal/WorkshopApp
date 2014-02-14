package com.chocolarte.obdconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;

public class ManageThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
	private ObdDebug obdDebugInput;
 
    public ManageThread(BluetoothSocket socket) {
    	this.obdDebugInput = new ObdDebug ();
    	//this.obdDebugInput.add("Constructing ManageThread");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        	this.obdDebugInput.add("Exception catched: " + e.toString());
        }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
 
    public void run() {
    	//this.obdDebugInput.add("Running ManageThread");
        byte[] buffer = new byte[32];  // buffer store for the stream
        int bytes = 0;
        // Keep listening to the InputStream until an exception occurs
       // while (bytes > -1){
            try {
                // Read from the InputStream
            	bytes = mmInStream.read(buffer);
            	this.obdDebugInput.add("Read: " + bytes + "bytes");
            } catch (IOException e) {
            	this.obdDebugInput.add("Exception catched: " + e.toString());
               // break;
            }           
       // }
        this.obdDebugInput.add("Response: "+ new String (buffer));        
    }
 
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            SystemClock.sleep(10000);
        } catch (IOException e) {
        	this.obdDebugInput.add("Exception catched: " + e.toString());
        }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        	this.obdDebugInput.add("Exception catched: " + e.toString());
        }
    }
}
