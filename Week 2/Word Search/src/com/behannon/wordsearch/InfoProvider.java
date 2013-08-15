//Alex Behannon
//Java II Week 2
//08-15-2013

package com.behannon.wordsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class InfoProvider extends ContentProvider {

	public static final String AUTHORITY = "com.behannon.wordsearch.infoprovider";
	
	//Class to hold content
	public static class DictionaryData implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.behannon.wordsearch.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.behannon.wordsearch.item";
		
		//Defining columns
		public static final String LANG_COLUMN = "language";
		public static final String WORD_COLUMN = "word";
		
		public static final String[] PROJECTION = { "_Id", LANG_COLUMN, WORD_COLUMN };
		
		private DictionaryData() {};
		
	}
	
	public static final int ITEMS = 1;
	public static final int ITEMS_ID = 2;
	
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(AUTHORITY, "items/#", ITEMS_ID);
	}
	
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		switch (uriMatcher.match(uri))
		{
		case ITEMS:
			return DictionaryData.CONTENT_TYPE;
			
		case ITEMS_ID:
			return DictionaryData.CONTENT_ITEM_TYPE;
		}
		
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selectionArgs, String[] sortOrder,
			String arg4) {
		
		MatrixCursor result = new MatrixCursor(DictionaryData.PROJECTION);
		
		String JSONString = StorageSystem.readFile(getContext(), "json");
		JSONObject job = null;
		JSONObject termObject = null;
		JSONObject englishObject = null;
		JSONObject frenchObject = null;
		
		JSONObject results = null;
		JSONArray termArray = null;
		
		try {
			
		job = new JSONObject(JSONString);
		termObject = job.getJSONObject("term0").getJSONObject("PrincipalTranslations").getJSONObject("0");
		englishObject = termObject.getJSONObject("OriginalTerm");
		frenchObject = termObject.getJSONObject("FirstTranslation");
		
		termArray.put(englishObject);
		termArray.put(frenchObject);
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//message sent if array is empty
		if(termObject == null)
		{
			return result;
		}
		
		switch (uriMatcher.match(uri))
		{
		
		case ITEMS:
			try {
			result.addRow(new Object[]{1, "English", englishObject.get("term")});
			result.addRow(new Object[]{2, "French", frenchObject.get("term")});
			} catch (JSONException e) {
				
			}
			break;
			
		case ITEMS_ID:
			String langRequest = uri.getLastPathSegment();

			for (int i = 0; i < termArray.length(); i++)
			{
				try
				{
					results = termArray.getJSONObject(i);

					if (langRequest == "English")
					{
						result.addRow(new Object[]{ i + 1, "English", results.get("term")});
					} if (langRequest == "French")
					{
						result.addRow(new Object[]{ i + 1, "French", results.get("term")});
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			break;
		}
		
		return result;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
}