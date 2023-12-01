package net.codersky.mcutils.java.tuple;

public class MutableTriple<F, S, T> extends Triple<F, S, T> {

	private F first;
	private S second;
	private T third;

	public MutableTriple(F first, S second, T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public F getFirst() {
		return first;
	}

	public F setFirst(F first) {
		return (this.first = first);
	}

	public S getSecond() {
		return second;
	}

	public S setSecond(S second) {
		return (this.second = second);
	}
	
	public T getThird() {
		return third;
	}
	
	public T setThird(T third) {
		return (this.third = third);
	}
}