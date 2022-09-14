// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public abstract class ClassNameUtil
{
    protected static final Map<Class<?>, String> cachedNames;
    
    public static String getClassNameWithoutPackage(final Object object) {
        return getClassNameWithoutPackage(object.getClass());
    }
    
    public static String getClassNameWithoutPackage(final Class<?> clazz) {
        String unqualifiedClassName = ClassNameUtil.cachedNames.get(clazz);
        if (unqualifiedClassName == null) {
            final String fullyQualifiedClassName = clazz.getName();
            unqualifiedClassName = fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf(46) + 1);
            ClassNameUtil.cachedNames.put(clazz, unqualifiedClassName);
        }
        return unqualifiedClassName;
    }
    
    static {
        cachedNames = new ConcurrentHashMap<Class<?>, String>();
    }
}
