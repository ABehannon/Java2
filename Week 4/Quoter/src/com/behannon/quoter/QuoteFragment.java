//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.quoter;

import org.json.JSONException;
import org.json.JSONObject;

import com.behannon.libs.FileSaving;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.WebCheck;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuoteFragment extends Fragment {
	
	Cursor cursor;
	RelativeLayout view;
	private FormListener listener;
	Boolean _connected = false;
	
	public interface FormListener{
		public void onBack();
		//public void onSave();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		view = (RelativeLayout) inflater.inflate(R.layout.detail_page, container, false);
		
		//Button for going back
		Button menuButton = (Button)view.findViewById(R.id.backButton);
		menuButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        listener.onBack();
		    }
		});
		
		//Button for saving info
		Button saveButton = (Button)view.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	System.out.println("CLICKED!");
	    		TextView authorText = (TextView)view.findViewById(R.id.authorText);
	    		TextView quoteText = (TextView)view.findViewById(R.id.quoteText);
	    		String authorData = authorText.getText().toString().replace("Author:", "");
	    		String quoteData = quoteText.getText().toString();
	    		String MixedData = authorData + ":" + quoteData;
	    		
	    		System.out.println("Save Data: " + MixedData);
	    		FileSaving.storeStringFile(getActivity(), "favoriteData", MixedData, false);
	        }
	    });
		
		cursor = getActivity().getContentResolver().query(QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);
		
		displayQuoteData(cursor);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (FormListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement FormListener");
		}
	}


	
	public void displayQuoteData(Cursor cursor){
		
		String read = FileSaving.readStringFile(getActivity(), "quoteData", false);
		
			//Init variables
			JSONObject json;

			try{
				
				//create json from the file loaded
				json = new JSONObject(read);
				String quote = json.get("quote").toString();
				String author = json.get("author").toString();
				
				//Set the text views to show data loaded
				((TextView)view.findViewById(R.id.quoteText)).setText(quote);
				((TextView)view.findViewById(R.id.authorText)).setText("Author: " + author);
				
			} catch (JSONException e){
				
				testConnection();
				e.printStackTrace();
				
			}
	}
	
	//Called to test internet when button pressed or app started
	public void testConnection(){
		
		if (_connected){
			
			Log.i("Network connection: ", WebCheck.getConnectionType(getActivity()));
			System.out.println("ONLINE");
			
		} else {

			Toast.makeText(getActivity().getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();
        	System.out.println("OFFLINE");

			String file = FileSaving.readStringFile(getActivity(), "quoteData", false);
			
			if (!file.isEmpty()){
				
				Cursor cursor = getActivity().getContentResolver().query(QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);
				displayQuoteData(cursor);
				
			} else {
				
				System.out.println("EMPTY FILE ERROR");
				
			}
		}
	}

}
