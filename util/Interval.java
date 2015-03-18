package util;

public class Interval {

	protected double a;
	protected double b;

	public Interval(double a, double b) {
		this.a = a;
		this.b = b;
	}

	public double getLeft() {
		return a;
	}

	public double getRight() {
		return b;
	}
}
