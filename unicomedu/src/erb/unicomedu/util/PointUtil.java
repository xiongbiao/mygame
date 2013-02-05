package erb.unicomedu.util;

import java.util.ArrayList;
import java.util.List;

public class PointUtil {
	private   Point p_0 = new Point(0.0, 0.0);
	private   Point p_1 = new Point(360.0, 0.0);
	private   Point p_2 = new Point(375.0, 30.0);
	private   Point p_3 = new Point(350.0, 60.0);
	private   Point p_4 = new Point(270.0, 45.0);
	private   Point p_5 = new Point(125.0, 90.0);
	private   Point p_6 = new Point(110.0, 150.0);
	private   Point p_7 = new Point(100.0, 100.0);
	private   Point p_8 = new Point(0.0, 150.0);

	private   Point p_9 = new Point(0.0, 220.0);
	private   Point p_10 = new Point(30.0, 355.0);
	private   Point p_11 = new Point(90.0, 365.0);
	private   Point p_12 = new Point(130.0, 370.0);
	private   Point p_13 = new Point(220.0, 340.0);
	private   Point p_14 = new Point(285.0, 255.0);
	private   Point p_15 = new Point(365.0, 205.0);

	private   Point p_16 = new Point(480.0, 130.0);
	private   Point p_17 = new Point(480.0, 0.0);

	private   Point p_18 = new Point(480.0, 340.0);
	private   Point p_19 = new Point(465.0, 380.0);
	private   Point p_20 = new Point(360.0, 375.0);
	private   Point p_21 = new Point(315.0, 440.0);
	private   Point p_22 = new Point(200.0, 430.0);
	private   Point p_23 = new Point(105.0, 470.0);

	private   Point p_24 = new Point(140.0, 510.0);
	private   Point p_25 = new Point(90.0, 525.0);
	private   Point p_26 = new Point(15.0, 470.0);

	private   Point p_27 = new Point(150.0, 635.0);
	private   Point p_28 = new Point(170.0, 732.0);
	private   Point p_29 = new Point(320.0, 732.0);

	private   Point p_30 = new Point(355.0, 685.0);
	private   Point p_31 = new Point(455.0, 720.0);
	private   Point p_32 = new Point(435.0, 545.0);
	private   Point p_33 = new Point(415.0, 495.0);
	private   Point p_34 = new Point(370.0, 475.0);
	private   Point p_35 = new Point(315.0, 505.0);
	private   Point p_36 = new Point(480.0, 475.0);
	private   Point p_37 = new Point(185.0, 375.0);
	private   Point point;
	private   ArrayList<ArrayList<Point>> pList;

	public PointUtil() {
		pList = new ArrayList<ArrayList<Point>>();
		pList.add(getHD());
		pList.add(getCH());
		pList.add(getBY());
		pList.add(getLG());
		pList.add(getTH());
		pList.add(getLW());
		pList.add(getYX());
		pList.add(getHZ());
		pList.add(getHP());
		pList.add(getPY());
		point = new Point();
	}
	
	public   void unDel(){
		point = null;
		pList.clear();
	}

	private  ArrayList<Point> getHD() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_0);
		pList.add(p_1);
		pList.add(p_2);
		pList.add(p_3);
		pList.add(p_4);
		pList.add(p_5);
		pList.add(p_6);
		pList.add(p_7);
		pList.add(p_8);
		return pList;
	}

	private  ArrayList<Point> getCH() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_1);
		pList.add(p_2);
		pList.add(p_16);
		pList.add(p_17);
		return pList;
	}

	private  ArrayList<Point> getBY() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_8);
		pList.add(p_7);
		pList.add(p_6);
		pList.add(p_5);
		pList.add(p_4);
		pList.add(p_3);
		pList.add(p_15);
		pList.add(p_14);
		pList.add(p_13);
		pList.add(p_12);
		pList.add(p_11);
		pList.add(p_10);
		pList.add(p_9);
		return pList;
	}

	private  ArrayList<Point> getLG() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_3);
		pList.add(p_2);
		pList.add(p_16);
		pList.add(p_18);
		pList.add(p_19);
		pList.add(p_20);
		pList.add(p_14);
		pList.add(p_15);
		return pList;
	}

	private  ArrayList<Point> getTH() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_12);
		pList.add(p_13);
		pList.add(p_14);
		pList.add(p_20);
		pList.add(p_21);
		pList.add(p_22);
		pList.add(p_37);
		return pList;
	}

	private  ArrayList<Point> getLW() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_10);
		pList.add(p_11);
		pList.add(p_23);
		pList.add(p_24);
		pList.add(p_25);
		pList.add(p_26);
		return pList;
	}

	private  ArrayList<Point> getYX() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_11);
		pList.add(p_12);
		pList.add(p_37);
		pList.add(p_22);
		pList.add(p_23);
		return pList;
	}

	private  ArrayList<Point> getHZ() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_23);
		pList.add(p_22);
		pList.add(p_21);
		pList.add(p_35);
		pList.add(p_24);
		return pList;
	}

	private  ArrayList<Point> getHP() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_21);
		pList.add(p_20);
		pList.add(p_19);
		pList.add(p_36);
		pList.add(p_32);
		pList.add(p_33);
		pList.add(p_34);
		pList.add(p_35);
		return pList;
	}

	private  ArrayList<Point> getPY() {
		ArrayList<Point> pList = new ArrayList<Point>();
		pList.add(p_25);
		pList.add(p_24);
		pList.add(p_35);
		pList.add(p_34);
		pList.add(p_33);
		pList.add(p_32);
		pList.add(p_31);
		pList.add(p_30);
		pList.add(p_29);
		pList.add(p_28);
		pList.add(p_27);
		return pList;
	}

	public int getNowPoint(double px, double py) {
		int rInt = -1;
		// ArrayList<ArrayList<Point>> pList = new
		// ArrayList<ArrayList<Point>>();
		// pList.add(getHD());
		// pList.add(getCH());
		// pList.add(getBY());
		// pList.add(getLG());
		// pList.add(getTH());
		// pList.add(getLW());
		// pList.add(getYX());
		// pList.add(getHZ());
		// pList.add(getHP());
		// pList.add(getPY());

		point.setX(px);
		point.setY(py);
		for (int i = 0; i < pList.size(); i++) {
			if (InPolygon(pList.get(i), point) == 0) {
				rInt = i;
				break;
			}
		}
		return rInt;
	}

	double INFINITY = 1e10;

	double ESP = 1e-5;

	int MAX_N = 1000;

	List<Point> Polygon;

	double Multiply(Point p1, Point p2, Point p0) {
		return ((p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y));
	}

	private boolean IsOnline(Point point, LineSegment line) {
		return ((Math.abs(Multiply(line.pt1, line.pt2, point)) < ESP) &&

		((point.x - line.pt1.x) * (point.x - line.pt2.x) <= 0) &&

		((point.y - line.pt1.y) * (point.y - line.pt2.y) <= 0));
	}

	private boolean Intersect(LineSegment L1, LineSegment L2) {
		return ((Math.max(L1.pt1.x, L1.pt2.x) >= Math.min(L2.pt1.x, L2.pt2.x))
				&&

				(Math.max(L2.pt1.x, L2.pt2.x) >= Math.min(L1.pt1.x, L1.pt2.x))
				&&

				(Math.max(L1.pt1.y, L1.pt2.y) >= Math.min(L2.pt1.y, L2.pt2.y))
				&&

				(Math.max(L2.pt1.y, L2.pt2.y) >= Math.min(L1.pt1.y, L1.pt2.y))
				&&

				(Multiply(L2.pt1, L1.pt2, L1.pt1)
						* Multiply(L1.pt2, L2.pt2, L1.pt1) >= 0) &&

		(Multiply(L1.pt1, L2.pt2, L2.pt1) * Multiply(L2.pt2, L1.pt2, L2.pt1) >= 0)

		);
	}

	private  int InPolygon(List<Point> polygon, Point point)

	{
		int n = polygon.size();
		int count = 0;
		LineSegment line = new LineSegment();

		line.pt1 = point;
		line.pt2.y = point.y;
		line.pt2.x = -INFINITY;
		for (int i = 0; i < n; i++) {
			LineSegment side = new LineSegment();
			side.pt1 = polygon.get(i);
			side.pt2 = polygon.get((i + 1) % n);
			if (IsOnline(point, side)) {
				return 1;
			}
			if (Math.abs(side.pt1.y - side.pt2.y) < ESP) {
				continue;
			}
			if (IsOnline(side.pt1, line)) {
				if (side.pt1.y > side.pt2.y)
					count++;
			} else if (IsOnline(side.pt2, line)) {
				if (side.pt2.y > side.pt1.y)
					count++;
			} else if (Intersect(line, side)) {
				count++;
			}
		}
		if (count % 2 == 1) {
			return 0;
		} else {
			return 2;
		}
	}

	class LineSegment {
		public Point pt1;
		public Point pt2;

		public LineSegment() {
			this.pt1 = new Point();
			this.pt2 = new Point();
		}
	}

}
