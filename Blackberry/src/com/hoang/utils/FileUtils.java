package com.hoang.utils;

import net.rim.device.api.system.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

/**
 * Utility class provides function to work with file system 
 * @author hoang
 *
 */
public class FileUtils {

	/**
	 * Source for storing file. By default it's media card 
	 */
	public static String DEFAULT_SOURCE = "file:///SDCard/";
	
	/**
	 * Check if folder is exist
	 * @param folderName
	 * @return
	 */
	public static boolean folderExist(String folderName){
		boolean result = false;
		try {
			FileConnection fc = (FileConnection) Connector.open(DEFAULT_SOURCE + folderName + "/");
			if (!fc.exists()){
				result = false;
			} else {
				result = true;
			}			
		} catch ( IOException e){
			
		}
		
		return result;
	}
	
	/**
	 * Create Folder
	 * @param folderName
	 */
	public static void createFolder(String folderName){
		try {
			FileConnection fc = (FileConnection) Connector.open(DEFAULT_SOURCE + folderName + "/");
			if (!fc.exists()){
				fc.mkdir();
			}			
		} catch ( IOException e){
			
		}		
	}
	
	/**
	 * Check if file is exists in folder
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public static boolean fileExist (String folderName, String fileName){
		boolean result = false;;
		
		return result;
	}

	/**
	 * Gets the content of the file.
	 * @param filename
	 * @return The file content.
	 */
	public static String GetFileContent(String filename){
		//String str = Settings.getString(filename, "");
		//return str;
		return "";
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
		//SharedPreferences.Editor editor = Settings.edit();
		//editor.putString(filename, data);		
		//return editor.commit();
		return true;
	}
	
	/**
	 * Appends the data to file.
	 * @param filename
	 * @param data
	 * @return <c>true</c>, if data to file was appended, <c>false</c> otherwise.</returns>
	 */
	public static boolean AppendDataToFile(String filename, String data){
		/*
		String currentData = Settings.getString(filename, "");
		String newData = currentData + System.getProperty("line.separator") + data;
		return Data.InsertDataToFile(filename, newData);
		*/
		return true;
	}
	
	
}
