package com.manydesigns.elements.reflection;

import org.apache.commons.lang.ArrayUtils;

public class GroovyClassAccessor extends JavaClassAccessor {

    public final static String[] PROPERTY_NAME_BLACKLIST = {"metaClass"};

    public GroovyClassAccessor(Class javaClass) {
        super(javaClass); //Don't cache Groovy classes as they can be reloaded
    }

    @Override
    protected boolean isValidProperty(PropertyAccessor propertyAccessor) {
        // blacklisted?
        if (ArrayUtils.contains(PROPERTY_NAME_BLACKLIST, propertyAccessor.getName())) {
            return false;
        }
        return super.isValidProperty(propertyAccessor);
    }
}