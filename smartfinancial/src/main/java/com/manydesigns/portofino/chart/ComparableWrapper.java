package com.manydesigns.portofino.chart;

public class ComparableWrapper implements Comparable<ComparableWrapper> {

	private final Comparable object;
	private String label;

	public ComparableWrapper(Comparable object) {
		this(object, object.toString());
	}

	public ComparableWrapper(Comparable object, String label) {
		this.object = object;
		this.label = label;
	}

	public int compareTo(ComparableWrapper myComparable) {
		return object.compareTo(myComparable.object);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Comparable getObject() {
		return object;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ComparableWrapper that = (ComparableWrapper) o;

		if (object != null ? !object.equals(that.object) : that.object != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return object != null ? object.hashCode() : 0;
	}
}