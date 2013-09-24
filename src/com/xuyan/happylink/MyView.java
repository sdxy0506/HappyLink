package com.xuyan.happylink;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

public class MyView extends BoardView {

	/**
	 * 第一关游戏时间为100秒
	 * */
	private int total_time = 100;

	/**
	 * 定义重置和帮助的次数
	 * */
	private int RefreshNum = 3;
	private int TipNum = 3;

	public MyView(Context context, AttributeSet set) {
		super(context, set);
	}

	public void play() {
		TipNum = 3;
		RefreshNum = 3;
		setMap();
		MyView.this.invalidate();
	}

	public void next() {
		total_time = total_time - 10;
		play();
	}

	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}

	/**
	 * 遍历数组，检查是否胜利
	 * */
	public boolean win() {
		boolean flag = true;
		for (int i = 0; i < xCount; i++) {
			for (int j = 0; j < yCount; j++) {
				if (map[i][j] != 0)
					flag = false;
			}
		}
		return flag;
	}

	public int getTotalTime() {
		return total_time;
	}

	public int getTipNum() {
		return TipNum;
	}

	public int getRefreshNum() {
		return RefreshNum;
	}

	/**
	 * 检测当前是否有能连接的图块
	 * */
	public boolean die() {
		return check.die(map);
	}

	/**
	 * 遍历map找出可以相连的两个点并且自动清除
	 * */
	public boolean auto_clear() {
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[i].length - 1; j++) {
				if (map[i][j] != 0) {
					a = new Point(i, j);
					for (int x = 1; x < map.length - 1; x++) {
						for (int y = 1; y < map[x].length - 1; y++) {
							if (map[x][y] != 0
									&& check.checklink(a, new Point(x, y))) {
								b = new Point(x, y);
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
