package com.xuyan.happylink;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View{

	/**
	 * 定义数组的行数和列数
	 */
	protected static final int xCount=12;
	protected static final int yCount=10;
	/**
	 * 定义棋盘map
	 */
	public int[][] map=new int[xCount][yCount];
	/**
	 * 定义图标的尺寸，图标数量
	 */
	protected int icon_size;
	protected int iconCounts=19;
	/**
	 * 定义存放图标资源的数组
	 */
	protected Bitmap[] icons=new Bitmap[iconCounts];
	/**
	 * 定义各类paint
	 */
	private Paint paint_bit=new Paint();
	private Paint paint=new Paint();
	private Paint paint_line=new Paint();
	/**
	 * 定义触摸屏幕的坐标
	 */
	protected int touch_x,touch_y;
	/**
	 * 定义一个list用来存放选择的点
	 */
	protected List<Point> selected = new ArrayList<Point>();
	/**
	 * 定义要连线的两个点
	 */
	protected Point a,b;
	/**
	 * 实例化检测算法，并把map传入其中
	 */
	public CheckLink check=new CheckLink(map);

	public static SoundPlay soundPlay;

	public static final int ID_SOUND_CHOOSE = 0;
	public static final int ID_SOUND_DISAPEAR = 1;
	public static final int ID_SOUND_WIN = 4;
	public static final int ID_SOUND_LOSE = 5;
	public static final int ID_SOUND_REFRESH = 6;
	public static final int ID_SOUND_TIP = 7;
	public static final int ID_SOUND_ERROR = 8;

	/**
	 * 设置各类paint的参数
	 */
	private void setPaint(){
		paint_bit.setAntiAlias(true);
		paint.setAntiAlias(true);	
		paint.setAlpha(0);
		paint_line.setColor(Color.YELLOW);
		paint_line.setStrokeWidth(4);
	}

	public BoardView(Context context,AttributeSet set) {
		super(context,set);
		calIconSize();
		setPaint();

		icons[0]=null;
		Resources r = getResources();
		loadBitmaps(1, r.getDrawable(R.drawable.fish_01));
		loadBitmaps(2, r.getDrawable(R.drawable.fish_02));
		loadBitmaps(3, r.getDrawable(R.drawable.fish_03));
		loadBitmaps(4, r.getDrawable(R.drawable.fish_04));
		loadBitmaps(5, r.getDrawable(R.drawable.fish_05));
		loadBitmaps(6, r.getDrawable(R.drawable.fish_06));
		loadBitmaps(7, r.getDrawable(R.drawable.fish_07));
		loadBitmaps(8, r.getDrawable(R.drawable.fish_08));
		loadBitmaps(9, r.getDrawable(R.drawable.fish_09));
		loadBitmaps(10, r.getDrawable(R.drawable.fish_10));
		loadBitmaps(11, r.getDrawable(R.drawable.fish_11));
		loadBitmaps(12, r.getDrawable(R.drawable.fish_12));
		loadBitmaps(13, r.getDrawable(R.drawable.fish_13));
		loadBitmaps(14, r.getDrawable(R.drawable.fish_14));
		loadBitmaps(15, r.getDrawable(R.drawable.fish_15));
		loadBitmaps(16, r.getDrawable(R.drawable.fish_16));
		loadBitmaps(17, r.getDrawable(R.drawable.fish_17));
		loadBitmaps(18, r.getDrawable(R.drawable.fish_18));
	}

	public static void initSound(Context context){
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
		soundPlay.loadSfx(context, R.raw.choose, ID_SOUND_CHOOSE);
		soundPlay.loadSfx(context, R.raw.disappear1, ID_SOUND_DISAPEAR);
		soundPlay.loadSfx(context, R.raw.win, ID_SOUND_WIN);
		soundPlay.loadSfx(context, R.raw.lose, ID_SOUND_LOSE);
		soundPlay.loadSfx(context, R.raw.item1, ID_SOUND_REFRESH);
		soundPlay.loadSfx(context, R.raw.item2, ID_SOUND_TIP);
		soundPlay.loadSfx(context, R.raw.alarm, ID_SOUND_ERROR);
	}
	/**
	 * 计算图标的长宽
	 **/
	 private void calIconSize()
	{
		 DisplayMetrics dm = new DisplayMetrics();
		 ((Activity) this.getContext()).getWindowManager()
		 .getDefaultDisplay().getMetrics(dm);
		 icon_size = dm.widthPixels/(yCount);
	}
	 /**
	  * 生成图块，并将其存入icons
	  * @param key
	  * @param d
	  */
	 public void loadBitmaps(int key,Drawable d){
		 Bitmap bitmap = Bitmap.createBitmap(icon_size,icon_size,Bitmap.Config.ARGB_8888);
		 Canvas canvas = new Canvas(bitmap);
		 d.setBounds(0, 0, icon_size, icon_size);
		 d.draw(canvas);
		 icons[key]=bitmap;
	 }
	 /**
	  * 初始化map
	  */
	 public void setMap(){
		 int key=1;
		 for(int i=0;i<map.length;i++){
			 for(int j=0;j<map[i].length;j++){
				 if(i==0||i==map.length-1) map[i][j]=0;
				 else if(j==0||j==map[i].length-1) map[i][j]=0;
				 else {
					 map[i][j]=key;
				 }
			 }
			 key++;
		 }
		 change();
	 }
	 /**
	  * 随机交换map的值，即map中的图片
	  **/
	 public void change(){
		 Random random=new Random();
		 int xc,yc,tc;
		 for(int x=1;x<xCount-1;x++){
			 for(int y=1;y<yCount-1;y++){
				 xc=random.nextInt(xCount-3)+1;
				 yc=random.nextInt(yCount-3)+1;
				 tc=map[x][y];
				 map[x][y]=map[xc][yc];
				 map[xc][yc]=tc;
			 }
		 }
		 soundPlay.play(ID_SOUND_REFRESH, 0);
		 BoardView.this.invalidate();
	 }
	 @Override
	 protected void onDraw(Canvas canvas) {
		 super.onDraw(canvas);
		 for(int i=0;i<map.length-1;i++){
			 for(int j=0;j<map[i].length-1;j++){
				 if(map[i][j]==0) 
					 canvas.drawRect(j*icon_size,i*icon_size,
							 j*icon_size+icon_size,i*icon_size+icon_size,paint);
				 else
					 canvas.drawBitmap(icons[map[i][j]], 
							 j*icon_size, i*icon_size, paint_bit);
			 }
		 }
		 link(canvas);
		 select(canvas);

	 }

	 /**
	  * 两点连线
	  * */
	 private void link(Canvas canvas){
		 List<Point> p=check.getPath();
		 if(p!=null){
			 for(int i=0;i<p.size()-1;i++){
				 Point a=new Point(p.get(i).y*icon_size+icon_size/2,
						 p.get(i).x*icon_size+icon_size/2);
				 Point b=new Point(p.get(i+1).y*icon_size+icon_size/2,
						 p.get(i+1).x*icon_size+icon_size/2);
				 canvas.drawLine(a.x, a.y, b.x, b.y, paint_line);
			 }
		 }
	 }
	 /**
	  * 选中图块时放大图块
	  * @param canvas
	  */
	 private void select(Canvas canvas){
		 for(Point position:selected){
			 Point p = new Point(position.y*icon_size,position.x*icon_size);
			 if(map[position.x][position.y] >= 1){
				 canvas.drawBitmap(icons[map[position.x][position.y]],
						 null,
						 new Rect(p.x-8, p.y-8, p.x + icon_size + 8, p.y + icon_size + 8), 
						 null);
			 }
		 }
	 }
	 public boolean onTouchEvent(MotionEvent event) {
		 touch_x=(int)event.getX();
		 touch_y=(int)event.getY();
		 int y=(touch_x-touch_x%icon_size)/icon_size;
		 int x=(touch_y-touch_y%icon_size)/icon_size;
		 Point p=new Point(x,y);
		 if(touch_x<=icon_size*yCount&&touch_y<=icon_size*xCount){
			 if(map[p.x][p.y] != 0){
				 if(selected.size() == 1){
					 if(check.checklink(selected.get(0),p)){ 
						 a=selected.get(0);
						 b=p;
						 removeMap(a,b);
						 selected.clear();
						 invalidate();
					 }
					 else{
						 selected.clear();
						 selected.add(p);
						 soundPlay.play(ID_SOUND_CHOOSE, 0);
						 invalidate();
					 }
				 }
				 else{
					 selected.add(p);
					 soundPlay.play(ID_SOUND_CHOOSE, 0);
					 invalidate();
				 }
			 }
		 }
		 return super.onTouchEvent(event);
	 }
	 @SuppressLint("HandlerLeak")
	 public void removeMap(Point a,Point b){
		 map[a.x][a.y]=0;
		 map[b.x][b.y]=0;
		 soundPlay.play(ID_SOUND_DISAPEAR, 0);
		 BoardView.this.invalidate();
		 final Handler mHandler=new Handler()
		 {
			 @Override
			 public void handleMessage(Message msg)
			 {
				 if(msg.what==0x111)
				 {
					 check.getPath().clear();
					 BoardView.this.invalidate();
				 }

			 }

		 };
		 class MyThread extends Thread{
			 public void run(){
				 try {
					 Thread.sleep(500);
				 } catch (InterruptedException e) {
					 e.printStackTrace();
				 }
				 Message m=new Message();
				 m.what=0x111;
				 //发送消息
				 mHandler.sendMessage(m);
			 }
		 }
		 Thread my=new MyThread();
		 my.start();
	 }

}
