package com.manydesigns.portofino.util;

public class Pair<T> {
	public T left;
	public T right;

	public Pair(T left, T right) {
		this.left = left;
		this.right = right;
	}

	public Pair() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Pair that = (Pair) o;

		if (left != null ? !left.equals(that.left) : that.left != null)
			return false;
		if (right != null ? !right.equals(that.right) : that.right != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = left != null ? left.hashCode() : 0;
		result = 31 * result + (right != null ? right.hashCode() : 0);
		return result;
	}
}