//Alex Behannon
//Java II Week 1
//08-08-2013

package com.behannon.wordsearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

//Main class saves the JSON to a locally saved file.
public class StorageSystem {
	
	static String searchedWordEnglish = null;
	static String searchedWordFrench = null;
	
	//save file
	@SuppressWarnings("unused")
	public static Boolean saveFile(Context context, String filename, String content) {
		
		//Attempt to save the file
		try {
			File file;
			FileOutputStream fos;
			
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			
			fos.write(content.getBytes());
			fos.close();
			
		} catch (IOException e) {
			
			//Log error
			Log.e("Error Writing: ", filename);
			
		}
		
		return true;
		
	}
	
	//restore file
	@SuppressWarnings("unused")
	public static String readFile(Context context, String filename) {
		String content = "";
		
		try {
			//file load setup
			File file;
			FileInputStream fin;
			file = new File(filename);
			fin = context.openFileInput(filename);
			
			//BIS setup
			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			
			StringBuilder contentBuffer = new StringBuilder();
			
			while ((bytesRead = bin.read(contentBytes)) != -1) {
				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
			}
			
			content = contentBuffer.toString();
			fin.close();
			
		} catch (FileNotFoundException e) {
			
			//Log error
			Log.e("Error: ", "File could not be found - " + filename);
			
		} catch (IOException e) {
			
			//Log error
			Log.e("Error: ", "IO Exception");
			
		}
		
		return content;
		
	}
	
	/*
	//Split saved file to make it usable
	public static void displayResponse() {
		
		try {
		//Loads locally saved file as string
		String fileResponse = readFile(MainActivity._context,"json");
		
		JSONObject testObject = new JSONObject(fileResponse);
		JSONObject termObject = testObject.getJSONObject("term0").getJSONObject("PrincipalTranslations").getJSONObject("0").getJSONObject("OriginalTerm");
		JSONObject termObject2 = testObject.getJSONObject("term0").getJSONObject("PrincipalTranslations").getJSONObject("0").getJSONObject("FirstTranslation");
		Object testString = termObject.get("term");
		System.out.println("TEST: " + termObject);
		System.out.println("TEST: " + termObject2);
		System.out.println("TEST: " + testString);
		
		
		
		//Splits saved file into separate parts to display
		String responseInfo[] = fileResponse.split(",");
		    
		//Splits responseInfoMain to grab the term used
		searchedWordEnglish = responseInfo[0].split(":")[5].replace("\"", "");
		
		//Splits responeInfoMain to grab definition of word
		searchedWordFrench = responseInfo[2].split(":")[1].replace("\"", "");
		
		} catch (Exception e) {
			System.out.println("ERROR DISPLAYING");
			
		}
		
		//Sends the responses to main activity
		if(searchedWordEnglish != null) {
			MainActivity.searchWordEnglish = searchedWordEnglish;
		} else {
			MainActivity.searchWordEnglish = null;
		}
		
		if(searchedWordFrench != null){
			MainActivity.searchWordFrench = searchedWordFrench;
		} else {
			MainActivity.searchWordFrench = null;
		}
		
		
	}
	*/
}