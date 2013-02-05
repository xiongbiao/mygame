package erb.unicomedu.util;

public class Point {
	public double x;
	public double y;
	double w = 480.0;
	double h = 730.0;
	public Point() {
	}

	public Point(double p_x, double p_y) {
      x = p_x/w;
      y = p_y/h;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
