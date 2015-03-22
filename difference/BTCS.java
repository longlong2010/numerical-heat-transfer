package difference;

import function.Function;

import matrix.Matrix;
import matrix.TriDiagonalMatrix;
import matrix.vector.Vector;

import equation.LinearEquations;

import util.Interval;

public abstract class BTCS extends FiniteDifference {
	
	public BTCS(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}

	protected abstract void next(TriDiagonalMatrix m, Vector b, int i, int j);

	@Override
	public void solve() {
		int step = this.values.getRowSize();
		int size = this.values.getColumnSize();
		double delta_t = getDeltaT();

		for (int i = 1; i < step; i++) {
			TriDiagonalMatrix m = new TriDiagonalMatrix(size);
			Vector b = new Vector(size);

			double ti = It.getLeft() + i * delta_t;
			m.set(0, 0, 1);
			b.set(0, this.a.value(ti));
			
			for (int j = 1; j < size - 1; j++) {
				next(m, b, i, j);
			}

			m.set(size - 1, size - 1, 1);
			b.set(0, this.b.value(ti));

			Vector v = LinearEquations.solve(m, b);
			
			for (int j = 1; j < size - 1; j++) {
				this.values.set(i, j, v.get(j));
			}
		}
	}
}
