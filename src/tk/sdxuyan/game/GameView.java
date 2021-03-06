package tk.sdxuyan.game;

import tk.sdxuyan.tool.Contants;
import tk.sdxuyan.tool.onScoreListener;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

public class GameView extends BoardView implements onScoreListener {

	public GameView(Context context, AttributeSet set) {
		super(context, set);
	}

	public void play() {
		setMap();
		GameView.this.invalidate();
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
		if (flag) {
			soundPlay.play(Contants.ID_SOUND_WIN, 0);
		}
		return flag;
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
								GameView.this.invalidate();
								return true;
							}
						}
					}

				}
			}
		}
		GameView.this.invalidate();
		return false;
	}

}
