// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.MethodNotFoundException;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstMethod extends AstNode
{
    private final AstProperty property;
    private final AstParameters params;
    
    public AstMethod(final AstProperty property, final AstParameters params) {
        this.property = property;
        this.params = params;
    }
    
    @Override
    public boolean isLiteralText() {
        return false;
    }
    
    @Override
    public Class<?> getType(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public boolean isReadOnly(final Bindings bindings, final ELContext context) {
        return true;
    }
    
    @Override
    public void setValue(final Bindings bindings, final ELContext context, final Object value) {
        throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
    }
    
    @Override
    public MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        return null;
    }
    
    @Override
    public boolean isLeftValue() {
        return false;
    }
    
    @Override
    public boolean isMethodInvocation() {
        return true;
    }
    
    @Override
    public final ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public void appendStructure(final StringBuilder builder, final Bindings bindings) {
        this.property.appendStructure(builder, bindings);
        this.params.appendStructure(builder, bindings);
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.invoke(bindings, context, null, null, null);
    }
    
    @Override
    public Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, Object[] paramValues) {
        final Object base = this.property.getPrefix().eval(bindings, context);
        if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.property.getPrefix()));
        }
        final Object method = this.property.getProperty(bindings, context);
        if (method == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
        }
        final String name = bindings.convert(method, String.class);
        paramValues = this.params.eval(bindings, context);
        context.setPropertyResolved(false);
        final Object result = context.getELResolver().invoke(context, base, name, paramTypes, paramValues);
        if (!context.isPropertyResolved()) {
            throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, base.getClass()));
        }
        return result;
    }
    
    @Override
    public int getCardinality() {
        return 2;
    }
    
    @Override
    public Node getChild(final int i) {
        return (i == 0) ? this.property : ((i == 1) ? this.params : null);
    }
    
    @Override
    public String toString() {
        return "<method>";
    }
}
