package ServicePackage;

public class Nothing<A> implements Maybe<A> {
	
	public Nothing(){}
	
	@Override
	public boolean isJust() {
		return false;
	}
	public A get() {
		throw new RuntimeException("Nothing.get");
	}
}
