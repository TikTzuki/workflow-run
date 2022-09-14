// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class BeanELResolver extends ELResolver
{
    private final boolean readOnly;
    private final ConcurrentHashMap<Class<?>, BeanProperties> cache;
    private ExpressionFactory defaultFactory;
    
    private static Method findAccessibleMethod(final Method method) {
        if (method == null || method.isAccessible()) {
            return method;
        }
        try {
            method.setAccessible(true);
        }
        catch (SecurityException e) {
            for (final Class<?> cls : method.getDeclaringClass().getInterfaces()) {
                Method mth = null;
                try {
                    mth = cls.getMethod(method.getName(), method.getParameterTypes());
                    mth = findAccessibleMethod(mth);
                    if (mth != null) {
                        return mth;
                    }
                }
                catch (NoSuchMethodException ex) {}
            }
            final Class<?> cls2 = method.getDeclaringClass().getSuperclass();
            if (cls2 != null) {
                Method mth2 = null;
                try {
                    mth2 = cls2.getMethod(method.getName(), method.getParameterTypes());
                    mth2 = findAccessibleMethod(mth2);
                    if (mth2 != null) {
                        return mth2;
                    }
                }
                catch (NoSuchMethodException ex2) {}
            }
            return null;
        }
        return method;
    }
    
    public BeanELResolver() {
        this(false);
    }
    
    public BeanELResolver(final boolean readOnly) {
        this.readOnly = readOnly;
        this.cache = new ConcurrentHashMap<Class<?>, BeanProperties>();
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.isResolvable(base) ? Object.class : null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        if (this.isResolvable(base)) {
            PropertyDescriptor[] properties;
            try {
                properties = Introspector.getBeanInfo(base.getClass()).getPropertyDescriptors();
            }
            catch (IntrospectionException e) {
                return Collections.emptyIterator();
            }
            return new Iterator<FeatureDescriptor>() {
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return properties != null && this.next < properties.length;
                }
                
                @Override
                public FeatureDescriptor next() {
                    final PropertyDescriptor property = properties[this.next++];
                    final FeatureDescriptor feature = new FeatureDescriptor();
                    feature.setDisplayName(property.getDisplayName());
                    feature.setName(property.getName());
                    feature.setShortDescription(property.getShortDescription());
                    feature.setExpert(property.isExpert());
                    feature.setHidden(property.isHidden());
                    feature.setPreferred(property.isPreferred());
                    feature.setValue("type", property.getPropertyType());
                    feature.setValue("resolvableAtDesignTime", true);
                    return feature;
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("cannot remove");
                }
            };
        }
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        Class<?> result = null;
        if (this.isResolvable(base)) {
            result = this.toBeanProperty(base, property).getPropertyType();
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object result = null;
        if (this.isResolvable(base)) {
            final Method method = this.toBeanProperty(base, property).getReadMethod();
            if (method == null) {
                throw new PropertyNotFoundException("Cannot read property " + property);
            }
            try {
                result = method.invoke(base, new Object[0]);
            }
            catch (InvocationTargetException e) {
                throw new ELException(e.getCause());
            }
            catch (Exception e2) {
                throw new ELException(e2);
            }
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = this.readOnly;
        if (this.isResolvable(base)) {
            result |= this.toBeanProperty(base, property).isReadOnly();
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (this.isResolvable(base)) {
            if (this.readOnly) {
                throw new PropertyNotWritableException("resolver is read-only");
            }
            final Method method = this.toBeanProperty(base, property).getWriteMethod();
            if (method == null) {
                throw new PropertyNotWritableException("Cannot write property: " + property);
            }
            try {
                method.invoke(base, value);
            }
            catch (InvocationTargetException e) {
                throw new ELException("Cannot write property: " + property, e.getCause());
            }
            catch (IllegalAccessException e2) {
                throw new PropertyNotWritableException("Cannot write property: " + property, e2);
            }
            context.setPropertyResolved(true);
        }
    }
    
    @Override
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, Object[] params) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object result = null;
        if (this.isResolvable(base)) {
            if (params == null) {
                params = new Object[0];
            }
            final String name = method.toString();
            final Method target = this.findMethod(base, name, paramTypes, params.length);
            if (target == null) {
                throw new MethodNotFoundException("Cannot find method " + name + " with " + params.length + " parameters in " + base.getClass());
            }
            try {
                result = target.invoke(base, this.coerceParams(this.getExpressionFactory(context), target, params));
            }
            catch (InvocationTargetException e) {
                throw new ELException(e.getCause());
            }
            catch (IllegalAccessException e2) {
                throw new ELException(e2);
            }
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    private Method findMethod(final Object base, final String name, final Class<?>[] types, final int paramCount) {
        if (types != null) {
            try {
                return findAccessibleMethod(base.getClass().getMethod(name, types));
            }
            catch (NoSuchMethodException e) {
                return null;
            }
        }
        Method varArgsMethod = null;
        for (final Method method : base.getClass().getMethods()) {
            if (method.getName().equals(name)) {
                final int formalParamCount = method.getParameterTypes().length;
                if (method.isVarArgs() && paramCount >= formalParamCount - 1) {
                    varArgsMethod = method;
                }
                else if (paramCount == formalParamCount) {
                    return findAccessibleMethod(method);
                }
            }
        }
        return (varArgsMethod == null) ? null : findAccessibleMethod(varArgsMethod);
    }
    
    private ExpressionFactory getExpressionFactory(final ELContext context) {
        final Object obj = context.getContext(ExpressionFactory.class);
        if (obj instanceof ExpressionFactory) {
            return (ExpressionFactory)obj;
        }
        if (this.defaultFactory == null) {
            this.defaultFactory = ExpressionFactory.newInstance();
        }
        return this.defaultFactory;
    }
    
    private Object[] coerceParams(final ExpressionFactory factory, final Method method, final Object[] params) {
        final Class<?>[] types = method.getParameterTypes();
        final Object[] args = new Object[types.length];
        if (method.isVarArgs()) {
            final int varargIndex = types.length - 1;
            if (params.length < varargIndex) {
                throw new ELException("Bad argument count");
            }
            for (int i = 0; i < varargIndex; ++i) {
                this.coerceValue(args, i, factory, params[i], types[i]);
            }
            final Class<?> varargType = types[varargIndex].getComponentType();
            int length = params.length - varargIndex;
            Object array = null;
            if (length == 1) {
                final Object source = params[varargIndex];
                if (source != null && source.getClass().isArray()) {
                    if (types[varargIndex].isInstance(source)) {
                        array = source;
                    }
                    else {
                        length = Array.getLength(source);
                        array = Array.newInstance(varargType, length);
                        for (int j = 0; j < length; ++j) {
                            this.coerceValue(array, j, factory, Array.get(source, j), varargType);
                        }
                    }
                }
                else {
                    array = Array.newInstance(varargType, 1);
                    this.coerceValue(array, 0, factory, source, varargType);
                }
            }
            else {
                array = Array.newInstance(varargType, length);
                for (int k = 0; k < length; ++k) {
                    this.coerceValue(array, k, factory, params[varargIndex + k], varargType);
                }
            }
            args[varargIndex] = array;
        }
        else {
            if (params.length != args.length) {
                throw new ELException("Bad argument count");
            }
            for (int l = 0; l < args.length; ++l) {
                this.coerceValue(args, l, factory, params[l], types[l]);
            }
        }
        return args;
    }
    
    private void coerceValue(final Object array, final int index, final ExpressionFactory factory, final Object value, final Class<?> type) {
        if (value != null || type.isPrimitive()) {
            Array.set(array, index, factory.coerceToType(value, type));
        }
    }
    
    private final boolean isResolvable(final Object base) {
        return base != null;
    }
    
    private final BeanProperty toBeanProperty(final Object base, final Object property) {
        BeanProperties beanProperties = this.cache.get(base.getClass());
        if (beanProperties == null) {
            final BeanProperties newBeanProperties = new BeanProperties(base.getClass());
            beanProperties = this.cache.putIfAbsent(base.getClass(), newBeanProperties);
            if (beanProperties == null) {
                beanProperties = newBeanProperties;
            }
        }
        final BeanProperty beanProperty = (property == null) ? null : beanProperties.getBeanProperty(property.toString());
        if (beanProperty == null) {
            throw new PropertyNotFoundException("Could not find property " + property + " in " + base.getClass());
        }
        return beanProperty;
    }
    
    private final void purgeBeanClasses(final ClassLoader loader) {
        final Iterator<Class<?>> classes = this.cache.keySet().iterator();
        while (classes.hasNext()) {
            if (loader == classes.next().getClassLoader()) {
                classes.remove();
            }
        }
    }
    
    protected static final class BeanProperties
    {
        private final Map<String, BeanProperty> map;
        
        public BeanProperties(final Class<?> baseClass) {
            this.map = new HashMap<String, BeanProperty>();
            PropertyDescriptor[] descriptors;
            try {
                descriptors = Introspector.getBeanInfo(baseClass).getPropertyDescriptors();
            }
            catch (IntrospectionException e) {
                throw new ELException(e);
            }
            for (final PropertyDescriptor descriptor : descriptors) {
                this.map.put(descriptor.getName(), new BeanProperty(descriptor));
            }
        }
        
        public BeanProperty getBeanProperty(final String property) {
            return this.map.get(property);
        }
    }
    
    protected static final class BeanProperty
    {
        private final PropertyDescriptor descriptor;
        
        public BeanProperty(final PropertyDescriptor descriptor) {
            this.descriptor = descriptor;
        }
        
        public Class<?> getPropertyType() {
            return this.descriptor.getPropertyType();
        }
        
        public Method getReadMethod() {
            return findAccessibleMethod(this.descriptor.getReadMethod());
        }
        
        public Method getWriteMethod() {
            return findAccessibleMethod(this.descriptor.getWriteMethod());
        }
        
        public boolean isReadOnly() {
            return findAccessibleMethod(this.descriptor.getWriteMethod()) == null;
        }
    }
}
