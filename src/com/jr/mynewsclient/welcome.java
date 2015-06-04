package com.jr.mynewsclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class welcome extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		SharedPreferences sf=getSharedPreferences("IsFirst", MODE_PRIVATE);
		final Boolean isFirst=sf.getBoolean("isFirst", true);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				if(isFirst){
					Intent intent = new Intent(welcome.this,Guide.class);
					startActivity(intent);
				}
				 else
					{
						 Intent intent = new Intent(welcome.this,MainActivity.class);
						 startActivity(intent);
					}
			
				finish();
			}
		}).start();	
}
}