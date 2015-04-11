package difference;

import function.Function;
import util.Interval;
public abstract class LaxFriedrichs extends FTCS {

	public LaxFriedrichs(int m, int n, Function f, Function a, Function b, Interval Ix, Interval It) {
		super(m, n, f, a, b, Ix, It);
	}
}
