// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;

public final class TreeValueExpression extends ValueExpression
{
    private static final long serialVersionUID = 1L;
    private final TreeBuilder builder;
    private final Bindings bindings;
    private final String expr;
    private final Class<?> type;
    private final boolean deferred;
    private transient ExpressionNode node;
    private String structure;
    
    public TreeValueExpression(final TreeStore store, final FunctionMapper functions, final VariableMapper variables, final TypeConverter converter, final String expr, final Class<?> type) {
        final Tree tree = store.get(expr);
        this.builder = store.getBuilder();
        this.bindings = tree.bind(functions, variables, converter);
        this.expr = expr;
        this.type = type;
        this.node = tree.getRoot();
        this.deferred = tree.isDeferred();
        if (type == null) {
            throw new NullPointerException(LocalMessages.get("error.value.notype", new Object[0]));
        }
    }
    
    private String getStructuralId() {
        if (this.structure == null) {
            this.structure = this.node.getStructuralId(this.bindings);
        }
        return this.structure;
    }
    
    @Override
    public Class<?> getExpectedType() {
        return this.type;
    }
    
    @Override
    public String getExpressionString() {
        return this.expr;
    }
    
    @Override
    public Class<?> getType(final ELContext context) throws ELException {
        return this.node.getType(this.bindings, context);
    }
    
    @Override
    public Object getValue(final ELContext context) throws ELException {
        return this.node.getValue(this.bindings, context, this.type);
    }
    
    @Override
    public boolean isReadOnly(final ELContext context) throws ELException {
        return this.node.isReadOnly(this.bindings, context);
    }
    
    @Override
    public void setValue(final ELContext context, final Object value) throws ELException {
        this.node.setValue(this.bindings, context, value);
    }
    
    @Override
    public boolean isLiteralText() {
        return this.node.isLiteralText();
    }
    
    @Override
    public ValueReference getValueReference(final ELContext context) {
        return this.node.getValueReference(this.bindings, context);
    }
    
    public boolean isLeftValue() {
        return this.node.isLeftValue();
    }
    
    public boolean isDeferred() {
        return this.deferred;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final TreeValueExpression other = (TreeValueExpression)obj;
            return this.builder.equals(other.builder) && this.type == other.type && this.getStructuralId().equals(other.getStructuralId()) && this.bindings.equals(other.bindings);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getStructuralId().hashCode();
    }
    
    @Override
    public String toString() {
        return "TreeValueExpression(" + this.expr + ")";
    }
    
    public void dump(final PrintWriter writer) {
        NodePrinter.dump(writer, this.node);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            this.node = this.builder.build(this.expr).getRoot();
        }
        catch (ELException e) {
            throw new IOException(e.getMessage());
        }
    }
}
