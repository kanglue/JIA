package com.ianglei.jia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JUtils
{
	public static String sdRoot;

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/jmemo/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/com.ianglei.jmemo/"; // filePath: /data/data/
		}
	}


	public static boolean checkNetState(Context context){
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	public static void showToask(Context context, String tip){
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	public static String getStorageRoot()
	{
		sdRoot= Environment.getExternalStorageDirectory().getPath();
		
		return sdRoot;
	}
	
	public static String getAppRoot()
	{
		String appRoot = getRootFilePath();
		
		isDirExist(appRoot);
		
		return appRoot;
	}
	
	public static void isDirExist(String dir)
	{
		File file = new File(dir);
		if(!file.exists())
		{
			file.mkdir();
		}
	}
	
	public static String getResourcePath(String url)
	{
		String resourcePath = "";
		Pattern p = null;
		if(url.contains("jpg"))
		{
			resourcePath = getAppRoot() + "jpg/";			
			p = Pattern.compile("([^/]{3,})jpg");
		}
		else if(url.endsWith("mp3"))
		{
			resourcePath = getAppRoot() + "mp3/";			
			p = Pattern.compile("([^/]{3,})mp3");
		}
		if(url.endsWith("pdf"))
		{
			resourcePath = getAppRoot() + "pdf/";			
			p = Pattern.compile("([^/]{3,})pdf");
		}
		
		isDirExist(resourcePath);
		
		Matcher m = p.matcher(url);
		String name = null;
		while (m.find()) {
			name = m.group(0) == null ? "" : m.group(0);
			resourcePath += name;
		}
		
		return resourcePath;
	}
}
