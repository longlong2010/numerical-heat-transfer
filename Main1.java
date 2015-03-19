import function.Function;

import util.Interval;

import matrix.Matrix;
import matrix.vector.Vector;

import difference.FTCS;
import difference.BTCS;

public class Main1 {

	public static void main(String[] args) {
		//f(x)
		Function f = new Function() {
			public double value(double x) {
				if (x < 0.3) {
					return 0;
				} else if (x <= 0.7) {
					return 1;
				} else if (x < 1.0) {
					return -(10 / 3) * x + (10 / 3);
				} else {
					return 0;
				}
			}
		};

		//a(t)
		Function a = new Function() {
			public double value(double x) {
				return f.value(0);
			}
		};

		//b(t)
		Function b = new Function() {
			public double value(double x) {
				return f.value(1);
			}
		};

		double nu = 1;
		int M = 100;
		double sigma = 0.1;
		
		Interval Ix = new Interval(0, 1);		
		Interval It = new Interval(0, 1);
		
		double delta_x = Ix.getLength() / M;
		double delta_t = sigma * delta_x * delta_x / nu;
		int N = (int) (It.getLength() / delta_t);

		FTCS ftcs = new FTCS(N, M, f, a, b, Ix, It) {
			@Override
			protected double next(int i, int j) {
				double delta_x = getDeltaX();
				double delta_t = getDeltaT();
				return values.get(i - 1, j) + 
					nu * (values.get(i - 1, j + 1) - 2 * values.get(i - 1, j) + values.get(i - 1, j - 1)) *
					(delta_t / (delta_x * delta_x));
			}
		};
		ftcs.solve();

		BTCS btcs = new BTCS(N, M, f, a, b, Ix, It) {
			@Override
			protected double[] next(int i) {
				return new double[1];
			}
		};
		btcs.solve();

	}
}
