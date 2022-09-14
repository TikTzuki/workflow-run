// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.lang.reflect.InvocationTargetException;
import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public abstract class AstProperty extends AstNode
{
    protected final AstNode prefix;
    protected final boolean lvalue;
    protected final boolean strict;
    
    public AstProperty(final AstNode prefix, final boolean lvalue, final boolean strict) {
        this.prefix = prefix;
        this.lvalue = lvalue;
        this.strict = strict;
    }
    
    protected abstract Object getProperty(final Bindings p0, final ELContext p1) throws ELException;
    
    protected AstNode getPrefix() {
        return this.prefix;
    }
    
    @Override
    public ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        return new ValueReference(this.prefix.eval(bindings, context), this.getProperty(bindings, context));
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            return null;
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            return null;
        }
        context.setPropertyResolved(false);
        final Object result = context.getELResolver().getValue(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }
    
    @Override
    public final boolean isLiteralText() {
        return false;
    }
    
    @Override
    public final boolean isLeftValue() {
        return this.lvalue;
    }
    
    @Override
    public boolean isMethodInvocation() {
        return false;
    }
    
    @Override
    public Class<?> getType(final Bindings bindings, final ELContext context) {
        if (!this.lvalue) {
            return null;
        }
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        final Class<?> result = context.getELResolver().getType(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }
    
    @Override
    public boolean isReadOnly(final Bindings bindings, final ELContext context) throws ELException {
        if (!this.lvalue) {
            return true;
        }
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        final boolean result = context.getELResolver().isReadOnly(context, base, property);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
        return result;
    }
    
    @Override
    public void setValue(final Bindings bindings, final ELContext context, final Object value) throws ELException {
        if (!this.lvalue) {
            throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
        }
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
        }
        context.setPropertyResolved(false);
        context.getELResolver().setValue(context, base, property, value);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
        }
    }
    
    protected Method findMethod(final String name, final Class<?> clazz, final Class<?> returnType, final Class<?>[] paramTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(name, paramTypes);
        }
        catch (NoSuchMethodException e) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz));
        }
        if (returnType != null && !returnType.isAssignableFrom(method.getReturnType())) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz));
        }
        return method;
    }
    
    @Override
    public MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
        }
        final String name = bindings.convert(property, String.class);
        final Method method = this.findMethod(name, base.getClass(), returnType, paramTypes);
        return new MethodInfo(method.getName(), method.getReturnType(), paramTypes);
    }
    
    @Override
    public Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, final Object[] paramValues) {
        final Object base = this.prefix.eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
        }
        final Object property = this.getProperty(bindings, context);
        if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
        }
        final String name = bindings.convert(property, String.class);
        final Method method = this.findMethod(name, base.getClass(), returnType, paramTypes);
        try {
            return method.invoke(base, paramValues);
        }
        catch (IllegalAccessException e3) {
            throw new ELException(LocalMessages.get("error.property.method.access", name, base.getClass()));
        }
        catch (IllegalArgumentException e) {
            throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), e);
        }
        catch (InvocationTargetException e2) {
            throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), e2.getCause());
        }
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.prefix : null;
    }
}
