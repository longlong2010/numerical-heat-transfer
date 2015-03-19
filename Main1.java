import function.Function;
import util.Interval;
import difference.FTCS;

public class Main1 {

	public static void main(String[] args) {

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

		Function a = new Function() {
			public double value(double x) {
				return f.value(0);
			}
		};

		Function b = new Function() {
			public double value(double x) {
				return f.value(1);
			}
		};

		double l = 0;
		double r = 1;
		double nu = 1;
		int M = 100;
		double sigma = 0.1;
		
		Interval Ix = new Interval(0, 1);		
		Interval It = new Interval(0, 1);
		
		double delta_x = (Ix.getRight() - Ix.getLeft()) / M;
		double delta_t = sigma * delta_x * delta_x / nu;
		int N = (int) (It.getRight() / delta_t);

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
	}
}
