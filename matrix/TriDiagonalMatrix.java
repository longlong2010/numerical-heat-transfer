package matrix;

public class TriDiagonalMatrix extends Matrix {

	public void set(int i, int j, double v) {
		int k = i - j;
		if (Math.abs(k) <= 1) {
			this.elements[i][k + 1] = v;
		}
	}

	public double get(int i, int j) {
		int k = i - j;
		return Math.abs(k) > 1 ? 0 : this.elements[i][k + 1];
	}

	public TriDiagonalMatrix(int n) {
		this.elements = new double[n][];
		for (int i = 0; i < n; i++) {
			this.elements[i] = new double[3];
		}
	}
	
	@Override
	public int getColumnSize() {
		return this.getRowSize();
	}

	public static void main(String[] args) {
		TriDiagonalMatrix m = new TriDiagonalMatrix(3);
		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.print();
	}
}
