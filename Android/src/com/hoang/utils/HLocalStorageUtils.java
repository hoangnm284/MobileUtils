package com.hoang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Utility class provides functions for working with LocalStorage
 * @author Nguyen Minh Hoang
 *
 */
public class HLocalStorageUtils{
		
	// shared preferences for whole application
	private static Context context = new Activity().getApplicationContext();
	private static SharedPreferences Settings = context.getSharedPreferences("HLocalStorage", 0);
	
	/**
	 * Check if the key is exist.
	 * @param filename
	 * @return true if exist, falseif not.
	 */
	public static boolean keyExist(String key){
		return Settings.contains(key);		
	}
	
	/**
	 * Gets the content for the key.
	 * @param key
	 * @return The content.
	 */
	public static String GetKeyContent(String key){
		String str = Settings.getString(key, "");
				
		return str;
	}
	
	/**
	 * Clear all data in key.
	 * @param key
	 * @return true if file was cleaned, false otherwise
	 */
	public static boolean CleanKey(String key){
		SharedPreferences.Editor editor = Settings.edit();
		editor.remove(key);
		return editor.commit();
	}
	
	/**
	 * Clear old data and inserts new data into key.
	 * @param key
	 * @param data
	 * @return <c>true</c>, if data was inserted to key, <c>false</c> otherwise.</returns>
	 */
	public static boolean InsertDataToKey(String key, String data){
		SharedPreferences.Editor editor = Settings.edit();
		editor.putString(key, data);
		
		return editor.commit();
	}
	
	/**
	 * Appends the data to key.
	 * @param key
	 * @param data
	 * @return <c>true</c>, if data was appended to key, <c>false</c> otherwise.</returns>
	 */
	public static boolean AppendDataToKey(String key, String data){
		String currentData = Settings.getString(key, "");
		String newData = currentData + System.getProperty("line.separator") + data;
		return InsertDataToKey(key, newData);
	}
	
}
