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
		double c = 0.5;
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
					return formula(values.get(i - 1, j - 1), values.get(i - 1, j), values.get(i - 1, j + 1));
				}

				private double formula(double k1, double k2, double k3) {
					return -c * (k3 - k1) / 2 + (k3 + k1) / 2;
				}

				@Override
				public void solve() {
					int step = this.values.getRowSize();
					int m = this.values.getColumnSize();
					for (int i = 1; i < step; i++) {
						double v0 = formula(values.get(i - 1, m - 2), values.get(i - 1, 0), values.get(i - 1, 1));
						this.values.set(i, 0, v0);
						for (int j = 1; j < m - 1; j++) {
							double v = next(i, j);
							this.values.set(i, j, v);
						}
						this.values.set(i, m - 1, v0);
					}
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
					return formula(values.get(i - 1, j - 1), values.get(i - 1, j), values.get(i - 1, j + 1));
				}
				
				private double formula(double k1, double k2, double k3) {
					return k2 - c * (k3 - k1) / 2 + c * c * (k3 - 2 * k2 + k3) / 2;
				}

				@Override
				public void solve() {
					int step = this.values.getRowSize();
					int m = this.values.getColumnSize();
					for (int i = 1; i < step; i++) {
						double v0 = formula(values.get(i - 1, m - 2), values.get(i - 1, 0), values.get(i - 1, 1));
						this.values.set(i, 0, v0);
						for (int j = 1; j < m - 1; j++) {
							double v = next(i, j);
							this.values.set(i, j, v);
						}
						this.values.set(i, m - 1, v0);
					}
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
					int m = this.values.getColumnSize();
					double k1 = values.get(i - 1, j);
					
					int j1 = j - 1 < 0 ? m - 1 + j - 1 : j - 1;
					int j2 = j + 1 > m - 1 ? j + 1 - (m - 1) : j + 1;
					double k2 = c > 0 ? values.get(i - 1, j1) : values.get(i - 1, j2);

					int j3 = j - 2 < 0 ? m - 1 + j - 2 : j - 2;
					int j4 = j + 2 > m - 1 ? j + 2 - (m - 1) : j + 2;
					double k3 = c > 0 ? values.get(i - 1, j3) : values.get(i - 1, j4);
					
					return formula(k1, k2, k3);
				}

				private double formula(double k1, double k2, double k3) {
					return k1 - Math.signum(c) * c * (3 * k1 - 4 * k2 + k3) / 2 + c * c * (k3 - 2 * k2 + k1) / 2;
				}

				@Override
				public void solve() {
					int step = this.values.getRowSize();
					int m = this.values.getColumnSize();
					for (int i = 1; i < step; i++) {
						double v0 = c > 0 ? formula(values.get(i - 1, 0), values.get(i - 1, m - 2), values.get(i - 1, m - 3)) 
										  : formula(values.get(i - 1, 0), values.get(i - 1, 1), values.get(i - 1, 2));
						this.values.set(i, 0, v0);
						for (int j = 1; j < m - 1; j++) {
							double v = next(i, j);
							this.values.set(i, j, v);
						}
						this.values.set(i, m - 1, v0);
					}
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
