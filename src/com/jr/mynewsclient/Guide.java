package com.jr.mynewsclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class Guide extends Activity implements OnGestureListener{

	private ViewFlipper viewFlipper;
    private GestureDetector detector;
	private Animation leftInAnimation;
	private Object leftOutAnimation;
	private Object rightInAnimation;
	private Object rightOutAnimation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
        detector = new GestureDetector(this);
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	     
        return this.detector.onTouchEvent(event); //touch事件交给手势处理。
	}
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "e1="+e1.getX()+" e2="+e2.getX()+" e1-e2="+(e1.getX()-e2.getX()));
        
        
        
        if(e1.getX()-e2.getX()>120){
                viewFlipper.setInAnimation(leftInAnimation);
                viewFlipper.setOutAnimation((Animation) leftOutAnimation);
                if(viewFlipper.getDisplayedChild()<2)
            viewFlipper.showNext();//向右滑动
                else {
                	SharedPreferences sf=getSharedPreferences("IsFirst", MODE_PRIVATE);
                	sf.edit().putBoolean("isFirst", false).apply();
            		//final Boolean isFirst=sf.getBoolean("isFirst", true);
                	startActivity(new Intent(Guide.this,MainActivity.class));
                	finish();
                }
            return true;
        }else if(e1.getX()-e2.getY()<-120){
                viewFlipper.setInAnimation((Animation) rightInAnimation);
                viewFlipper.setOutAnimation((Animation) rightOutAnimation);
                viewFlipper.showPrevious();//向左滑动
                return true;
        }
        return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
