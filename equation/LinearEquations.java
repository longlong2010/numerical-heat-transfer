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
