package com.hoang.utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.component.BitmapField;

/**
 * 
 * @author hoang
 *
 */
public class YmBitmapField extends BitmapField{
	Bitmap _normalImage;
	Bitmap _focusImage;
	
	public YmBitmapField (Bitmap normalBitmap, Bitmap focusBitmap, long style){
		super(normalBitmap, style);
		this._normalImage = normalBitmap;
		this._focusImage = focusBitmap;
	}
	
	public void onFocus(int direction){
		this.setBitmap(_focusImage);
	}
	
	public void onUnfocus(){
		super.onUnfocus();
		this.setBitmap(_normalImage);
	}
}
