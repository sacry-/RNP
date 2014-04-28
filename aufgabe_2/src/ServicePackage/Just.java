package ServicePackage;

public class Just<A> implements Maybe<A> {
	A a;
	
	public Just(A a) {
		this.a=a;
	}
	
	@Override
	public boolean isJust() {
		return true;
	}

	@Override
	public A get() {
		return a;
	}

}
