//Alex Behannon
//Java II Week 1
//08-08-2013

package com.behannon.wordsearch;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//initial setup for variables an such
	static Context _context;
	Boolean _connected;
	Boolean internetConnection = true;
	static String keyword;
	static String searchWordEnglish;
	static String searchWordFrench;
	static ArrayList<String> langList;
	static ArrayList<String> defList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		xmlLayoutSetup();
	}
	

	//Layout test
	public void xmlLayoutSetup(){
		
		_context = this;
		
		//Tests connection upon being loaded.
		testConnection();
		
		//Set up buttons for app
		Button searchButton = (Button) findViewById(R.id.searchButton);
		
		//On click handler for search
    	searchButton.setOnClickListener(new OnClickListener() {
    		EditText searchBox = (EditText) findViewById(R.id.searchBox);
    		TextView columnText1 = (TextView) findViewById(R.id.columnText1);
    		TextView columnText2 = (TextView) findViewById(R.id.columnText2);
    		Spinner spinner = (Spinner) findViewById(R.id.optionSpinner);
    		
    		@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View v) {

				columnText1.setText("");
				
				Log.i("Search On Click Handler", searchBox.getText().toString());
				
				//Check if empty text box
				if(searchBox.getText().toString() == "") {
					
					Toast.makeText(_context, "Please enter a word", Toast.LENGTH_SHORT).show();
					return;
				
				} else {
					
					//Get spinner choice
					final String spinnerChoice = spinner.getSelectedItem().toString();
					System.out.println("SPINNER: " + spinnerChoice);
					
					columnText1.setText("Searching...");
					columnText2.setText("Searching...");
					
					//Handler create
					Handler jsonHandler = new Handler() {
						@Override
						public void handleMessage(Message message) {
							if(message.arg1 == RESULT_OK && message.obj != null) {
								String finalURL = message.obj.toString();
								Log.i("RESPONSE: ", finalURL);
								
								try {
									
									//Get JSON from API
									JSONObject json = new JSONObject(finalURL);
									JSONObject data = json.getJSONObject("term0");
									Boolean error = data.has("error");
									if(error) {
										Toast.makeText(_context,"Error",Toast.LENGTH_SHORT).show();	
									} else {
										//Check and get JSON data
										JSONObject results = json.getJSONObject("term0").getJSONObject("PrincipalTranslations").getJSONObject("0").getJSONObject("OriginalTerm");
										System.out.println("RESULTS: " + results);
										
										Cursor cursor = getContentResolver().query(InfoProvider.DictionaryData.CONTENT_URI, null, null, null, null);
									
									}
								} catch (JSONException e) {
									columnText1.setText("Error");
									Log.e("JSON", e.toString());
								}
							}
						}
					};
					
					//Create messenger
					Messenger actMessenger = new Messenger(jsonHandler);
					
					Intent intent = new Intent(_context,JSONService.class);
					intent.putExtra("messenger", actMessenger);
					intent.putExtra("keyword", searchBox.getText().toString());
					
					
					//Start intent
					startService(intent);
					
					
					
					
				}
			}
		});
	
	}
	
	//Called to test internet when button pressed or app started
	private void testConnection() {
		
		Button searchButton = (Button) findViewById(R.id.searchButton);
		
		_connected = Web.getConnectionStatus(_context);
        if (_connected) {
            internetConnection = true;
            System.out.println("ONLINE");

        } else {
        	Toast.makeText(getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();
        	internetConnection = false;
        	System.out.println("OFFLINE");
        	searchButton.setEnabled(false);
        }
	}
		
}
