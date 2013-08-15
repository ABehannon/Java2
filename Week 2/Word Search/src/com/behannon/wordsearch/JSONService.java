//Alex Behannon
//Java II Week 2
//08-15-2013

package com.behannon.wordsearch;

import java.net.MalformedURLException;
import java.net.URL;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class JSONService extends IntentService {

	//initial setup for variables an such
	Context _context;
	Boolean _connected;
	Boolean internetConnection = true;
	String _response;
	
	String finalURL = null;
	String keyword = null;
	
	public JSONService() {
		super("JSONService");
		
	}

	@Override
	@SuppressWarnings("unused")
	protected void onHandleIntent(Intent intent) {
		
			Log.i("onHandleIntent", "Service has started");
			
			Bundle extras = intent.getExtras();
			if(extras != null) {
				keyword = (String) extras.getString("keyword");
				System.out.println("SERVICE - Keyword: " + keyword);
			}
			
			//Create init variables and fix URL info
			String URL = "http://api.wordreference.com/0.8/e105d/json/enfr/";
			String baseURL = keyword.replaceAll("\\s", "+");
			String moddedURL = URL + baseURL;
			String encodeURL;
			
			URL finalURL;
			try{
				
				finalURL = new URL(moddedURL);
				_response = Web.getURLStringResponse(finalURL);
				System.out.println("Modded URL: "+ moddedURL);
				
				StorageSystem.saveFile(this, "json", _response);
				
			}catch (MalformedURLException e){
				
				Log.e("Bad URL", "Malformed URL");
				finalURL = null;
				
			}
			
			Log.i("onHandleEvent", "Service has finished");
			
			//Create message
			Messenger messenger = (Messenger) extras.get("messenger");
			Message message = Message.obtain();
			
			if (message != null) {
				
				message.arg1 = MainActivity.RESULT_OK;
				message.obj = _response;
				
			}
			
			//Send message
			try {
				
				messenger.send(message);
				
			} catch (RemoteException e) {
				
				Log.e("onHandleIntent", e.getMessage());
			}
			
	}

}