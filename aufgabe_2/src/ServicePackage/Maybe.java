package ServicePackage;

public interface Maybe<A> {
	public boolean isJust();
	public A get();
}
