import function.Function;
import matrix.Matrix;

public abstract class FiniteDifference {
	
	protected Function f;
	protected Function a;
	protected Function b;

	protected Matrix grids;

	abstract public void next();

	public FiniteDifference() {
	}

	public FiniteDifference(int m, int n, Function f, Function a, Function b) {
		this.grids = new Matrix(m, n);
		this.f = f;
		this.a = a;
		this.b = b;

		for (int i = 0; i < m; i++) {
		}
	}
}
