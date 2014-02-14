//A simple tool to print traces on the screen of the phone. It requires the provided Activity to have a TextView named "debug_message"

package com.chocolarte.obdconnect;

import java.util.Calendar;
import android.app.Activity;
import android.widget.TextView;

public class ObdDebug {
	
	private static TextView obdText;
	private static String	debugString;
	
	public ObdDebug(){
	}
	
	public ObdDebug(Activity context){
	//	obdText = (TextView) context.findViewById(R.id.debug_message);		
	}
	
	public void add(String newDebugText){
		Calendar c = Calendar.getInstance(); 	
		debugString = debugString + "\n"
				+ c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE) + ":"
				+ c.get(Calendar.SECOND) + "."
				+ c.get(Calendar.MILLISECOND)
				+ " - " + newDebugText;
		
		if (obdText != null) {
			try{
				obdText.setText(debugString);
			} catch (Exception e){}		
		}
	}
}
