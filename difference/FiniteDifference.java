import function.Function;
import matrix.Matrix;
import util.Interval;

public abstract class FiniteDifference {
	
	protected Function f;
	protected Function a;
	protected Function b;

	protected Matrix grids;

	protected Interval Ix;
	protected Interval It;

	abstract public void next();

	public FiniteDifference() {
	}

	public FiniteDifference(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		this.grids = new Matrix(m, n);
		
		this.f = f;
		this.a = a;
		this.b = b;

		this.Ix = Ix;
		this.It = It;

		double x0 = Ix.getLeft();
		double delta_x = (Ix.getRight() - x0) / n;
		for (int i = 0; i < n; i++) {
			grids.set(0, i, f.value(x0 + i * delta_x));
		}

		double t0 = It.getLeft();
		double delta_t = (It.getRight() - t0) / m;
		for (int i = 0; i < m; i++) {
			grids.set(i, 0, a.value(i * delta_t));
			grids.set(i, n - 1, b.value(t0 + i * delta_t));
		}
	}
}
