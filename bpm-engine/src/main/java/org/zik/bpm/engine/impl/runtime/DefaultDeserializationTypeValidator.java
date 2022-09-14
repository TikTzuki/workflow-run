// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import org.zik.bpm.engine.runtime.WhitelistingDeserializationTypeValidator;

public class DefaultDeserializationTypeValidator implements WhitelistingDeserializationTypeValidator
{
    protected static final Collection<String> ALLOWED_PACKAGES;
    protected static final Collection<String> ALLOWED_CLASSES;
    protected Set<String> allowedClasses;
    protected Set<String> allowedPackages;
    
    public DefaultDeserializationTypeValidator() {
        this.allowedClasses = new HashSet<String>(DefaultDeserializationTypeValidator.ALLOWED_CLASSES);
        this.allowedPackages = new HashSet<String>(DefaultDeserializationTypeValidator.ALLOWED_PACKAGES);
    }
    
    @Override
    public void setAllowedClasses(final String deserializationAllowedClasses) {
        this.extractElements(deserializationAllowedClasses, this.allowedClasses);
    }
    
    @Override
    public void setAllowedPackages(final String deserializationAllowedPackages) {
        this.extractElements(deserializationAllowedPackages, this.allowedPackages);
    }
    
    @Override
    public boolean validate(final String className) {
        return className == null || className.trim().isEmpty() || this.isPackageAllowed(className) || this.isClassNameAllowed(className);
    }
    
    protected boolean isPackageAllowed(final String className) {
        return this.isPackageAllowed(className, DefaultDeserializationTypeValidator.ALLOWED_PACKAGES) || this.isPackageAllowed(className, this.allowedPackages);
    }
    
    protected boolean isPackageAllowed(final String className, final Collection<String> allowedPackages) {
        for (final String allowedPackage : allowedPackages) {
            if (!allowedPackage.isEmpty() && className.startsWith(allowedPackage)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isClassNameAllowed(final String className) {
        return DefaultDeserializationTypeValidator.ALLOWED_CLASSES.contains(className) || this.allowedClasses.contains(className);
    }
    
    protected void extractElements(final String allowedElements, final Set<String> set) {
        if (!set.isEmpty()) {
            set.clear();
        }
        if (allowedElements == null) {
            return;
        }
        final String allowedElementsSanitized = allowedElements.replaceAll("\\s", "");
        if (allowedElementsSanitized.isEmpty()) {
            return;
        }
        final String[] split;
        final String[] classes = split = allowedElementsSanitized.split(",");
        for (final String className : split) {
            set.add(className.trim());
        }
    }
    
    static {
        ALLOWED_PACKAGES = Arrays.asList("java.lang");
        ALLOWED_CLASSES = Collections.unmodifiableSet((Set<?>)new HashSet<Object>(Arrays.asList("java.util.ArrayList", "java.util.Arrays$ArrayList", "java.util.HashMap", "java.util.HashSet", "java.util.LinkedHashMap", "java.util.LinkedHashSet", "java.util.LinkedList", "java.util.Properties", "java.util.TreeMap", "java.util.TreeSet")));
    }
}
