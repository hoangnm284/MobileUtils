package com.hoang.utils;

import com.hoang.utils.Data.WebDataCallback;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

/**
 * Class for bitmap image loaded from url
 * references: http://www.coderholic.com/blackberry-webbitmapfield/ 
 * @author Nguyen Minh Hoang
 *
 */
public class WebBitmapField extends BitmapField implements WebDataCallback {
	private EncodedImage _bitmap = null;

	/**
	 * Construtor
	 * @param url
	 */
	public WebBitmapField(String url){
		try {
			// get data from server
			// return data will be pass to callback function in this object
			Data.getWebData(url, this);
		} catch (Exception e){
			
		}
	}
	
	/**
	 * return bitmap image
	 */
	public Bitmap getBitmap(){
		if (_bitmap == null) return null;
		return _bitmap.getBitmap();
	}
	
	/**
	 * function to handle call back when load data finish
	 */
	public void callback(byte[] data) {
		// if data is empty then stop processing
		if (data == null || data.length == 0) return;
		
		// if data is not empty
		try {
			// convert data to bitmap image
			_bitmap = EncodedImage.createEncodedImage(data, 0, data.length);
		} catch (Exception e){
			Utils.ShowAlert("WebBitmap Error", e.getMessage());
		}
	}

}
