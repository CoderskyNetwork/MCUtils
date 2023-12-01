package net.codersky.mcutils.java.tuple;

public abstract class Pair<F, S> {

	public abstract F getFirst();
	public abstract S getSecond();

	public boolean isFirstEqual(MutablePair<F, ?> other) {
		return getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
	}

	public boolean isSecondEqual(MutablePair<?, S> other) {
		return getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
	}

	@Override
	public String toString() {
		return "Pair[" + getFirst() + ", " + getSecond() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pair))
			return false;
		final Pair<?, ?> other = (Pair<?, ?>)obj;
		boolean first = getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
		boolean second = getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
		return first && second;
	}
}
