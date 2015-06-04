package com.jr.mynewsclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jr.utils.MyApplication;
import com.jr.utils.MyListView;
import com.jr.utils.RoundedImageView;
import com.jr.utils.news;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends BaseActivity {


      
	Activity activity = this;
	com.jr.utils.RoundedImageView imageView;
	ImageView leftmenuImageView;
	ListView menulist;
	TextView showLoginState;
	View dragView;
	String currentCat="头条";
	SlidingUpPanelLayout slidingupLayout;
	String[] menuString = new String[]{
			"头条",
			"国内",
			"国际",
			"科技",
			"军事",
			"娱乐"
	};
	List<String> menuListString = new ArrayList<String>();
	MyListView newsListView;
	List<news> newsList=new ArrayList<news>();
	private Handler handler;
	MyAdapter adapter;
	ProgressBar update_bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		final MenuDrawer mDrawer = MenuDrawer.attach(activity);
		mDrawer.setContentView(R.layout.activity_main);
		mDrawer.setMenuView(R.layout.leftmenu);
		imageView = (RoundedImageView) findViewById(R.id.headImage);
		update_bar = (ProgressBar) findViewById(R.id.update_bar);
		leftmenuImageView = (ImageView) findViewById(R.id.backImage);
		init_title();
		//leftmenuImageView.setOnClickListener(null);
		leftmenuImageView.setImageResource(R.drawable.btn_more_n_1);
		leftmenuImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//MainActivity.this.finish();
				//mDrawer.closeMenu();
				mDrawer.openMenu();
			}
		});
		
		menulist = (ListView) findViewById(R.id.menulist);
		//dragView = findViewById(R.id.dragview);
		slidingupLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		//slidingupLayout.setDragView(dragView);
		for(String menuitem:menuString)
		{
			menuListString.add(menuitem);
		}
		menulist.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, menuListString));
		menulist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				init_news(menuString[arg2]);
				currentCat=menuString[arg2];
				mDrawer.closeMenu();
			}
		});
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.defaulthead);
		imageView.setImageBitmap(bm);
		
		newsListView= (MyListView) findViewById(R.id.newsList);
		adapter = new MyAdapter();
		newsListView.setAdapter(adapter);
		newsListView.setonRefreshListener(new MyListView.OnRefreshListener() {  
			  
            @Override  
            public void onRefresh() {  
            	init_news(currentCat);
        }});  
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1243)
					adapter.notifyDataSetChanged();
					newsListView.onRefreshComplete();
					update_bar.setVisibility(View.GONE);
				super.handleMessage(msg);
			}
			
		};
		init_news("头条");

		newsListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, NewsDetail.class);
				intent.putExtra("id", newsList.get(arg2-1).getId());
				intent.putExtra("title", newsList.get(arg2-1).getTitle());
				intent.putExtra("time", newsList.get(arg2-1).getTime());
				startActivity(intent);
				
			}
			
		});
		
		showLoginState = (TextView) findViewById(R.id.showLoginState);
		
		if(MyApplication.getLoginPreg().getBoolean("state", false))
			{
			 String username = MyApplication.getLoginPreg().getString("username", "");

		    	try {
		    		username = URLEncoder.encode(username,"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			showLoginState.setText(username);
			showLoginState.setOnClickListener(null);
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
			 imageLoader.loadImage(MyApplication.NEWSURL+"returnheadimage.php?id="+username, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					imageView.setImageBitmap(arg2);
					System.out.println(arg0);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			
			}
		else 
			{
			//textview.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			showLoginState.setText(Html.fromHtml("<u>"+"登录"+"</u>"));
			showLoginState.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this, Login.class);
					startActivityForResult(intent,0);
				}
			});
			
			}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 0:
        	if(resultCode==RESULT_OK)
        		if(MyApplication.getLoginPreg().getBoolean("state", false))
    			{showLoginState.setText(MyApplication.getLoginPreg().getString("username", ""));
    			showLoginState.setOnClickListener(null);}
        	break;
        
        default:
            break;
        }
    }


	private void init_news(final String cat){
		update_bar.setVisibility(View.VISIBLE);
new Thread(new Runnable() {
			


			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient httpClient=new DefaultHttpClient();
				//URI url=URI.create("http://192.168.43.21/json.json");
				HttpGet httpGet=new HttpGet(MyApplication.NEWSURL+"getnewslist.php?cat="+cat);
			//	HttpGet httpGet=new HttpGet("http://192.168.191.5/xml.txt");
				try {
					HttpResponse httpResponse=httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode()==200) {
						HttpEntity httpEntity=httpResponse.getEntity();
						String result=EntityUtils.toString(httpEntity,"UTF-8");
						Log.v("MainActivity", result);
						Gson gson=new Gson();
						newsList=gson.fromJson(result.trim(),new TypeToken<List<news>>(){}.getType());
						
						
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					Message msg=Message.obtain();
					msg.what=1243;
					
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsList==null?0:newsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v=LayoutInflater.from(getApplicationContext()).inflate(R.layout.news, null);
			TextView title=(TextView) v.findViewById(R.id.title);
			TextView content=(TextView) v.findViewById(R.id.content);
			//ImageView iv=(ImageView) v.findViewById(R.id.image);
			
			title.setText(newsList.get(position).getTitle());
			content.setText(newsList.get(position).getDesc());
			return v;
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(MyApplication.getLoginPreg().getBoolean("state", false))
		{showLoginState.setText(MyApplication.getLoginPreg().getString("username", ""));
		showLoginState.setOnClickListener(null);}
		
		super.onResume();
	}


  

}
