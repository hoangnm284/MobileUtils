package com.hoang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.ui.UiApplication;

public class Data {
	/**
	 * url using for testing internet connection
	 */
	public static String TEST_URL = "http://www.google.com";
	
	/**
	 * Check if Internet connection is available
	 * if test server is not specify then google.com will be used 
	 * @param testServer 
	 * @return
	 */
	public static boolean InternetConnectionAvailable(String testServer){
		if (testServer == "") testServer = Data.TEST_URL;
		
		boolean result = false;
		try {
			HttpConnection conn = (HttpConnection) Connector.open(testServer);
			conn.setRequestMethod(HttpConnection.GET);
			if (conn.getResponseCode() == HttpConnection.HTTP_OK){
				result = true;
			} else {
				result = false;
			}
			conn.close();
		} catch(Exception e){
			e.printStackTrace();
			Utils.ShowAlert("error", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Connect to server to get content
	 * 
	 * @param url to be connect
	 * @return data return from server in String
	 * @throws IOException
	 */
	public static String DownloadFromServer(String url){
		String result = "";
		url += getConnectionString();
		try {
			HttpConnection conn = (HttpConnection) Connector.open(url);
			if (conn.getResponseCode() == HttpConnection.HTTP_OK){
				InputStream inputStream = conn.openInputStream();
				result = new String(IOUtilities.streamToBytes(inputStream), "UTF-8");			
			} else {
			}
			conn.close();
		} catch(Exception e){
			e.printStackTrace();
			Utils.ShowAlert("error", e.getMessage());
		}
		
		return result;
	}
	
	
	/**
	 * Send data to server async 
	 * @param url
	 */
	public static void SendToServer(String url){
		url += getConnectionString();
		Data data = new Data();
		UiApplication.getUiApplication().invokeLater(data.new SendDataAsync(url));
	}
	
	/**
	 * Class to handle send data async
	 * @author hoang
	 *
	 */
	public class SendDataAsync implements Runnable {
		/**
		 * URL for sending
		 */
		private String URL;
		
		/**
		 * Constructor with url for sending
		 * @param url
		 */
		public SendDataAsync(String url){
			this.URL = url;
		}
		
		/**
		 * Action to send url;
		 */
		public void run() {
			try {
				HttpConnection conn = (HttpConnection) Connector.open(URL);
				conn.setRequestMethod(HttpConnection.GET);
				if (conn.getResponseCode() == HttpConnection.HTTP_OK){
					Utils.Log("Sent to Server", URL);
				} else {
					
				}
				conn.close();
			} catch(Exception e){
				Utils.ShowAlert("error", e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Define network status then return most suitable connection string
	 * @return
	 */
	private static String getConnectionString(){
		String connectionString="";
		// check wifi available
		if(WLANInfo.getWLANState()==WLANInfo.WLAN_STATE_CONNECTED){
		    connectionString="?;interface=wifi";
		}
		// check Mobile Data System connection
		else if((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS){
		     connectionString = "?;&deviceside=false";
		}
		// check coverage network
		else if((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_DIRECT)==CoverageInfo.COVERAGE_DIRECT){
		    // get provider id
			String carrierUid = getCarrierBIBSUid();
		    if(carrierUid == null) {
		        connectionString = "?;deviceside=true";
		    }
		    else{
		        connectionString = "?;deviceside=false?;connectionUID="+carrierUid + "?;ConnectionType=mds-public";
		    }               
		}
		else if(CoverageInfo.getCoverageStatus() == CoverageInfo.COVERAGE_NONE) {          
		}
		
		return connectionString;
	}
	
	/**
	 * Get Carrier BIB Uid
	 * @return
	 */
	private static String getCarrierBIBSUid()
	{
	    ServiceRecord[] records = ServiceBook.getSB().getRecords();
	    int currentRecord;

	    for(currentRecord = 0; currentRecord < records.length; currentRecord++){
	    	if(records[currentRecord].getCid().toLowerCase().equals("ippp")){
	    		if(records[currentRecord].getName().toLowerCase().indexOf("bibs") >= 0)
	            {
	                return records[currentRecord].getUid();
	            }
	        }
	    }

	    return null;
	}
	
	/**
	 * Method to get data from web site then pass return data to call back function
	 * references: http://www.coderholic.com/blackberry-webbitmapfield/ 
	 * @param url
	 * @param callback
	 */
	public static void getWebData(final String url, final WebDataCallback callback){
		// start the process in thread to run in background
		// but BB only allow 17 threads, so how to solve with long list of request?
		Thread t = new Thread(new Runnable(){
			public void run() {
				// add connection string type to url
				String source = url + getConnectionString();
				
				HttpConnection conn = null;
				InputStream stream = null;
				byte[] result = null;
				
				try {
					// open http connection
					conn = (HttpConnection) Connector.open(source, Connector.READ, true);
					// check reponse code
					if (conn.getResponseCode() == HttpConnection.HTTP_OK){
						// get returned data
						stream = conn.openInputStream();
						// convert returned data to bytes
						result = IOUtilities.streamToBytes(stream);
					}
					// pass returned data to call back function
					callback.callback(result);
				}
				catch (Exception e){
					Utils.ShowAlert("get web data Error", e.getMessage());
				}
				finally {
					try {
						// release stream
						stream.close();
						stream = null;
						// release connection
						conn.close();
						conn = null;
					} catch (Exception e){
						Utils.ShowAlert("get web data Error", e.getMessage());
					}
				}
			}
			
		});
		t.start();
	}
	
	/**
	 * Interface for web data call back functions
	 * @author hoang
	 *
	 */
	public interface WebDataCallback {
		public void callback(byte[] data);
	}

}
