package com.jr.mynewsclient;

import java.util.ArrayList;
import java.util.List;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jr.utils.MyApplication;
import com.jr.utils.news;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class showImage extends BaseActivity{

	ViewPager viewPager;
	ArrayList<CharSequence> urls;
	ImageLoader imageLoader=ImageLoader.getInstance();
	public static void startAction(){
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image);
		init_title();
		Intent intent = getIntent();
		urls= intent.getCharSequenceArrayListExtra("url");
		for (CharSequence s:urls) {
			System.out.println(s.toString());;
		}
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyAdapter());
	}
	
	private class MyAdapter extends PagerAdapter{

		List<View> views=new ArrayList<View>() ;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return urls == null?0:urls.size();
		}

		
		 @Override  
		    public boolean isViewFromObject(View arg0, Object arg1) {                           
		        // TODO Auto-generated method stub  
		        return arg0 == arg1;  
		    }  
		      
		 public void destroyItem(View collection, int position, Object arg2) {
			   
			   ((ViewPager) collection).removeView(views.get(position));
			   }   
		 
		    @Override  
		    public Object instantiateItem(View view, int position)                                //ÊµÀý»¯Item  
		    {  
		    	View v = LayoutInflater.from(showImage.this).inflate(R.layout.viewpaperitem, null);
		    	final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) v.findViewById(R.id.bigimageView);
		    	TextView tv = (TextView)v.findViewById(R.id.description);
		    	tv.setText((position+1)+"/"+urls.size());
		    	ImageSize targetSize = new ImageSize(80, 50); // result Bitmap will be fit to this size
		    	//Bitmap bitmap =
		    	System.out.println("55555555555555");
		    	imageLoader.loadImage(MyApplication.NEWSURL+urls.get(position).toString(), new SimpleImageLoadingListener() {
		    	    @Override
		    	    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	        // Do whatever you want with Bitmap
		    	    	imageView.setImage(ImageSource.bitmap(loadedImage));
		    	    }
		    	});
		    	System.out.println("66666666666666");
		        ((ViewPager) view).addView(v, 0);  
		        views.add(v);
		          
		        return v;  
		    }
		
	}
}
