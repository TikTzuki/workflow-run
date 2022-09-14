// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstUnary extends AstRightValue
{
    public static final Operator EMPTY;
    public static final Operator NEG;
    public static final Operator NOT;
    private final Operator operator;
    private final AstNode child;
    
    public AstUnary(final AstNode child, final Operator operator) {
        this.child = child;
        this.operator = operator;
    }
    
    public Operator getOperator() {
        return this.operator;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) throws ELException {
        return this.operator.eval(bindings, context, this.child);
    }
    
    @Override
    public String toString() {
        return "'" + this.operator.toString() + "'";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append(this.operator);
        b.append(' ');
        this.child.appendStructure(b, bindings);
    }
    
    @Override
    public int getCardinality() {
        return 1;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.child : null;
    }
    
    static {
        EMPTY = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o) {
                return BooleanOperations.empty(converter, o);
            }
            
            @Override
            public String toString() {
                return "empty";
            }
        };
        NEG = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o) {
                return NumberOperations.neg(converter, o);
            }
            
            @Override
            public String toString() {
                return "-";
            }
        };
        NOT = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o) {
                return !converter.convert(o, Boolean.class);
            }
            
            @Override
            public String toString() {
                return "!";
            }
        };
    }
    
    public abstract static class SimpleOperator implements Operator
    {
        @Override
        public Object eval(final Bindings bindings, final ELContext context, final AstNode node) {
            return this.apply(bindings, node.eval(bindings, context));
        }
        
        protected abstract Object apply(final TypeConverter p0, final Object p1);
    }
    
    public interface Operator
    {
        Object eval(final Bindings p0, final ELContext p1, final AstNode p2);
    }
}
