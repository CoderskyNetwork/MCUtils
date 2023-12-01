package net.codersky.mcutils.java.tuple;

public class MutablePair<F, S> extends Pair<F, S> {

	private F first;
	private S second;

	public MutablePair(F first, S second) {
		this.first = first;
		this.second = second;
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
}