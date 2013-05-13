package com.hoang.utils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.progressindicator.*;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class LoadingScreen extends PopupScreen {

	private LabelField _labelField = null;
	private ActivityIndicatorView _view = null;
	
	/**
	 * Constructor
	 * @param text
	 */
	public LoadingScreen(String text){
        super(new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR));
        _view = new ActivityIndicatorView(Field.FIELD_TOP);
        this.add(_view);
        _labelField = new LabelField(text, Field.FIELD_HCENTER);
        this.add(_labelField);        
	}

	/**
	 * Display pop up while run action in background
	 * @param runThis
	 * @param text
	 */
    public static void showScreenAndWait(final Runnable runThis, String text) {
        final LoadingScreen thisScreen = new LoadingScreen(text);
        Thread threadToRun = new Thread() {
            public void run() {
                // First, display this screen
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(thisScreen);
                    }
                });
                // Now run the code that must be executed in the Background
                try {
                    runThis.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                    throw new RuntimeException("Exception detected while waiting: " + t.toString());
                }
                // Now dismiss this screen
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().popScreen(thisScreen);
                    }
                });
            }
        };
        threadToRun.start();
    }

}
