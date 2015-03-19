package difference;

import function.Function;
import matrix.Matrix;
import util.Interval;
import util.Grids;

public abstract class FiniteDifference {
	
	protected Function f;
	protected Function a;
	protected Function b;

	protected Matrix values;
	protected Grids grids;

	protected Interval Ix;
	protected Interval It;

	abstract public void solve();

	public FiniteDifference() {
	}

	public FiniteDifference(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		values = new Matrix(m, n);
		grids = new Grids(m, n);

		this.f = f;
		this.a = a;
		this.b = b;

		this.Ix = Ix;
		this.It = It;

		double x0 = Ix.getLeft();
		double delta_x = (Ix.getRight() - x0) / n;
		for (int i = 0; i < n; i++) {
			double x = x0 + i * delta_x;
			values.set(0, i, f.value(x));
			grids.setX(i, x);
		}

		double t0 = It.getLeft();
		double delta_t = (It.getRight() - t0) / m;
		for (int i = 0; i < m; i++) {
			double t = t0 + i * delta_t;
			values.set(i, 0, a.value(t));
			values.set(i, n - 1, b.value(t));
			grids.setT(i, t);
		}
	}

	public double getDeltaX() {
		return Ix.getLength() / values.getColumnSize();
	}

	public double getDeltaT() {
		return It.getLength() / values.getRowSize();
	}
}
