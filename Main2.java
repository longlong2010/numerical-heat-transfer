import function.Function;

import util.Interval;

import matrix.Matrix;

import difference.ExplicitScheme;;

import java.io.PrintWriter;
import java.io.File;

public class Main2 {

	public static void main(String[] args) {
		Function u0 = new Function() {
			@Override
			public double value(double x) {
				if (x <= -0.5) {
					return 0;
				} else if (x <= 0.5) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		Function u1 = new Function() {
			@Override
			public double value(double x) {
				return 0;
			}
		};

		Function u2 = new Function() {
			@Override
			public double value(double x) {
				return 0;
			}
		};

		double[] param = {0.2, 2.0, 10.0};	
		double c = 0.1;
		double a = 1;
		int m = 300;

		Interval Ix = new Interval(-1.5, 1.5);
		double delta_x = Ix.getLength() / m;
		double delta_t = c * delta_x / a;

		Matrix data;
		int row, col;
		PrintWriter writer;

		//Lax-Friedrichs
		for (int k = 0; k < param.length; k++) {
			Interval It = new Interval(0, param[k]);
			int n = (int) (param[k] / delta_t);
		
			ExplicitScheme lf = new ExplicitScheme(n, m, u0, u1, u2, Ix, It) {
				@Override
				protected double next(int i, int j) {
					return -c * (values.get(i - 1, j + 1) - values.get(i - 1, j - 1)) / 2 
					+ (values.get(i - 1, j + 1) + values.get(i - 1, j - 1)) / 2;
				}
			};
			
			lf.solve();

			data = lf.getValues();
			row = data.getRowSize();
			col = data.getColumnSize();

			try {
				writer = new PrintWriter(new File("lf" + String.valueOf(k)));
				for (int i = 0; i < col; i++) {
					writer.println(data.get(row - 1, i));
				}
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		//Lax-Wendroff
		for (int k = 0; k < param.length; k++) {
			Interval It = new Interval(0, param[k]);
			int n = (int) (param[k] / delta_t);
			
			ExplicitScheme lw = new ExplicitScheme(n, m, u0, u1, u2, Ix, It) {
				@Override
				protected double next(int i, int j) {
					return values.get(i - 1, j) - c * (values.get(i - 1, j + 1) - values.get(i - 1, j - 1)) / 2 + 
							c * c * (values.get(i - 1, j + 1) - 2 * values.get(i - 1, j) + values.get(i - 1, j - 1)) / 2;
				}
			};
			lw.solve();

			data = lw.getValues();
			row = data.getRowSize();
			col = data.getColumnSize();

			try {
				writer = new PrintWriter(new File("lw" + String.valueOf(k)));
				for (int i = 0; i < col; i++) {
					writer.println(data.get(row - 1, i));
				}
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		//Warming-Beam
		for (int k = 0; k < param.length; k++) {
			Interval It = new Interval(0, param[k]);
			int n = (int) (param[k] / delta_t);
			ExplicitScheme wb = new ExplicitScheme(n, m, u0, u1, u2, Ix, It) {
				@Override
				protected double next(int i, int j) {
					double v;
					if (c > 0) {
						if (j <= 1) {
							v = values.get(i - 1, j) - c * (values.get(i - 1, j + 1) - values.get(i - 1, j - 1)) / 2;
						} else {
							v = values.get(i - 1 , j) - 
								c * (3 * values.get(i - 1, j) 
										- 4 * values.get(i - 1, j - 1) 
										+ values.get(i - 1, j - 2)) / 2 + 
								c * c * (values.get(i - 1, j - 2) - 
										2 * values.get(i - 1, j - 1) 
										+ values.get(i - 1, j)) / 2;
						}
					} else {
						int col = values.getColumnSize();
						if (j >= col - 2) {
							v = values.get(i - 1, j) - c * (values.get(i - 1, j + 1) - values.get(i - 1, j - 1)) / 2;
						} else {
							v = values.get(i - 1 , j) + 
								c * (3 * values.get(i - 1, j) 
										- 4 * values.get(i - 1, j + 1) 
										+ values.get(i - 1, j + 2)) / 2 + 
								c * c * (values.get(i - 1, j + 2) - 
										2 * values.get(i - 1, j + 1) 
										+ values.get(i - 1, j)) / 2;
						}
					}
					return v;
				}
			};
			wb.solve();

			data = wb.getValues();
			row = data.getRowSize();
			col = data.getColumnSize();

			try {
				writer = new PrintWriter(new File("wb" + String.valueOf(k)));
				for (int i = 0; i < col; i++) {
					writer.println(data.get(row - 1, i));
				}
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
