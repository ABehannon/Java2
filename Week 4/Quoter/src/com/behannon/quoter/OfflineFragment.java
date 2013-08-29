//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.quoter;

import com.behannon.libs.FileSaving;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OfflineFragment extends Fragment {
	
	//Initial variables
	RelativeLayout view;
	private FormListener listener;
	
	//Form Listener setup for Offline Frag
	public interface FormListener{
		public void onBack();
	}
	
	//Setup for fragment view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		view = (RelativeLayout) inflater.inflate(R.layout.favorite_page, container, false);
		
		//Button for going back
		Button menuButton = (Button)view.findViewById(R.id.backButton2);
		menuButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        listener.onBack();
		    }
		});
		
		displayQuoteData2();
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
	
	//Attempts to display the favorite quote
	public void displayQuoteData2(){
		
		String read = FileSaving.readStringFile(getActivity(), "favoriteData", false);

			try{
				
				//splits the stirng from the file loaded
				String splitter[] = read.split(":");
				String quote = splitter[1];
				String author = splitter[0];
				
				//Set the text views to show data loaded
				((TextView)view.findViewById(R.id.quoteText2)).setText(quote);
				((TextView)view.findViewById(R.id.authorText2)).setText("Author:" + author);
				
			} catch (Exception e){
				e.printStackTrace();
				((TextView)view.findViewById(R.id.quoteText2)).setText("No favorite has been saved.");
				((TextView)view.findViewById(R.id.authorText2)).setText("");
			}
	}

}
