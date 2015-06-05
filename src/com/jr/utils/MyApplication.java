package com.jr.utils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyApplication extends Application{
	
	private static Context context;
	private  static SharedPreferences mPref;
	private Editor editor;
	public ImageLoader imageLoader;
	public static final String NEWSURL=//"http://192.168.191.1/newsclient/";
	"http://113.251.222.116/newsclient/";
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();
		mPref=getSharedPreferences("loginstate", Context.MODE_PRIVATE);
		editor=mPref.edit();
		imageLoader=ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	};
	public static SharedPreferences getLoginPreg() {
		return mPref;
	}
	public static Context getContext() {
		return context;
	}
}
