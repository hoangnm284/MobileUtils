package com.hoang.utils;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * Class provides methods to work with web request
 * @author Nguyen Minh Hoang
 *
 */
public class HDataUtils {
	
	/**
	 * Constructor
	 */
	public HDataUtils ()
	{		
	}
	
	/**
	 * check if network is available
	 * @return
	 * @param context
	 */
	public static boolean networkAvailable(Context context){
		// get network manager
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// get network info
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		// check network status
		if (networkInfo != null && networkInfo.isConnected()) {
	        // fetch data
	    	return true;
	    } else {
	        // display error
	    	return false;
	    }
	}
	
	/**
	 * Function to send data to server in a separate thread.
	 * Only use this function if you don't care about the response data
	 * @param Url
	 */
	public static void sendDataAsync(String Url){
		HDataUtils dataUtils = new HDataUtils();
		SendDataAsync asyncTask = dataUtils.new SendDataAsync();
		asyncTask.execute(Url);
	}
	
	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves
	 * the web page content as a InputStream, which it returns as
	 * a string.
	 * This function should be surrounded by an async task or run in other thread than main thread
	 * @param myurl
	 * @return
	 * @throws IOException
	 */
	private static String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    String result = "";
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){	        
	        	is = conn.getInputStream();
	        	// Convert the InputStream into a string
	        	result = HUtils.readIt(is);
	        }
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } catch (Exception e){
	    	HUtils.log("Download URL error", e.getMessage());
	    }
	    finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	    
	    return result;
	}
	
	/**
	 * Class to handle sending data to server in separate thread.
	 * Only using this class if you don't care about response data,
	 * otherwise you should write your own async class
	 * @author Hoang
	 *
	 */
	protected class SendDataAsync extends AsyncTask <String, String, String>{

		@Override
		protected String doInBackground(String... args) {
			String myurl = args[0];
			try {
				HDataUtils.downloadUrl(myurl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				HUtils.log("Download Async error", e.getMessage());
			}
			return null;
		}
		
	}
}
