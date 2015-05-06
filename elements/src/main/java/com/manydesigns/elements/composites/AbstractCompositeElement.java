package com.manydesigns.elements.composites;

import com.manydesigns.elements.Element;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.reflection.PropertyAccessor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCompositeElement<T extends Element> extends ArrayList<T> implements Element {

	private static final long serialVersionUID = -7649717045080410052L;
	protected String id;

	public AbstractCompositeElement(int initialCapacity) {
		super(initialCapacity);
	}

	public AbstractCompositeElement() {
	}

	public AbstractCompositeElement(Collection<? extends T> c) {
		super(c);
	}

	public void readFromRequest(HttpServletRequest req) {
		for (T current : this) {
			current.readFromRequest(req);
		}
	}

	public boolean validate() {
		boolean result = true;
		for (T current : this) {
			result = current.validate() && result;
		}
		return result;
	}

	public void readFromObject(Object obj) {
		for (T current : this) {
			current.readFromObject(obj);
		}
	}

	public void writeToObject(Object obj) {
		for (T current : this) {
			current.writeToObject(obj);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Field findFieldByPropertyName(String propertyName) {
		for (T element : this) {
			if (element instanceof Field) {
				Field field = (Field) element;
				PropertyAccessor accessor = field.getPropertyAccessor();
				if (accessor.getName().equals(propertyName)) {
					return field;
				}
			} else if (element instanceof AbstractCompositeElement) {
				Field field = ((AbstractCompositeElement) element).findFieldByPropertyName(propertyName);
				if (field != null) {
					return field;
				}
			}
		}
		return null;
	}

	public Collection<Field> fields() {
		List<Field> fields = new ArrayList<Field>();
		for (T element : this) {
			if (element instanceof Field) {
				Field field = (Field) element;
				fields.add(field);
			} else if (element instanceof AbstractCompositeElement) {
				fields.addAll(((AbstractCompositeElement) element).fields());
			}
		}
		return fields;
	}
}