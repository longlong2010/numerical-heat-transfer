package matrix.vector;

import matrix.Matrix;

public class Vector extends Matrix {

	public Vector(int m) {
		super(m, 1);
	}

	public void set(int i, double v) {
		super.set(i, 0, v);
	}

	public double get(int i) {
		return super.get(i, 0);
	}

	public double length() {
		int row = this.getRowSize();
		double length = 0;
		for (int i = 0; i < row; i++) {
			double v = this.get(i);
			length += v * v;
		}
		return Math.sqrt(length);
	}
}
