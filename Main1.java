import function.Function;

import util.Interval;
import util.Grids;

import matrix.Matrix;
import matrix.vector.Vector;
import matrix.TriDiagonalMatrix;

import difference.ExplicitScheme;
import difference.ImplicitScheme;

import java.io.PrintWriter;
import java.io.File;

public class Main1 {

	public static void main(String[] args) {
		//f(x)
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

		//a(t)
		Function a = new Function() {
			public double value(double x) {
				return f.value(0);
			}
		};

		//b(t)
		Function b = new Function() {
			public double value(double x) {
				return f.value(1);
			}
		};

		double nu = 1;

		//grids param
		int M = 100;

		double[] params1 = {0.1, 0.5, 1};
		double[] params2 = {0.01, 0.1, 1, 10.0};

		Matrix data;
		Grids grids;
		int row, col;

		PrintWriter writer;
		
		for (int i = 0; i < params1.length; i++) {
			for (int j = 0; j < params2.length; j++) {

				double sigma = params1[i];

				Interval Ix = new Interval(0, 1);		
				Interval It = new Interval(0, params2[j]);

				// x step, t step
				double delta_x = Ix.getLength() / M;
				double delta_t = sigma * delta_x * delta_x / nu;
				int N = (int) (It.getLength() / delta_t);

				//FTCS
				ExplicitScheme ftcs = new ExplicitScheme(N, M, f, a, b, Ix, It) {
					@Override
						protected double next(int i, int j) {
							double delta_x = getDeltaX();
							double delta_t = getDeltaT();
							double sigma = nu * delta_t / (delta_x * delta_x);

							return values.get(i - 1, j) + 
								sigma * (values.get(i - 1, j + 1) - 2 * values.get(i - 1, j) + values.get(i - 1, j - 1));
						}
				};
				ftcs.solve();
				data = ftcs.getValues();
				grids = ftcs.getGrids();
				row = data.getRowSize();
				col = data.getColumnSize();

				try {	
					writer = new PrintWriter(new File("ftcs" + String.valueOf(i) + "-" + String.valueOf(j)));
					for (int k = 0; k < col; k++) {
						writer.println(data.get(row - 1, k));
					}
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < params1.length; i++) {
			for (int j = 0; j < params2.length; j++) {

				double sigma = params1[i];

				Interval Ix = new Interval(0, 1);		
				Interval It = new Interval(0, params2[j]);

				// x step, t step
				double delta_x = Ix.getLength() / M;
				double delta_t = sigma * delta_x * delta_x / nu;
				int N = (int) (It.getLength() / delta_t);
				//
				ImplicitScheme btcs = new ImplicitScheme(N, M, f, a, b, Ix, It) {
					@Override
						protected void next(TriDiagonalMatrix m, Vector b, int i, int j) {
							double delta_x = getDeltaX();
							double delta_t = getDeltaT();
							double sigma = nu * delta_t / (delta_x * delta_x);

							m.set(j, j - 1, sigma);
							m.set(j, j, -(1 + 2 * sigma));
							m.set(j, j + 1, sigma);
							b.set(j, -values.get(i - 1, j));
						}
				};
				btcs.solve();
				data = btcs.getValues();
				grids = btcs.getGrids();
				row = data.getRowSize();
				col = data.getColumnSize();
				
				try {	
					writer = new PrintWriter(new File("btcs" + String.valueOf(i) + "-" + String.valueOf(j)));
					for (int k = 0; k < col; k++) {
						writer.println(data.get(row - 1, k));
					}
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		for (int i = 0; i < params1.length; i++) {
			for (int j = 0; j < params2.length; j++) {

				double sigma = params1[i];

				Interval Ix = new Interval(0, 1);		
				Interval It = new Interval(0, params2[j]);

				// x step, t step
				double delta_x = Ix.getLength() / M;
				double delta_t = sigma * delta_x * delta_x / nu;
				int N = (int) (It.getLength() / delta_t);
				//CNCS
				ImplicitScheme cncs = new ImplicitScheme(N, M, f, a, b, Ix, It) {
					@Override
						protected void next(TriDiagonalMatrix m, Vector b, int i, int j) {
							double delta_x = getDeltaX();
							double delta_t = getDeltaT();
							double sigma = nu * delta_t / (delta_x * delta_x);

							m.set(j, j - 1, sigma / 2);
							m.set(j, j, -(1 + sigma));
							m.set(j, j + 1, sigma / 2);

							b.set(j, -values.get(i - 1, j) 
									-sigma * (values.get(i - 1, j + 1) - 2 * values.get(i - 1, j) + values.get(i - 1, j - 1)) / 2);
						}
				};
				cncs.solve();
				data = cncs.getValues();
				grids = cncs.getGrids();
				row = data.getRowSize();
				col = data.getColumnSize();
				
				try {	
					writer = new PrintWriter(new File("cncs" + String.valueOf(i) + "-" + String.valueOf(j)));
					for (int k = 0; k < col; k++) {
						writer.println(data.get(row - 1, k));
					}
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
