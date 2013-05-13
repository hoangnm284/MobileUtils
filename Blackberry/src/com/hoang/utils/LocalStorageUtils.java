package com.hoang.utils;

import net.rim.device.api.*;
import net.rim.device.api.system.*;

/**
 * Class to handle local storage functions
 * Local storage is Persistent data store in Blackberry
 * @author Nguyen Minh Hoang
 *
 */
public class LocalStorageUtils {
	
	/**
	 * Get persistent object with keyword
	 * @param key
	 * @return
	 */
	public static PersistentObject GetLocalStorageObject(String key){
		return PersistentStore.getPersistentObject(Utils.convertStringToLong(key));
	}
	
	/**
	 * Insert data into local storage
	 * the new data will replace the current one if have
	 * @param key
	 * @param data
	 * @return
	 */
	public static boolean Insert(String key, Object data){
		// get persistent object base on keyword
		PersistentObject localStorage = LocalStorageUtils.GetLocalStorageObject(key);
		// insert data into localStorage
		localStorage.setContents(data);
		// commit to save data
		localStorage.commit();
		
		return true;
	}
	
	
	/**
	 * @param key
	 * Get data from local storage
	 * @return
	 */
	public static Object Get(String key){
		// get persistent object base on keyword
		PersistentObject localStorage = LocalStorageUtils.GetLocalStorageObject(key);
		// get data
		Object result = localStorage.getContents();
		
		return result;
	}
}
