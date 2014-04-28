package ServicePackage;

public class Nothing implements Maybe<Object> {

	@Override
	public boolean isJust() {
		return false;
	}
	public Object get() {
		throw new RuntimeException("Nothing.get");
	}
}
