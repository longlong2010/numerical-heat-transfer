package matrix;

import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Matrix {

	protected double[][] elements;

	public void set(int i, int j, double v) {
		this.elements[i][j] = v;
	}

	public double get(int i, int j) {
		return this.elements[i][j];
	}

	public int getColumnSize() {
		return this.elements.length > 0 ? this.elements[0].length : 0;
	}

	public int getRowSize() {
		return this.elements.length;
	}

	public void print() {
		int row = this.getRowSize();
		int col = this.getColumnSize();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.printf("%g ", this.get(i, j));
			}
			System.out.println();
		}
		System.out.println();
	}

	public static Matrix loadMatrix(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			String line;
			try {
				ArrayList<double[]> list = new ArrayList<double[]>();
				int col = 0;
				while ((line = reader.readLine()) != null) {
					StringTokenizer tokenizer = new StringTokenizer(line);	
					int count = tokenizer.countTokens();
					if (count > col) {
						col = count;
					}
					double[] vector = new double[count];
					for (int i = 0; tokenizer.hasMoreTokens(); i++) {
						String token = tokenizer.nextToken();	
						double val = Double.valueOf(token).doubleValue();
						vector[i] = val;
					}
					list.add(vector);
				}
				int row = list.size();
				Matrix m = new Matrix(row, col);
				for (int i = 0; i < row; i++) {
					double[] vector = list.get(i);
					for (int j = 0; j < vector.length; j++) {
						m.set(i, j, vector[j]);
					}
				}
				reader.close();
				return m;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new Matrix();
	}
	
	public Matrix() {
		this.elements = new double[0][];	
	}

	public Matrix(int m, int n) {
		this.elements = new double[m][];
		for (int i = 0; i < this.elements.length; i++) {
			this.elements[i] = new double[n];
		}
	}

	public static void main(String[] args) {
		Matrix.loadMatrix(args[0]).print();
	}
}
