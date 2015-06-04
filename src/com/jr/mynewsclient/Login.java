package com.jr.mynewsclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jr.utils.MyApplication;
import com.jr.utils.Utils;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseActivity implements OnClickListener{
	View v;
	int[] location;
	int width;
	private EditText username;
	private EditText passwd;
	private Boolean loginBoolean=false;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==2){
			Toast.makeText(getApplicationContext(), "登录成功", 0).show();
			loginBoolean=true;
			Login.this.finish();
			}
			if(msg.what==1) {
				Toast.makeText(getApplicationContext(), "注册成功，请登录", 0).show();
				username.setText("");
				passwd.setText("");
			}
			if(msg.what==11)
			Toast.makeText(getApplicationContext(), "网络或服务器发生错误", 0).show();
			if(msg.what==12)
				Toast.makeText(getApplicationContext(), "用户名已被注册", 0).show();
			if(msg.what==22)
				Toast.makeText(getApplicationContext(), "登录失败，请检查用户名和密码", 0).show();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init_title();
		username = (EditText) findViewById(R.id.username);
		passwd = (EditText) findViewById(R.id.password);
		Button button1=(Button) findViewById(R.id.button1);
		Button button2=(Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		//拿到屏幕的大小
		width=this.getWindowManager().getDefaultDisplay().getWidth();
		v=findViewById(R.id.loginlayout);
		location=new int[2];
		
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		v.getLocationOnScreen(location);
		if(event.getY()>location[1] && event.getY()<location[1]+50 && event.getX()>width-50){
			this.finish();
			return super.onTouchEvent(event);
		}else{
			return false;
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(loginBoolean)
		setResult(RESULT_OK);
		super.finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(loginBoolean)
			setResult(RESULT_OK);
		super.onBackPressed();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			makeNewAccount();
			break;
		case R.id.button2:
			login();
			break;
		default:
			break;
		}
	}


	private void makeNewAccount() {
		// TODO Auto-generated method stub
		final String usernameString = username.getText().toString().trim();
		final String passwdString = passwd.getText().toString().trim();
		if (!usernameString.equals("") && !passwdString.equals("")) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(MyApplication.NEWSURL+"makeNewAccount.php");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", usernameString));
					params.add(new BasicNameValuePair("passwd", Utils.md5(passwdString)));
					System.out.println(Utils.md5(passwdString));
					try {
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse response = httpClient.execute(httpPost);
						if(response.getStatusLine().getStatusCode() == 200)
						{
							HttpEntity entity2 = response.getEntity();
							String result = EntityUtils.toString(entity2,"utf-8");
							if(result.equals("success")) {
								
								Message msg = Message.obtain();
								msg.what=1;
								handler.sendMessage(msg);
								
							} else {
								Message msg = Message.obtain();
								msg.what=12;
								handler.sendMessage(msg);
							}
								
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Message msg = Message.obtain();
						msg.what=11;
						handler.sendMessage(msg);
						
					}
				}
			}).start();
		}
		else Toast.makeText(this, "账号或密码不能为空", 0).show();
		
	}


	private void login() {
		// TODO Auto-generated method stub
		final String usernameString = username.getText().toString().trim();
		final String passwdString = passwd.getText().toString().trim();
		if (!usernameString.equals("") && !passwdString.equals("")) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(MyApplication.NEWSURL+"login.php");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", usernameString));
					params.add(new BasicNameValuePair("passwd", Utils.md5(passwdString)));
					System.out.println(Utils.md5(passwdString));
					try {
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse response = httpClient.execute(httpPost);
						if(response.getStatusLine().getStatusCode() == 200)
						{
							HttpEntity entity2 = response.getEntity();
							String result = EntityUtils.toString(entity2,"utf-8");
							if(result.equals("success")) {
								Editor editor = MyApplication.getLoginPreg().edit();
								editor.clear();
								editor.putBoolean("state", true);
								editor.putString("username", usernameString);
								editor.commit();
								Message msg = Message.obtain();
								msg.what=2;
								handler.sendMessage(msg);
								
							} else {
								Message msg = Message.obtain();
								msg.what=22;
								handler.sendMessage(msg);
							}
								
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Message msg = Message.obtain();
						msg.what=11;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
		else Toast.makeText(this, "账号或密码不能为空", 0).show();
	}
	
	
}
