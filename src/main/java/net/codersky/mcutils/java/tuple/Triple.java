package net.codersky.mcutils.java.tuple;

public abstract class Triple<F, S, T> {

	public abstract F getFirst();
	public abstract S getSecond();
	public abstract T getThird();

	public boolean isFirstEqual(MutableTriple<F, ?, ?> other) {
		return getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
	}

	public boolean isSecondEqual(MutableTriple<?, S, ?> other) {
		return getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
	}

	public boolean isThirdEqual(MutableTriple<?, ?, T> other) {
		return getThird() == null ? other.getSecond() == null : getThird().equals(other.getThird());
	}

	@Override
	public String toString() {
		return "Triple[" + getFirst().toString() + ", " + getSecond().toString() + ", " + getThird().toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Triple))
			return false;
		final Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;
		boolean first = getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
		boolean second = getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
		boolean third = getThird() == null ? other.getThird() == null : getThird().equals(other.getThird());
		return first && second && third;
	}
}
