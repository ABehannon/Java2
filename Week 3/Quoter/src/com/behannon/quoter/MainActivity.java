//Alex Behannon
//08-20-2013
//Java Week 3

package com.behannon.quoter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.WebCheck;

public class MainActivity extends Activity {

	//initial variables
	Context _context;
	Boolean _connected = false;
	String spinnerChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Sets context to the main activity
		_context = this;
		_connected = WebCheck.getConnectionStatus(_context);
		
		//Tests connection upon being loaded.
		testConnection();
		
		//setup button for xml of main activity
		Button searchButton = (Button) findViewById(R.id.backButton);
		
		
		//on click listener for main activity
		searchButton.setOnClickListener(new View.OnClickListener() {
		
			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View v) {
				
				_connected = WebCheck.getConnectionStatus(_context);

				//Tests connection upon being clicked
				testConnection();
				
				//Spinner options. ATM there is only one option, but previous API had multiple categories to choose from.
				Spinner spinner = (Spinner) findViewById(R.id.optionsSpinner);
				spinnerChoice = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
				System.out.println("SPINNER CHOICE: " + spinnerChoice);
				
				//Check to make sure that the category is valid
                if (spinnerChoice.contains("category")) {
                	
                	//Toast for if there is not a correct category chosen.
                    Toast toast = Toast.makeText(_context,"Please select a category.", Toast.LENGTH_SHORT);
                    toast.show();
                    
                } else {
                	
                	//Handler event for content provider
                	Handler serviceHandler = new Handler(){
                		
                		public void handleMessage(Message message) {
                			
                			if(message.arg1 == RESULT_OK && message.obj != null) {
                				
                				String workingURL = message.obj.toString();
                				Log.i("workingURL: ", workingURL);
                				
                				try {
                					
                					//Get JSON from API
                					JSONObject json = new JSONObject(workingURL);
                					System.out.println(json);
                					
                					String quote = json.get("quote").toString();
                					String author = json.get("author").toString();
                					
                					System.out.println("Quote: " + quote);
                					System.out.println("Author: " + author);

                					Cursor cursor = getContentResolver().query(QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);
                				
                					//displayQuoteData(cursor);
                					
                					//Show second activity.
                					Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                			        startActivity(intent);
                					
                				} catch (JSONException e){
                					
                					Log.e("JSON Error in OnClick:", e.toString());
                					
                				}
                			}
                		}
                	};
                	
                	//Create messenger
                	Messenger serviceMessenger = new Messenger(serviceHandler);
                	
                	Intent intent1 = new Intent(_context, DataService.class);
                	intent1.putExtra("messenger", serviceMessenger);
                	intent1.putExtra("category", spinnerChoice);
                	
                	//Start intent for data
                	startService(intent1);                	
                }
			}
		});
	}

	//Called to test internet when button pressed or app started
	public void testConnection(){
		
		if (_connected){
			
			Log.i("Network connection: ", WebCheck.getConnectionType(_context));
			System.out.println("ONLINE");
			
		} else {

			Toast.makeText(getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();
        	System.out.println("OFFLINE");
        	
		}
	}
	
	//Settings menu, unused.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}