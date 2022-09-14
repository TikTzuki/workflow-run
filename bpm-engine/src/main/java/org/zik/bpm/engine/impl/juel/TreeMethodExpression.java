// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.MethodExpression;

public final class TreeMethodExpression extends MethodExpression
{
    private static final long serialVersionUID = 1L;
    private final TreeBuilder builder;
    private final Bindings bindings;
    private final String expr;
    private final Class<?> type;
    private final Class<?>[] types;
    private final boolean deferred;
    private transient ExpressionNode node;
    private String structure;
    
    public TreeMethodExpression(final TreeStore store, final FunctionMapper functions, final VariableMapper variables, final TypeConverter converter, final String expr, final Class<?> returnType, final Class<?>[] paramTypes) {
        final Tree tree = store.get(expr);
        this.builder = store.getBuilder();
        this.bindings = tree.bind(functions, variables, converter);
        this.expr = expr;
        this.type = returnType;
        this.types = paramTypes;
        this.node = tree.getRoot();
        this.deferred = tree.isDeferred();
        if (this.node.isLiteralText()) {
            if (returnType == Void.TYPE) {
                throw new ELException(LocalMessages.get("error.method.literal.void", expr));
            }
        }
        else if (!this.node.isMethodInvocation()) {
            if (!this.node.isLeftValue()) {
                throw new ELException(LocalMessages.get("error.method.invalid", expr));
            }
            if (paramTypes == null) {
                throw new ELException(LocalMessages.get("error.method.notypes", new Object[0]));
            }
        }
    }
    
    private String getStructuralId() {
        if (this.structure == null) {
            this.structure = this.node.getStructuralId(this.bindings);
        }
        return this.structure;
    }
    
    @Override
    public MethodInfo getMethodInfo(final ELContext context) throws ELException {
        return this.node.getMethodInfo(this.bindings, context, this.type, this.types);
    }
    
    @Override
    public String getExpressionString() {
        return this.expr;
    }
    
    @Override
    public Object invoke(final ELContext context, final Object[] paramValues) throws ELException {
        return this.node.invoke(this.bindings, context, this.type, this.types, paramValues);
    }
    
    @Override
    public boolean isLiteralText() {
        return this.node.isLiteralText();
    }
    
    @Override
    public boolean isParmetersProvided() {
        return this.node.isMethodInvocation();
    }
    
    public boolean isDeferred() {
        return this.deferred;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final TreeMethodExpression other = (TreeMethodExpression)obj;
            return this.builder.equals(other.builder) && this.type == other.type && Arrays.equals(this.types, other.types) && this.getStructuralId().equals(other.getStructuralId()) && this.bindings.equals(other.bindings);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getStructuralId().hashCode();
    }
    
    @Override
    public String toString() {
        return "TreeMethodExpression(" + this.expr + ")";
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
