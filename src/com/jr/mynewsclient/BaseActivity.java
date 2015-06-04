package com.jr.mynewsclient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.Inflater;

import com.jr.utils.ActivityContainer;
import com.jr.utils.MyApplication;
import com.jr.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;

public class BaseActivity extends Activity{
	ImageView backImageView;
	ImageView actionImageView;
	private PopupMenu popupMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityContainer.add(this);
	}
	public void init_title(){
		backImageView = (ImageView) findViewById(R.id.backImage);
		actionImageView = (ImageView) findViewById(R.id.actionImage);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseActivity.this.finish();
			}
		});
		popupMenu = new PopupMenu(BaseActivity.this, actionImageView);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getItemId()) {
				case R.id.action_about:
					//aboutSoftware
					
					View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.about, null);
					final TextView actionBarTitle = (TextView) findViewById(R.id.actionBarTitle);
					actionBarTitle.setText("关于");
					PopupWindow popupWindow = new PopupWindow(v,ViewGroup.LayoutParams.MATCH_PARENT,  
			                ViewGroup.LayoutParams.MATCH_PARENT,true);
					popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
				    popupWindow.setTouchable(true); // 设置PopupWindow可触摸
				    popupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pic_tip_bai_bg));
					popupWindow.showAsDropDown(backImageView);
					popupWindow.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss() {
							// TODO Auto-generated method stub
							actionBarTitle.setText(getString(R.string.app_name));
						}
					});
					break;
				case R.id.action_update:
					//update
					Toast.makeText(BaseActivity.this, "已是最新版本", 0).show();
					break;
				case R.id.action_change:
					//changheadview
					if(
							!MyApplication.getLoginPreg().getBoolean("state", false))
								{
								Toast.makeText(getBaseContext(), "请先登录！", 0).show();
								break;}
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent,2);
					Toast.makeText(BaseActivity.this, "changheadview", 0).show();
					break;
				case R.id.action_logout:
					MyApplication.getLoginPreg().edit().clear().apply();
					break;
				case R.id.action_exit:
					ActivityContainer.finish();
					break;
				default:
					//Toast.makeText(BaseActivity.this, arg0.getItemId(), 0).show();
					break;
				}
				return false;
			}
		});
		popupMenu.inflate(R.menu.main);
		actionImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				popupMenu.show();
			}
		});
	}
	public static void startAction() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 switch (requestCode) {
	        case 2:
	            
	            Uri uri = null;
	            if (data == null) {
	                return;
	            }
	            if (resultCode == RESULT_OK) {
	                if (!Environment.getExternalStorageState().equals(
	                        Environment.MEDIA_MOUNTED)) {
	                    Toast.makeText(this, "SD不存在",1).show();
	                    return;
	                }
	                uri = data.getData();
	                saveCropAvator(uri);
	                //Utils.startImageAction(uri, 200, 200,3, true,BaseActivity.this);
	            } else {
	                Toast.makeText(this, "图片获取失败",1).show();
	            }
	            break;

	        default:
	            break;
	        }
		
	}
	public void saveCropAvator(Uri uri) {
		// TODO Auto-generated method stub
		
		ImageLoader imageLoader= ImageLoader.getInstance();
		 Bitmap bitmap=imageLoader.loadImageSync(uri.toString());
		try {
			//bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
			if (bitmap != null) {
	               // bitmap = toRoundCorner(bitmap, 10);//锟斤拷锟斤拷圆锟角达拷锟�锟斤拷
	            	((RoundedImageView)BaseActivity.this.findViewById(R.id.headImage)).setImageBitmap(bitmap);
	            	 upload(uri);
	            	 if (bitmap != null && bitmap.isRecycled()) {
	                     bitmap.recycle();
	                 }
	            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			upload(uri);
		} 
            
	}
	private void upload(Uri uri) {
		// TODO Auto-generated method stub
		if(
				!MyApplication.getLoginPreg().getBoolean("state", false))
					{
					Toast.makeText(getBaseContext(), "请先登录！", 0).show();
					return;}
		String username=MyApplication.getLoginPreg().getString("username", "");
		UploadFilesTask mytTask = new UploadFilesTask();
		mytTask.execute(MyApplication.NEWSURL+"upload.php",uri,username);
		
	}
	
	class UploadFilesTask extends AsyncTask<Object, Integer, String> {
	    protected String doInBackground(Object... objs) {
		String end = "\r\n";
		String boundary = "---------------------------265001916915724";
		String username=(String) objs[2];
		try {
			username=URLEncoder.encode(username, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}   
		try {
			URL url = new URL((String)objs[0]);
			HttpURLConnection con;
			con = (HttpURLConnection) url.openConnection();
		
		/* Output to the connection. Default is false,set to true because post method must write something to the connection */
		//con.setDoOutput(true);
		/* Read from the connection. Default is true.*/
			con.setDoInput(true);  
	           con.setDoOutput(true);  
	           con.setUseCaches(true);
		/* 设置请求属性 */
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Content-Type", "text/plain"); 
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());  //output to the connection
		String fileMeta = "--" + boundary + end +  
                "Content-Disposition: form-data; name=\"file\"; filename=\""+username+"\"" + end +  
                "Content-Type: image/jpeg" + end + end;  
// 向流中写入fileMeta  
        ds.write(fileMeta.getBytes()); 
		Uri uri = (Uri) objs[1];
		
		String[] proj = { MediaStore.Images.Media.DATA };  
		  
		Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);  
		  
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
		  
		actualimagecursor.moveToFirst();  
		  
		String img_path = actualimagecursor.getString(actual_image_column_index);  
		  
		File file = new File(img_path); 
		
		FileInputStream fStream =new FileInputStream(file);  
         /* 设置每次写入1024bytes */  
         int bufferSize =1024;  
         byte[] buffer =new byte[bufferSize];  
         int length =-1;  
         /* 从文件读取数据至缓冲区 */  
         while((length = fStream.read(buffer)) !=-1)  
         {  
           /* 将资料写入DataOutputStream中 */  
           ds.write(buffer, 0, length);  
         }  
        //--------------------伪造images.jpg信息结束-----------------------------------  
		ds.writeBytes(end+end); 
        //Log.d(TAG, "结束上传");  
        // POST Data结束  
        ds.writeBytes("--" + boundary + "--"); 
		ds.close();
		InputStream is = con.getInputStream();  //input from the connection 正式建立HTTP连接
		int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1)
        {
            b.append((char) ch);
        }
      /* 显示网页响应内容 */
		return b.toString().trim();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "上传失败，请重试。";
		}
		////////////////////////////////////////////////////////////
	        
	           // publishProgress((int) ((i / (float) count) * 100));
	       
	    }

	    protected void onProgressUpdate(Integer... progress) {
	        //setProgressPercent(progress[0]);
	    }

	    protected void onPostExecute(String result) {
	    	Log.v("sfdgtfffffff", result);
	    	
	    	try {
	    		result = URLDecoder.decode(result, "utf-8");
	    		String teString = URLEncoder.encode("我","utf-8");
	    		Log.v("encode我", teString);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Log.v("sfdgtfffffff", result);
	        Toast.makeText(getBaseContext(),result, 0).show();
	    }
	}
}


