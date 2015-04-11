import function.Function;

import util.Interval;

import matrix.Matrix;

import difference.LaxWendroff;
import difference.LaxFriedrichs;
import difference.WarmingBeam;

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
		
			LaxFriedrichs lf = new LaxFriedrichs(n, m, u0, u1, u2, Ix, It) {
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
			
			LaxWendroff lw = new LaxWendroff(n, m, u0, u1, u2, Ix, It) {
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
			WarmingBeam wb = new WarmingBeam(n, m, u0, u1, u2, Ix, It) {
				@Override
				protected double next(int i, int j) {
					return 0;
				}
			};
		}
	}
}
