import matrix.TriDiagonalMatrix;
import matrix.vector.Vector;
import matrix.Matrix;

import function.Function;

import equation.LinearEquations;

import util.Interval;

public class Main3 {

	public static void main(String[] args) {
		double phi_0 = 0.0;
		double phi_l = 1.0;

		double Pe = 50.0;
		double L = 1.0;
		double[] Peclets = {1.0, 2.0, 5.0, 10.0};
	
		Function analytic = new Function() {
			@Override
			public double value(double x) {
				return phi_0 + 1 / (Math.exp(Pe) - 1) * (phi_l - phi_0) * (Math.exp(Pe * x / L) - 1);
			}
		};
		
		Interval Ix = new Interval(0, L);
		//TDMA
		for (int k = 0; k < Peclets.length; k++) {
			double peclet = Peclets[k];
			double delta_x = peclet / Pe * Ix.getLength();
			int n = (int) Math.ceil(L / delta_x) - 1;
			
			TriDiagonalMatrix m = new TriDiagonalMatrix(n);
			Vector b = new Vector(n);

			m.set(0, 0, 2);
			m.set(0, 1, 0.5 * peclet - 1);

			b.set(0, (0.5 * peclet  + 1) * phi_0);

			for (int i = 1; i < n - 1; i++) {
				m.set(i, i - 1, -0.5 * peclet - 1);
				m.set(i, i, 2);
				m.set(i, i + 1, 0.5 * peclet - 1);

				b.set(i, 0);
			}
			
			m.set(n - 1, n - 2, -0.5 * peclet - 1);
			m.set(n - 1, n - 1, 2);
			
			b.set(n - 1, (-0.5 * peclet + 1) * phi_l);
			
			Vector v = LinearEquations.solve(m, b);
			
			for (int i = 0; i < n; i ++) {
				double x = Ix.getLeft() + (i + 1) * delta_x;
				double y = analytic.value(x);
				System.out.printf("%e\t%e\t%e\n", x, v.get(i), y);
			}
			System.out.println();
		}

		//Jacobi	
		for (int k = 0; k < Peclets.length; k++) {
			double peclet = Peclets[k];
			double delta_x = peclet / Pe * Ix.getLength();
			int n = (int) Math.ceil(L / delta_x) - 1;
			
			Matrix m = new Matrix(n, n);
			Vector b = new Vector(n);

			m.set(0, 0, 2);
			m.set(0, 1, 0.5 * peclet - 1);

			b.set(0, (0.5 * peclet  + 1) * phi_0);

			for (int i = 1; i < n - 1; i++) {
				m.set(i, i - 1, -0.5 * peclet - 1);
				m.set(i, i, 2);
				m.set(i, i + 1, 0.5 * peclet - 1);

				b.set(i, 0);
			}
			
			m.set(n - 1, n - 2, -0.5 * peclet - 1);
			m.set(n - 1, n - 1, 2);
			
			b.set(n - 1, (-0.5 * peclet + 1) * phi_l);
			
			Vector v0 = new Vector(n);
			v0.set(0, 1);
			Vector v = LinearEquations.jacobiInterationSolve(m, b, v0, 1e-15, 10000);

			for (int i = 0; i < n; i++) {
				double x = Ix.getLeft() + (i + 1) * delta_x;
				double y = analytic.value(x);
				System.out.printf("%e\t%e\t%e\n", x, v.get(i), y);
			}
			System.out.println();
		}

		//Gauss-Seidel

		for (int k = 0; k < Peclets.length; k++) {
			double peclet = Peclets[k];
			double delta_x = peclet / Pe * Ix.getLength();
			int n = (int) Math.ceil(L / delta_x) - 1;
			
			Matrix m = new Matrix(n, n);
			Vector b = new Vector(n);

			m.set(0, 0, 2);
			m.set(0, 1, 0.5 * peclet - 1);

			b.set(0, (0.5 * peclet  + 1) * phi_0);

			for (int i = 1; i < n - 1; i++) {
				m.set(i, i - 1, -0.5 * peclet - 1);
				m.set(i, i, 2);
				m.set(i, i + 1, 0.5 * peclet - 1);

				b.set(i, 0);
			}
			
			m.set(n - 1, n - 2, -0.5 * peclet - 1);
			m.set(n - 1, n - 1, 2);
			
			b.set(n - 1, (-0.5 * peclet + 1) * phi_l);
			
			Vector v0 = new Vector(n);
			v0.set(0, 1);
			Vector v = LinearEquations.underRelaxationInterationSolve(m, b, v0, 0.1, 1e-25, 10000);

			for (int i = 0; i < n; i++) {
				double x = Ix.getLeft() + (i + 1) * delta_x;
				double y = analytic.value(x);
				System.out.printf("%e\t%e\t%e\n", x, v.get(i), y);
			}
			System.out.println();
		}
	}
}
