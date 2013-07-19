package com.example.scaletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
	
	public Bitmap bmp=BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.game01);
	public int ic_width;
	public int ic_height;
	public int sc_width=MainActivity.src_width;
	public int sc_height=MainActivity.src_height;
	public Bitmap pic;
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//scale();
		// TODO Auto-generated constructor stub
	}
	public void play(){
		
		MyView.this.invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint paint=new Paint();
		canvas.drawBitmap(bmp, 0, 0, paint);
	}
	public Bitmap scale(){
		ic_width=bmp.getWidth();
		ic_height=bmp.getHeight();
		float scw=((float)sc_width)/ic_width;
		float sch=((float)sc_height)/ic_height;
		Matrix matrix = new Matrix();
		matrix.postScale(scw, sch);
		pic=Bitmap.createBitmap(bmp, 0, 0, ic_width, ic_height, matrix,true);
		return pic;
	}

}
