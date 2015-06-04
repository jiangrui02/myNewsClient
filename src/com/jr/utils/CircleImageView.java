package com.jr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView{

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public CircleImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override  
    protected void onDraw(Canvas canvas) {  
        // super.onDraw(canvas);  
        setScaleType(ScaleType.CENTER_CROP);  
        Drawable drawable = getDrawable();  
  
        if (null == drawable) {  
            return;  
        }  
  
        // ½«drawable×ª»»³Ébitmap
        Bitmap bitmap = Bitmap  
                .createBitmap(  
                        drawable.getIntrinsicWidth(),  
                        drawable.getIntrinsicHeight(),  
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                : Bitmap.Config.RGB_565);  
  
        Canvas srcCanvas = new Canvas(bitmap);  
  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                drawable.getIntrinsicHeight());  
  
        drawable.draw(srcCanvas);  
  
        float cx = getWidth() / 2;  
        float cy = getHeight() / 2;  
  
        float radius = Math.min(getWidth(), getHeight()) / 2;  
  
        Paint borderPaint = new Paint();  
        borderPaint.setAntiAlias(true);  
        borderPaint.setColor(Color.WHITE);  
  
        canvas.drawCircle(cx, cy, radius, borderPaint);  
  
        // »­Í¼  
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,  
                TileMode.CLAMP);  
        Paint paint = new Paint();  
        paint.setShader(shader);  
        paint.setAntiAlias(true);  
        canvas.drawCircle(cx, cy, radius - 5, paint);  
  
    }  


}
