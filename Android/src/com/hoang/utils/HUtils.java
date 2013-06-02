package com.hoang.utils;

import java.io.*;

import android.util.Log;

/**
 * General Utility class
 * @author Hoang
 *
 */
public class HUtils {

	/**
	 * Reads an InputStream and converts it to a String.
	 * @param stream
	 * @return
	 * @throws IOException 
	 */
	public static String readIt(InputStream stream) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(stream));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
		    total.append(line);
		}
		
	    return total.toString();
	}
	
	/**
	 * Write to Logcat
	 * uncomment this function when publish app
	 * @param title
	 * @param message
	 */
	public static void log(String title, String message){
		Log.w(title, message);
	}
}
