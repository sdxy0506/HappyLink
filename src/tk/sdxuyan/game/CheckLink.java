package tk.sdxuyan.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;

public class CheckLink {
	private int[][] map;
	/**
	 * link_e是有一个拐点连线的拐点 link_c，link_d分别是两个拐点连线时的拐点
	 */
	private Point link_c, link_d, link_e;
	/**
	 * 判断连接线的状态 flag=1时为水平连线 flag=2时为竖直连线 flag=3时为一个拐点的连线 flag=4时为两个拐点的连线
	 */

	private List<Point> path = new ArrayList<Point>();

	public CheckLink(int[][] map) {
		this.map = map;
		path.clear();
	}

	public List<Point> getPath() {
		return path;
	}

	/**
	 * 判断两点水平是否可以连线
	 */
	private boolean horzion(Point a, Point b) {
		if (a.x == b.x && a.y == b.y)
			return false;
		int x_start = a.y <= b.y ? a.y : b.y;
		int x_end = a.y <= b.y ? b.y : a.y;
		for (int x = x_start + 1; x < x_end; x++) {
			if (map[a.x][x] != 0) {
				return false;
			}
		}
		return true;

	}

	/**
	 * 判断两点竖直之间是否可以连线 *
	 */
	private boolean vertical(Point a, Point b) {
		if (a.x == b.x && a.y == b.y)
			return false;
		int y_start = a.x <= b.x ? a.x : b.x;
		int y_end = a.x <= b.x ? b.x : a.x;
		for (int y = y_start + 1; y < y_end; y++) {
			if (map[y][a.y] != 0) {
				return false;
			}
		}
		return true;
	}

	private boolean oneCorner(Point a, Point b) {
		Point c = new Point(b.x, a.y);
		Point d = new Point(a.x, b.y);
		if (map[c.x][c.y] == 0) {
			boolean method1 = vertical(a, c) && horzion(b, c);
			link_e = c;
			return method1;
		}
		if (map[d.x][d.y] == 0) {
			boolean method2 = horzion(a, d) && vertical(b, d);
			link_e = d;
			return method2;
		}
		return false;
	}

	public boolean twoCorner(Point a, Point b) {
		LinkedList<Line> linelist = scan(a, b);
		if (linelist.isEmpty())
			return false;
		for (int index = 0; index < linelist.size(); index++) {
			Line l = (Line) (linelist.get(index));
			if (l.direct == 1) {
				if (vertical(a, l.a) && vertical(b, l.b)) {
					link_c = l.a;
					link_d = l.b;
					return true;
				}
			} else {
				if (horzion(a, l.a) && horzion(b, l.b)) {
					link_c = l.a;
					link_d = l.b;
					return true;
				}
			}
		}
		return false;
	}

	private LinkedList<Line> scan(Point a, Point b) {
		LinkedList<Line> line = new LinkedList<Line>();
		for (int y = a.y - 1; y >= 0; y--) {
			Point c = new Point(a.x, y);
			Point d = new Point(b.x, y);
			if (map[a.x][y] == 0 && map[b.x][y] == 0 && vertical(c, d)) {
				line.add(new Line(0, c, d));
			}
		}
		for (int y = a.y + 1; y < map[a.x].length; y++) {
			Point c = new Point(a.x, y);
			Point d = new Point(b.x, y);
			if (map[a.x][y] == 0 && map[b.x][y] == 0 && vertical(c, d)) {
				line.add(new Line(0, c, d));
			}
		}
		for (int x = a.x - 1; x >= 0; x--) {
			Point c = new Point(x, a.y);
			Point d = new Point(x, b.y);
			if (map[x][a.y] == 0 && map[x][b.y] == 0 && horzion(c, d)) {
				line.add(new Line(1, c, d));
			}
		}
		for (int x = a.x + 1; x < map.length; x++) {
			Point c = new Point(x, a.y);
			Point d = new Point(x, b.y);
			if (map[x][a.y] == 0 && map[x][b.y] == 0 && horzion(c, d)) {
				line.add(new Line(1, c, d));
			}
		}
		return line;
	}

	public boolean checklink(Point a, Point b) {
		path.clear();
		if (map[a.x][a.y] != map[b.x][b.y]) {
			return false;
		}
		if (a.x == b.x && horzion(a, b)) {
			path.add(a);
			path.add(b);
			return true;
		} else if (a.y == b.y && vertical(a, b)) {
			path.add(a);
			path.add(b);
			return true;
		} else if (oneCorner(a, b)) {
			path.add(a);
			path.add(link_e);
			path.add(b);
			return true;
		} else if (twoCorner(a, b)) {
			path.add(a);
			path.add(link_c);
			path.add(link_d);
			path.add(b);
			return true;
		} else
			return false;
	}

	/**
	 * 检测当前是否有能连接的图块
	 * */
	public boolean die(int[][] map) {
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[i].length - 1; j++) {
				if (map[i][j] != 0) {
					Point aa = new Point(i, j);
					for (int x = 1; x < map.length - 1; x++) {
						for (int y = 1; y < map[x].length - 1; y++) {
							if (map[x][y] != 0
									&& checklink(aa, new Point(x, y))) {
								getPath().clear();
								return false;
							}
						}
					}

				}
			}
		}
		return true;
	}
}

class Line {
	public Point a, b;
	public int direct;

	public Line() {
	}

	public Line(int direct, Point a, Point b) {
		this.direct = direct;
		this.a = a;
		this.b = b;
	}
}