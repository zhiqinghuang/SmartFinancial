package com.manydesigns.portofino.pageactions.crud.reflection;

import com.manydesigns.elements.annotations.*;
import com.manydesigns.elements.annotations.impl.*;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.portofino.pageactions.crud.configuration.CrudProperty;
import com.manydesigns.portofino.reflection.AbstractAnnotatedAccessor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

public class CrudPropertyAccessor extends AbstractAnnotatedAccessor implements PropertyAccessor {
	protected final CrudProperty crudProperty;
	protected final PropertyAccessor nestedAccessor;

	public static final Logger logger = LoggerFactory.getLogger(CrudPropertyAccessor.class);

	public CrudPropertyAccessor(@Nullable CrudProperty crudProperty, PropertyAccessor nestedAccessor, boolean inKey) {
		super(crudProperty != null ? crudProperty.getAnnotations() : null);
		this.crudProperty = crudProperty;
		this.nestedAccessor = nestedAccessor;

		Enabled enabledAnn = nestedAccessor.getAnnotation(Enabled.class);
		boolean accessorEnabled = (enabledAnn == null || enabledAnn.value());

		Insertable insertableAnn = nestedAccessor.getAnnotation(Insertable.class);
		boolean accessorInsertable = (insertableAnn == null || insertableAnn.value());

		Updatable updatableAnn = nestedAccessor.getAnnotation(Updatable.class);
		boolean accessorUpdatable = (updatableAnn == null || updatableAnn.value());

		InSummary inSummaryAnn = nestedAccessor.getAnnotation(InSummary.class);
		boolean accessorInSummary = (inSummaryAnn == null || inSummaryAnn.value());

		Searchable searchableAnn = nestedAccessor.getAnnotation(Searchable.class);
		boolean accessorSearchable = (searchableAnn == null || searchableAnn.value());

		boolean crudEnabled;
		boolean crudInsertable;
		boolean crudUpdatable;
		boolean crudInSummary;
		boolean crudSearchable;

		if (crudProperty != null) {
			String label = crudProperty.getLabel();
			if (StringUtils.isNotEmpty(label)) {
				annotations.put(Label.class, new LabelImpl(label));
			}
			crudEnabled = crudProperty.isEnabled();
			crudSearchable = crudProperty.isSearchable();
			crudInSummary = crudProperty.isInSummary();
			crudInsertable = crudProperty.isInsertable();
			crudUpdatable = crudProperty.isUpdatable();
		} else {
			crudEnabled = inKey;
			crudSearchable = false;
			crudInSummary = inKey;
			crudInsertable = false;
			crudUpdatable = false;
		}

		annotations.put(Searchable.class, new SearchableImpl(crudSearchable && accessorSearchable));
		annotations.put(InSummary.class, new InSummaryImpl(crudInSummary && accessorInSummary));
		annotations.put(Enabled.class, new EnabledImpl(crudEnabled && accessorEnabled));
		annotations.put(Insertable.class, new InsertableImpl(crudInsertable && accessorInsertable));
		annotations.put(Updatable.class, new UpdatableImpl(crudUpdatable && accessorUpdatable));
	}

	public String getName() {
		return nestedAccessor.getName();
	}

	public Class getType() {
		return nestedAccessor.getType();
	}

	public int getModifiers() {
		return nestedAccessor.getModifiers();
	}

	@SuppressWarnings({ "unchecked" })
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		T annotation = super.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		return nestedAccessor.getAnnotation(annotationClass);
	}

	public Object get(Object obj) {
		return nestedAccessor.get(obj);
	}

	public void set(Object obj, Object value) {
		nestedAccessor.set(obj, value);
	}

	public CrudProperty getCrudProperty() {
		return crudProperty;
	}

	@Override
	public String toString() {
		return crudProperty.getName() + " (" + nestedAccessor.getName() + ")";
	}
}