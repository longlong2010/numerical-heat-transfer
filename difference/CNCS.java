package difference;

import function.Function;
import matrix.Matrix;
import util.Interval;

public abstract class CNCS extends FiniteDifference {

	public CNCS(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}
}
