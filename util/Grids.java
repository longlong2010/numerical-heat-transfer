package util;

public class Grids {

	protected double[] t;
	protected double[] x;

	public Grids(int m, int n) {
		t = new double[m];
		x = new double[n];
	}

	public void setX(int i, double v) {
		x[i] = v;
	}

	public void setT(int i, double v) {
		t[i] = v;
	}

	public double getX(int i) {
		return x[i];
	}

	public double getT(int i) {
		return t[i];
	}
}
