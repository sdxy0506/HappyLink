package com.xuyan.happylink;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

public class MyView extends BoardView {
	
	/**
	 * ��һ����Ϸʱ��Ϊ100��
	 * */
	private int total_time=100;
	
	/**
	 * �������úͰ����Ĵ���
	 * */
	private int RefreshNum=3;
	private int TipNum=3;
	
 	public MyView(Context context, AttributeSet set) {
		super(context,set);
	}
	
	public void play(){
		TipNum=3;
		RefreshNum=3;		
		setMap();		
		MyView.this.invalidate();
	}
	
	public void next(){
		total_time=total_time-10;
		play();
	}
	
	
	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}

	/**
	 * �������飬����Ƿ�ʤ��
	 * */
	public boolean win(){
		boolean flag=true;
		for(int i=0;i<xCount;i++){
			for(int j=0;j<yCount;j++){
				if(map[i][j]!=0) flag=false;
			}
		}
		return flag;
	}
	
	public int getTotalTime(){
		return total_time;
	}
	
	public int getTipNum(){
		return TipNum;
	}
	
	public int getRefreshNum(){
		return RefreshNum;
	}
	
	/**
	 * ��⵱ǰ�Ƿ��������ӵ�ͼ��
	 * */
	public boolean die(){
		for(int i=1;i<xCount-1;i++){
			for(int j=1;j<yCount-1;j++){
				if(map[i][j]!=0){
					Point aa=new Point(i, j);
					for(int x=1;x<map.length-1;x++){
						for(int y=1;y<map[x].length-1;y++){
							if(map[x][y]!=0&&check.checklink(aa ,new Point(x,y))){
								check.getPath().clear();
								return false;
							}
						}
					}

				}
			}
		}
		return true;
	}
	
	/**
	 * ����map�ҳ����������������㲢���Զ����
	 * */
	public boolean auto_clear(){
		for(int i=1;i<map.length-1;i++){
			for(int j=1;j<map[i].length-1;j++){
				if(map[i][j]!=0){
					a=new Point(i, j);
					for(int x=1;x<map.length-1;x++){
						for(int y=1;y<map[x].length-1;y++){
							if(map[x][y]!=0&&check.checklink(a, new Point(x,y))){
								b=new Point(x, y);
								removeMap(a, b);
								MyView.this.invalidate();
								return true;
							}
						}
					}

				}
			}
		}
		MyView.this.invalidate();
		return false;
	}
}
