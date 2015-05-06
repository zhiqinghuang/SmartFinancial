package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.reflection.PropertyAccessor;

public interface Criteria {
	Criteria eq(PropertyAccessor accessor, Object value);

	Criteria in(PropertyAccessor accessor, Object[] value);

	Criteria ne(PropertyAccessor accessor, Object value);

	Criteria between(PropertyAccessor accessor, Object min, Object max);

	Criteria gt(PropertyAccessor accessor, Object value);

	Criteria ge(PropertyAccessor accessor, Object value);

	Criteria lt(PropertyAccessor accessor, Object value);

	Criteria le(PropertyAccessor accessor, Object value);

	Criteria like(PropertyAccessor accessor, String value, TextMatchMode textMatchMode);

	Criteria ilike(PropertyAccessor accessor, String value, TextMatchMode textMatchMode);

	Criteria isNull(PropertyAccessor accessor);

	Criteria isNotNull(PropertyAccessor accessor);

	Criteria orderBy(PropertyAccessor accessor, String direction);

	OrderBy getOrderBy();

	static class OrderBy {

		protected final PropertyAccessor propertyAccessor;
		protected final String direction;

		public static final String ASC = "asc";
		public static final String DESC = "desc";

		public OrderBy(PropertyAccessor propertyAccessor, String direction) {
			this.propertyAccessor = propertyAccessor;
			this.direction = direction;
		}

		public PropertyAccessor getPropertyAccessor() {
			return propertyAccessor;
		}

		public String getDirection() {
			return direction;
		}

		public boolean isAsc() {
			return ASC.equals(direction);
		}

		public boolean isDesc() {
			return DESC.equals(direction);
		}
	}
}