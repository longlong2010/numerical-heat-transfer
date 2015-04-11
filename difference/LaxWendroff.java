package difference;

import function.Function;
import util.Interval;
public abstract class LaxWendroff extends FTCS {

	public LaxWendroff(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}
}
