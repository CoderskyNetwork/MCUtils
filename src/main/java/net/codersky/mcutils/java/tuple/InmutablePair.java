package net.codersky.mcutils.java.tuple;

public class InmutablePair<F, S> extends Pair<F, S> {

	private final F first;
	private final S second;

	public InmutablePair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}
}
