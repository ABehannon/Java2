//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.quoter;
import java.net.MalformedURLException;
import java.net.URL;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SecondActivity extends Activity implements QuoteFragment.FormListener, OfflineFragment.FormListener {
	
	//initial variables
		Context _context;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quotefrag);
		
	}
	
	

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}

}
