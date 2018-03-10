package com.ianglei.jia.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpAgent
{
	public static final String TAG = "HttpAgent";
	

	public static InputStream streampost(String remote_addr)
	{
	    URL infoUrl = null;
	    InputStream inStream = null;
	    try {
	        infoUrl = new URL(remote_addr);
	        URLConnection connection = infoUrl.openConnection();
	        HttpURLConnection httpConnection = (HttpURLConnection)connection;
	        int responseCode = httpConnection.getResponseCode();
	        if(responseCode == HttpURLConnection.HTTP_OK){
	            inStream = httpConnection.getInputStream();
	        }
	    } catch (MalformedURLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return inStream;
	}
	
    public static String downloadResource(String uri)
    {
		if (null == uri)
		{
			Log.w(TAG,"uri is null");
			return null;
		}
		
		String filePath = JUtils.getResourcePath(uri);
		Log.d(TAG, filePath);
		File file = new File(filePath);
		
		try
		{
			URL url = new URL(uri);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			//urlConnection.setDoOutput(true);
			urlConnection.connect();
			
			BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));
			
			InputStream inputStream = urlConnection.getInputStream();
			
			int totalSize = urlConnection.getContentLength();

    		//variable to store total downloaded bytes
    		int downloadedSize = 0;

    		//create a buffer...
    		byte[] buffer = new byte[1024000];
    		int bufferLength = 0; //used to store a temporary size of the buffer

    		//now, read through the input buffer and write the contents to the file
    		while ( (bufferLength = inputStream.read(buffer)) != -1 ) {
    			//add the data in the buffer to the file in the file output stream (the file on the sd card
    			fileOutput.write(buffer, 0, bufferLength);
    			//add up the size so we know how much is downloaded
    			downloadedSize += bufferLength;
    			//Log.i(tag, "download "+downloadedSize+" of "+totalSize);
    			
    		}
    		Log.i(TAG, "download "+downloadedSize+" of "+totalSize);
    		//close the output stream when done
    		fileOutput.close();
    		urlConnection.disconnect();    		  		
			
		} catch (MalformedURLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;		
    }
	
//	public String posturl(String url)
//	{
//		InputStream is = null;
//		String result = null;
//
//		try
//		{
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(url);
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//		} catch (Exception e)
//		{
//			Log.d(TAG, "Fail to establish http connection!" + e.toString());
//		}
//
//		try
//		{
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					is, "utf-8"));
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null)
//			{
//				sb.append(line + "\n");
//			}
//			is.close();
//
//			result = sb.toString();
//		} catch (Exception e)
//		{
//			Log.d(TAG, "Fail to convert net stream!");
//		}
//
//		return result;
//	}
	
	
	public String httpGetContent(String urlstr) throws Exception
    {
    	String retStr = null;
 		
		URL url = new URL(urlstr);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
    	urlConnection.connect();
    		
 
        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
        BufferedReader buffer = new BufferedReader(in);
        String inputLine = null;
        while (((inputLine = buffer.readLine()) != null))  
        {  
        	retStr += inputLine + "\n";  
        }    		

    	
    	return retStr;
    }
	
	

//	public String posturl(ArrayList<NameValuePair> nameValuePairs, String url){
//	    String result = "";
//	    String tmp= "";
//	    InputStream is = null;
//	    try{
//	        HttpClient httpclient = new DefaultHttpClient();
//	        HttpPost httppost = new HttpPost(url);
//	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//	        HttpResponse response = httpclient.execute(httppost);
//	        HttpEntity entity = response.getEntity();
//	        is = entity.getContent();
//	    }catch(Exception e){
//	        return "Fail to establish http connection!";
//	    }
//
//	    try{
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
//	        StringBuilder sb = new StringBuilder();
//	        String line = null;
//	        while ((line = reader.readLine()) != null) {
//	            sb.append(line + "\n");
//	        }
//	        is.close();
//
//	        tmp=sb.toString();
//	    }catch(Exception e){
//	        return "Fail to convert net stream!";
//	    }
//
//	    try{
//	        JSONArray jArray = new JSONArray(tmp);
//	        for(int i=0;i<jArray.length();i++){
//	            JSONObject json_data = jArray.getJSONObject(i);
//	            Iterator<?> keys=json_data.keys();
//	            while(keys.hasNext()){
//	                result += json_data.getString(keys.next().toString());
//	            }
//	        }
//	    }catch(JSONException e){
//	        return "The URL you post is wrong!";
//	    }
//
//	    return result;
//	}
}
