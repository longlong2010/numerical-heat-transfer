package difference;

import function.Function;

import util.Interval;

public abstract class CNCS extends BTCS {
	public CNCS(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}
}
