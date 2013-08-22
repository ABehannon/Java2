//Alex Behannon
//08-20-2013
//Java Week 3

package com.behannon.quoter;
import org.json.JSONException;
import org.json.JSONObject;

import com.behannon.libs.FileSaving;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.WebCheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {
	
	//initial variables
		Context _context;
		Boolean _connected = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_page);
		
		Cursor cursor = getContentResolver().query(QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);
		displayQuoteData(cursor);
		
		//setup button for xml of main activity
				Button backButton = (Button) findViewById(R.id.backButton);
				
				
				//on click listener for main activity
				backButton.setOnClickListener(new View.OnClickListener() {
				
					public void onClick(View v) {
						
						finish();
						
					}
				});

	}
	
	public void displayQuoteData(Cursor cursor){
		
		String read = FileSaving.readStringFile(this, "quoteData", false);
		
			//Init variables
			JSONObject json;

			try{
				
				//create json from the file loaded
				json = new JSONObject(read);
				String quote = json.get("quote").toString();
				String author = json.get("author").toString();
				
				//Set the text views to show data loaded
				((TextView)findViewById(R.id.quoteText)).setText(quote);
				((TextView)findViewById(R.id.authorText)).setText("Author: " + author);
				
			} catch (JSONException e){
				
				e.printStackTrace();
				
			}
	}
	
	//Called to test internet when button pressed or app started
	public void testConnection(){
		
		if (_connected){
			
			Log.i("Network connection: ", WebCheck.getConnectionType(_context));
			System.out.println("ONLINE");
			
		} else {

			Toast.makeText(getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();
        	System.out.println("OFFLINE");

			String file = FileSaving.readStringFile(_context, "quoteData", false);
			
			if (!file.isEmpty()){
				
				Cursor cursor = getContentResolver().query(QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);
				displayQuoteData(cursor);
				
			} else {
				
				System.out.println("EMPTY FILE ERROR");
				
			}
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
