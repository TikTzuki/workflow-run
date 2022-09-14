// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.lang.reflect.InvocationTargetException;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import java.util.Arrays;
import org.zik.bpm.engine.impl.javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstIdentifier extends AstNode implements IdentifierNode
{
    private final String name;
    private final int index;
    
    public AstIdentifier(final String name, final int index) {
        this.name = name;
        this.index = index;
    }
    
    @Override
    public Class<?> getType(final Bindings bindings, final ELContext context) {
        final ValueExpression expression = bindings.getVariable(this.index);
        if (expression != null) {
            return expression.getType(context);
        }
        context.setPropertyResolved(false);
        final Class<?> result = context.getELResolver().getType(context, null, this.name);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.identifier.property.notfound", this.name));
        }
        return result;
    }
    
    @Override
    public boolean isLeftValue() {
        return true;
    }
    
    @Override
    public boolean isMethodInvocation() {
        return false;
    }
    
    @Override
    public boolean isLiteralText() {
        return false;
    }
    
    @Override
    public ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        final ValueExpression expression = bindings.getVariable(this.index);
        if (expression != null) {
            return expression.getValueReference(context);
        }
        return new ValueReference(null, this.name);
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        final ValueExpression expression = bindings.getVariable(this.index);
        if (expression != null) {
            return expression.getValue(context);
        }
        context.setPropertyResolved(false);
        final Object result = context.getELResolver().getValue(context, null, this.name);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.identifier.property.notfound", this.name));
        }
        return result;
    }
    
    @Override
    public void setValue(final Bindings bindings, final ELContext context, final Object value) {
        final ValueExpression expression = bindings.getVariable(this.index);
        if (expression != null) {
            expression.setValue(context, value);
            return;
        }
        context.setPropertyResolved(false);
        context.getELResolver().setValue(context, null, this.name, value);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.identifier.property.notfound", this.name));
        }
    }
    
    @Override
    public boolean isReadOnly(final Bindings bindings, final ELContext context) {
        final ValueExpression expression = bindings.getVariable(this.index);
        if (expression != null) {
            return expression.isReadOnly(context);
        }
        context.setPropertyResolved(false);
        final boolean result = context.getELResolver().isReadOnly(context, null, this.name);
        if (!context.isPropertyResolved()) {
            throw new PropertyNotFoundException(LocalMessages.get("error.identifier.property.notfound", this.name));
        }
        return result;
    }
    
    protected Method getMethod(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        final Object value = this.eval(bindings, context);
        if (value == null) {
            throw new MethodNotFoundException(LocalMessages.get("error.identifier.method.notfound", this.name));
        }
        if (!(value instanceof Method)) {
            throw new MethodNotFoundException(LocalMessages.get("error.identifier.method.notamethod", this.name, value.getClass()));
        }
        final Method method = (Method)value;
        if (returnType != null && !returnType.isAssignableFrom(method.getReturnType())) {
            throw new MethodNotFoundException(LocalMessages.get("error.identifier.method.notfound", this.name));
        }
        if (!Arrays.equals(method.getParameterTypes(), paramTypes)) {
            throw new MethodNotFoundException(LocalMessages.get("error.identifier.method.notfound", this.name));
        }
        return method;
    }
    
    @Override
    public MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        final Method method = this.getMethod(bindings, context, returnType, paramTypes);
        return new MethodInfo(method.getName(), method.getReturnType(), paramTypes);
    }
    
    @Override
    public Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, final Object[] params) {
        final Method method = this.getMethod(bindings, context, returnType, paramTypes);
        try {
            return method.invoke(null, params);
        }
        catch (IllegalAccessException e3) {
            throw new ELException(LocalMessages.get("error.identifier.method.access", this.name));
        }
        catch (IllegalArgumentException e) {
            throw new ELException(LocalMessages.get("error.identifier.method.invocation", this.name, e));
        }
        catch (InvocationTargetException e2) {
            throw new ELException(LocalMessages.get("error.identifier.method.invocation", this.name, e2.getCause()));
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append((bindings != null && bindings.isVariableBound(this.index)) ? "<var>" : this.name);
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getCardinality() {
        return 0;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return null;
    }
}
