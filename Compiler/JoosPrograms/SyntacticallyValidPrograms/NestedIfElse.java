public class NestedIfElse {
	public NestedIfElse() {}
	public int m(int x) {
		if (false)
			if (false)
				return 0;
			else return 1;
		return 2;
	}
}
