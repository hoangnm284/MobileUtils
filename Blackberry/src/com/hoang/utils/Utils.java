package com.hoang.utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

/**
 * Utility class which provide helpful functions
 * @author Nguyen Minh Hoang
 *
 */
public class Utils {
	/**
	 * Use 1 loadingscreen to avoid conflict
	 */
	private static LoadingScreen loadingScreen;
	
	/**
	 * Constructor
	 */
	public Utils(){		
	}
	
	/**
	 * Show loading popup
	 * @param message
	 */
	public static void showLoadingPopup(String message){
		loadingScreen = new LoadingScreen(message);
		UiApplication.getUiApplication().pushScreen(loadingScreen);
	}
	
	/**
	 * Hide loading popup
	 */
	public static void dismissLoadingPupup(){
		UiApplication.getUiApplication().popScreen(loadingScreen);
	}
	
	/**
	 * display an pop up screen while run actions in background
	 * the screen will be disappeared when action finish
	 * @param runThis
	 * @param message
	 */
	public static void runWithPopUp(final String message, final Runnable runThis){
		LoadingScreen.showScreenAndWait(runThis, message);
	}
	
	/**
	 * Convert a string to long value
	 * Note that this method is different from Long.Parse(string)
	 * this method will convert any String to long value using hashcode
	 * @param string
	 * @return long value
	 */
	public static long convertStringToLong(String str){
		int asInt = str.hashCode();
		return (long) asInt;
	}
	
	/**
	 * Function to show log in console for debugging
	 * Commenting the action inside to disable when deploying application
	 * @param tag
	 * @param message
	 */
	public static void Log(String tag, String message){
		System.out.println(tag + " --------- " + message);
	}
	
	/**
	 * Resize image
	 * @param originalImage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap ResizeImage(Bitmap originalImage, int newWidth, int newHeight) {
	    Bitmap newImage = new Bitmap(newWidth, newHeight);
	    originalImage.scaleInto(newImage, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FILL);
	    return newImage;
	}

	/**
	 * Show alert message
	 * @param title
	 * @param message
	 */
	public static void ShowAlert(String title, String message){
		Utils obj = new Utils();
		UiApplication.getUiApplication().invokeLater(obj.new AlertDialog(title, message));
	}
	
	/**
	 * Alert dialog class 
	 * @author Nguyen Minh Hoang
	 *
	 */
	protected class AlertDialog implements Runnable{
		String title;
		String message;
		
		public AlertDialog(String title, String message){
			this.title = title;
			this.message = message;
		}
		
		public void run(){
			Dialog.alert(this.message);
		}
	}
}