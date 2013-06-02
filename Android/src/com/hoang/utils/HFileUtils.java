package com.hoang.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.*;

/**
 * Simple class provides functions to work with file system
 * @author Nguyen Minh Hoang
 *
 */
public class HFileUtils extends Activity{
	/**
	 * Separator to separte data in file
	 */
	public static String SEPARATOR = System.getProperty("line.separator");
	
	/**
	 * Path to storage folder. Edit this params to change working files default location.
	 * By default, the folder is set to external storage
	 */
	public static String DIRECTORY_PATH 	= Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/**
	 * set to true if user external storage - false to user internal storage
	 * default is true
	 */
	public static boolean EXTERNAL_USE		= false;
	
	/**
	 * generate file path
	 * @param filename
	 * @return file path
	 */
	public static String generateFilePath(String filename){
		// if using external storage
		if (EXTERNAL_USE)
			return DIRECTORY_PATH + "/" + filename;
		// if usesing internal storage
		else
			return filename;
	}
	
	public static File getFile(String filename){
		// get file path
		String filePath = generateFilePath(filename);
		// get file
		return new File(filePath);
	}
	
	/**
	 * Check if the Files is exist.
	 * @param filename
	 * @return true if exist, false if not.
	 */
	public static boolean fileExist(String filename){
		// get gile
		File file = getFile(filename);
		// check file exits
		return file.exists();
	}
	
	/**
	 * Gets the content of the file.
	 * @param filename
	 * @return The file content.
	 */
	public static String getFileContent(String filename){
		String result = "";
		// check if file exits
		if (fileExist(filename)){
			// get file path
			String filePath = generateFilePath(filename);
			// read file content
			result = readFileToString(filePath);
		}
		
		return result;
	}
	
	/**
	 * Read file from given file path and return data in string
	 * @param filePath
	 * @return
	 */
	public static String readFileToString(String filePath){
		String result = "";
		try {
			FileInputStream fileInputStream = new Activity().openFileInput(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String data = reader.readLine();
			while (data != null) {
				result += data;
				data = reader.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "";
		}
		
		return result;
	}
	
	/**
	 * Clear all data in file.
	 * @param filename
	 * @return true if file was cleaned, false otherwise
	 */
	public static boolean CleanFile(String filename){
		return InsertDataToFile(filename, "");
	}
	
	/**
	 * Clear old data and inserts new data into file.
	 * @param filename
	 * @param data
	 * @return <c>true</c>, if data to file was inserted, <c>false</c> otherwise.</returns>
	 */
	public static boolean InsertDataToFile(String filename, String data){
		// get file paht
		String filePath = generateFilePath(filename);
		try {
			// open file to write
			FileOutputStream fos = new Activity().openFileOutput(filePath, Context.MODE_PRIVATE);
			// write data to file
			fos.write(data.getBytes());
			// close file
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Appends the data to file.
	 * @param filename
	 * @param data
	 * @return <c>true</c>, if data to file was appended, <c>false</c> otherwise.</returns>
	 */
	public static boolean AppendDataToFile(String filename, String data){
		// get current data
		String currentData = getFileContent(filename);
		// add new data to current data, separate by separator
		String newData = currentData + SEPARATOR + data;
		// insert new data to the file
		return InsertDataToFile(filename, newData);
	}
}
