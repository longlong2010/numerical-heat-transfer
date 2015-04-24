package equation;

import matrix.Matrix;
import matrix.TriDiagonalMatrix;
import matrix.vector.Vector;

public class LinearEquations {

	public static Vector solve(TriDiagonalMatrix m, Vector b) {
		int size = m.getRowSize();
		TriDiagonalMatrix l = new TriDiagonalMatrix(size);
		TriDiagonalMatrix u = new TriDiagonalMatrix(size);
		l.set(0, 0, 1);
		u.set(0, 0, m.get(0, 0));

		for (int i = 1; i < size; i++) {
			l.set(i, i, 1);
			l.set(i, i - 1, m.get(i, i - 1) / u.get(i - 1, i - 1));
			
			u.set(i - 1, i, m.get(i - 1, i));
			u.set(i, i, m.get(i, i) - l.get(i, i - 1) * m.get(i - 1, i));
		}
		
		Vector y = new Vector(size);
		y.set(0, b.get(0));
		for (int i = 1; i < size; i++) {
			y.set(i, b.get(i) - l.get(i, i - 1) * y.get(i - 1));	
		}

		Vector x = new Vector(size);
		x.set(size - 1, y.get(size - 1) / u.get(size - 1, size - 1));
		for (int i = size - 2; i >= 0; i--) {
			x.set(i, (y.get(i) - u.get(i, i + 1) * x.get(i + 1)) / u.get(i, i));
		}

		return x;
	}

	public static Vector jacobiInterationSolve(Matrix m, Vector b, Vector v0, double eps, int max_step) {
		int size = m.getRowSize();
		Vector v1 = v0;
		for (int i = 0; i < max_step; i++) {
			Vector delta = new Vector(size);
			v1 = new Vector(size);
			for (int j = 0; j < size; j++) {
				double s = 0;
				for (int k = 0; k < size; k++) {
					s += m.get(j, k) * v0.get(k);
				}
				delta.set(j, (b.get(j) - s) / m.get(j, j));
				v1.set(j, v0.get(j) + delta.get(j));
			}
			v0 = v1;
			if (delta.length() < eps) {
				break;
			}
		}
		return v1;
	}

	public static Vector gaussSeidelInterationSolve(Matrix m, Vector b, Vector v0, double eps, int max_step) {
		return underRelaxationInterationSolve(m, b, v0, 1, eps, max_step);
	}

	public static Vector underRelaxationInterationSolve(Matrix m, Vector b, Vector v0, double omiga, double eps, int max_step) {
		int size = m.getRowSize();
		Vector v1 = v0;
		for (int i = 0; i < max_step; i++) {
			Vector delta = new Vector(size);
			for (int j = 0; j < size; j++) {
				double s = 0;
				for (int k = 0; k < size; k++) {
					s += m.get(j, k) * v1.get(k);
				}
				delta.set(j, (b.get(j) - s) / m.get(j, j));
				v1.set(j, omiga * (v1.get(j) + delta.get(j)) + (1 - omiga) * v0.get(j));
			}
			v0 = v1;
			if (delta.length() < eps) {
				break;
			}
		}
		return v1;
	}

	public static void main(String[] args) {
		TriDiagonalMatrix m = new TriDiagonalMatrix(5);
		m.set(0, 0, 2);
		m.set(0, 1, -1);
		m.set(1, 0, -1);
		m.set(1, 1, 2);
		m.set(1, 2, -1);
		m.set(2, 1, -1);
		m.set(2, 2, 2);
		m.set(2, 3, -1);
		m.set(3, 2, -1);
		m.set(3, 3, 2);
		m.set(3, 4, -1);
		m.set(4, 3, -1);
		m.set(4, 4, 2);
		
		Vector b = new Vector(5);
		b.set(0, 1);
		b.set(2, 1);

		LinearEquations.solve(m, b).print();
	}
}
