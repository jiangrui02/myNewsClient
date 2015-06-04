package com.jr.mynewsclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jr.utils.MyApplication;
import com.jr.utils.Utils;
import com.jr.utils.comment;
import com.jr.utils.news;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetail extends BaseActivity{

	TextView newsTextView;
	MyAdapter adapter;
	ImageView switchView;
	ImageView send_comment;
	EditText mycomment;
	String commentcontent="";
	String newsId="";
	DisplayImageOptions options;
	ImageView imageView;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==23) {
				String content=(String)msg.obj;
				newsTextView.setText(content);
				if(urlList!=null && urlList.size()>0){
					imageLoader.displayImage(MyApplication.NEWSURL+urlList.get(0).url, imageView,  new AnimateFirstDisplayListener());;
					imageView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(NewsDetail.this, showImage.class);
							ArrayList<CharSequence> urlArrayList = new ArrayList<CharSequence>();
							for(ImageUrl s:urlList){
							urlArrayList.add(s.url)	;
							}
							intent.putCharSequenceArrayListExtra("url", urlArrayList);
							startActivity(intent);
						}
					});
				} else {
					imageView.setVisibility(View.GONE);
				}
				
			} else if(msg.what==24) {
			    int commentnumber =msg.arg1;
			  TextView cView=(TextView) findViewById(R.id.commentnum);
			  cView.setText(commentnumber+"");
			  options = new DisplayImageOptions.Builder()  
	            .showStubImage(R.drawable.defaulthead)          // 设置图片下载期间显示的图片  
	            .showImageForEmptyUri(R.drawable.defaulthead)  // 设置图片Uri为空或是错误的时候显示的图片  
	            .showImageOnFail(R.drawable.defaulthead)       // 设置图片加载或解码过程中发生错误显示的图片      
	            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
	            .cacheOnDisc(false)                          // 设置下载的图片是否缓存在SD卡中  
	            .displayer(new RoundedBitmapDisplayer(100))  // 设置成圆角图片  
	            .build();                                   // 创建配置过得DisplayImageOption对象  
				adapter.notifyDataSetChanged();
			}
			if(msg.what==25)
			{
				Toast.makeText(getApplicationContext(), "评论成功", 0).show();
				get_comment(newsId);
				}
			if(msg.what==26)
				Toast.makeText(getApplicationContext(), "评论失败", 0).show();
			if(msg.what==27)
				Toast.makeText(getApplicationContext(), "网络或服务器错误", 0).show();
			super.handleMessage(msg);
		}
		
	};
	List<comment> commentList=new ArrayList<comment>();
	List<ImageUrl> urlList=new ArrayList<ImageUrl>();
	SlidingUpPanelLayout mSlidingUpPanelLayout;
	public ImageLoader imageLoader=ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newsdetail);
		init_title();
		Intent intent = getIntent();
		newsId = intent.getStringExtra("id");
		String title =intent.getStringExtra("title");
		String time =intent.getStringExtra("time");
		imageLoader.init(ImageLoaderConfiguration.createDefault(NewsDetail.this));
		newsTextView = (TextView) findViewById(R.id.newsdetailtextview);
		TextView titleTextView = (TextView)findViewById(R.id.newsdetailTitle);
		mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mSlidingUpPanelLayout.setPanelSlideListener(new PanelSlideListener() {
			
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPanelHidden(View panel) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPanelExpanded(View panel) {
				// TODO Auto-generated method stub
				
					switchView.setImageResource(R.drawable.ic_action_collapse_down);
					//setBackgroundResource(R.drawable.ic_action_collapse);
					mSlidingUpPanelLayout.setTouchEnabled(false);
				
			}
			
			@Override
			public void onPanelCollapsed(View panel) {
				// TODO Auto-generated method stub
					switchView.setImageResource(R.drawable.ic_action_collapse);
					mSlidingUpPanelLayout.setTouchEnabled(true);
			}

			@Override
			public void onPanelAnchored(View panel) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		//mSlidingUpPanelLayout.setTouchEnabled(false);
		switchView =(ImageView) findViewById(R.id.switchsliding);
		send_comment =(ImageView) findViewById(R.id.send_comment);
		imageView = (ImageView)findViewById(R.id.imageView);
		//imageView.setImage(ImageSource.resource(R.drawable.s));
		send_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mycomment = (EditText) findViewById(R.id.mycomment);
				commentcontent = mycomment.getText().toString().trim();
				if(!commentcontent.equals(""))
					{
					if(MyApplication.getLoginPreg().getBoolean("state", false))
						sendComment(commentcontent);//sendComment();//send my comment
					else {
						Intent intent = new Intent(NewsDetail.this, Login.class);
						startActivityForResult(intent, 0);
					}
					}
					
			}
		});
		switchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PanelState pState=mSlidingUpPanelLayout.getPanelState();
				if(pState==PanelState.EXPANDED) {
					mSlidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);// COLLAPSED,
					//switchView.setImageResource(R.drawable.ic_action_collapse);
					//setBackgroundResource(R.drawable.ic_action_collapse);
				}
				if(pState==PanelState.COLLAPSED) {
					mSlidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
					//switchView.setImageResource(R.drawable.ic_action_collapse_down);
				}
			}
		});
		titleTextView.setText(title);
		TextView timeTextView = (TextView)findViewById(R.id.newsdetailTime);
		timeTextView.setText(time);
		get_content(newsId);
		RecyclerView commListView = (RecyclerView) findViewById(R.id.commentlistview);
		//LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		commListView.setLayoutManager(layoutManager);
		adapter = new MyAdapter();
		commListView.setAdapter(adapter);
		get_comment(newsId);
	}

	
	private void get_content(final String newsId){
		new Thread(new Runnable() {
					


					public void run() {
						// TODO Auto-generated method stub
						HttpClient httpClient=new DefaultHttpClient();
						//URI url=URI.create("http://192.168.43.21/json.json");
						HttpGet httpGet=new HttpGet(MyApplication.NEWSURL+"getnewsdetail.php?id="+newsId);
					//	HttpGet httpGet=new HttpGet("http://192.168.191.5/xml.txt");
						try {
							HttpResponse httpResponse=httpClient.execute(httpGet);
							if (httpResponse.getStatusLine().getStatusCode()==200) {
								HttpEntity httpEntity=httpResponse.getEntity();
								String result=EntityUtils.toString(httpEntity,"UTF-8");
								Log.v("NewsDetail", result);
								
								Gson gson=new Gson();
								detailjson jsondata = gson.fromJson(result.trim(), detailjson.class);
								urlList=gson.fromJson(jsondata.urls,new TypeToken<List<ImageUrl>>(){}.getType());
								
								Message msg=Message.obtain();
								msg.what=23;
								msg.obj=jsondata.content;
								handler.sendMessage(msg);
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
	
	private void get_comment(final String id){
		new Thread(new Runnable() {
					


					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpClient httpClient=new DefaultHttpClient();
						//URI url=URI.create("http://192.168.43.21/json.json");
						HttpGet httpGet=new HttpGet(MyApplication.NEWSURL+"getnewscomment.php?id="+id);
					//	HttpGet httpGet=new HttpGet("http://192.168.191.5/xml.txt");
						try {
							HttpResponse httpResponse=httpClient.execute(httpGet);
							if (httpResponse.getStatusLine().getStatusCode()==200) {
								HttpEntity httpEntity=httpResponse.getEntity();
								String result=EntityUtils.toString(httpEntity,"UTF-8");
								Log.v("MainActivity", result);
								Gson gson=new Gson();
								commentjson jsondata = gson.fromJson(result.trim(), commentjson.class);
								commentList=gson.fromJson(jsondata.data,new TypeToken<List<comment>>(){}.getType());
								Message msg=Message.obtain();
								msg.what=24;
								msg.arg1=jsondata.number;
								handler.sendMessage(msg);
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
	
	public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
		int width;
		
		public class ViewHolder extends RecyclerView.ViewHolder{

			TextView username;
			TextView content;
			TextView time;
			ImageView imageView;
			public ViewHolder(View v) {
				super(v);
				// TODO Auto-generated constructor stub
				username=(TextView) v.findViewById(R.id.commentusername);
				content=(TextView) v.findViewById(R.id.commentcontent);
				time=(TextView) v.findViewById(R.id.commenttime);
				imageView=(ImageView) v.findViewById(R.id.commentheadimageview);
			}

		}

		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			return commentList==null?0:commentList.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int position) {
			// TODO Auto-generated method stub
			String devicename = commentList.get(position).getDevicename();
			if(devicename.equals(""))
				devicename = "android客户端";
			arg0.username.setText("  "+commentList.get(position).getUsername());
			arg0.content.setText(commentList.get(position).getContent());
			arg0.time.setText(commentList.get(position).getTime()+" | "+devicename);
			arg0.time.setWidth(width);
			imageLoader.displayImage(MyApplication.NEWSURL+"headimage/"+commentList.get(position).getUrl()+".jpg", arg0.imageView, options, new AnimateFirstDisplayListener());
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
			// TODO Auto-generated method stub
			width=arg0.getWidth();
			View v=LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment,null );
			//arg0.removeView(v);
			
			//ImageView iv=(ImageView) v.findViewById(R.id.image);
			
			
			return new ViewHolder(v);
		}

		
	}
public class commentjson {
	public int number;
	public JsonArray data;
}
public class detailjson {
	public String content;
	public JsonArray urls;
}
public class ImageUrl {
	public String url;
}
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	PanelState pState=mSlidingUpPanelLayout.getPanelState();
	if(pState==PanelState.EXPANDED) {
		mSlidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);// COLLAPSED,
		switchView.setImageResource(R.drawable.ic_action_collapse);
		//setBackgroundResource(R.drawable.ic_action_collapse);
	} else
	super.onBackPressed();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if(resultCode==RESULT_OK){
		switch (requestCode) {
		case 0:
			if(MyApplication.getLoginPreg().getBoolean("state", false)){
			sendComment(commentcontent);
			}
			break;
	
		default:
			break;
		}
	}
	super.onActivityResult(requestCode, resultCode, data);
}


private void sendComment(final String commentcontent) {
	// TODO Auto-generated method stub
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(MyApplication.NEWSURL+"commitcomment.php");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", MyApplication.getLoginPreg().getString("username", "")));
					params.add(new BasicNameValuePair("content", commentcontent));
					params.add(new BasicNameValuePair("devicename", android.os.Build.MODEL));
					params.add(new BasicNameValuePair("titleid", newsId));
					try {
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse response = httpClient.execute(httpPost);
						if(response.getStatusLine().getStatusCode() == 200)
						{
							HttpEntity entity2 = response.getEntity();
							String result = EntityUtils.toString(entity2,"utf-8");
							Message msg=Message.obtain();
								msg.what=25;
								handler.sendMessage(msg);
								
							} else {
								Message msg = Message.obtain();
								msg.what=26;
								handler.sendMessage(msg);
							}
								
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Message msg = Message.obtain();
						msg.what=27;
						handler.sendMessage(msg);
					}
				
				}}).start();
	
			}	
private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {  
    
    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());  

    @Override  
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {  
        if (loadedImage != null) {  
            ImageView imageView = (ImageView) view;  
            // 是否第一次显示  
            boolean firstDisplay = !displayedImages.contains(imageUri);  
            if (firstDisplay) {  
                // 图片淡入效果  
                FadeInBitmapDisplayer.animate(imageView, 500);  
                displayedImages.add(imageUri);  
            }  
        }  
    }  
}
}
