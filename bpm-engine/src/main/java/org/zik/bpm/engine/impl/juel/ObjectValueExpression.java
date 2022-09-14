// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;

public final class ObjectValueExpression extends ValueExpression
{
    private static final long serialVersionUID = 1L;
    private final TypeConverter converter;
    private final Object object;
    private final Class<?> type;
    
    public ObjectValueExpression(final TypeConverter converter, final Object object, final Class<?> type) {
        this.converter = converter;
        this.object = object;
        this.type = type;
        if (type == null) {
            throw new NullPointerException(LocalMessages.get("error.value.notype", new Object[0]));
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final ObjectValueExpression other = (ObjectValueExpression)obj;
            return this.type == other.type && (this.object == other.object || (this.object != null && this.object.equals(other.object)));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.object == null) ? 0 : this.object.hashCode();
    }
    
    @Override
    public Object getValue(final ELContext context) {
        return this.converter.convert(this.object, this.type);
    }
    
    @Override
    public String getExpressionString() {
        return null;
    }
    
    @Override
    public boolean isLiteralText() {
        return false;
    }
    
    @Override
    public Class<?> getType(final ELContext context) {
        return null;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context) {
        return true;
    }
    
    @Override
    public void setValue(final ELContext context, final Object value) {
        throw new ELException(LocalMessages.get("error.value.set.rvalue", "<object value expression>"));
    }
    
    @Override
    public String toString() {
        return "ValueExpression(" + this.object + ")";
    }
    
    @Override
    public Class<?> getExpectedType() {
        return this.type;
    }
}
