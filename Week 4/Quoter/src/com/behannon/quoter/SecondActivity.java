//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.quoter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

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
