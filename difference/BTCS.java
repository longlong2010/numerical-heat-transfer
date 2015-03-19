package difference;

import function.Function;
import matrix.Matrix;
import util.Interval;

public abstract class BTCS extends FiniteDifference {
	
	public BTCS(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}

	protected abstract double[] next(int i);

	@Override
	public void solve() {
		int step = this.values.getRowSize();
		int m = this.values.getColumnSize();
		for (int i = 1; i < step; i++) {
			double[] v = next(i);
			for (int j = 1; j < m - 1; j++) {
				this.values.set(i, j, v[j - 1]);
			}
		}
	}
}
