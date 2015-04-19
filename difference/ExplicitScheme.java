package difference;

import function.Function;
import matrix.Matrix;
import util.Interval;

public abstract class ExplicitScheme extends FiniteDifference {

	public ExplicitScheme(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}

	protected abstract double next(int i, int j);

	@Override
	public void solve() {
		int step = this.values.getRowSize();
		int m = this.values.getColumnSize();
		for (int i = 1; i < step; i++) {
			for (int j = 1; j < m - 1; j++) {
				double v = next(i, j);
				this.values.set(i, j, v);
			}
		}
	}
}
