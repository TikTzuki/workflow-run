// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstBinary extends AstRightValue
{
    public static final Operator ADD;
    public static final Operator AND;
    public static final Operator DIV;
    public static final Operator EQ;
    public static final Operator GE;
    public static final Operator GT;
    public static final Operator LE;
    public static final Operator LT;
    public static final Operator MOD;
    public static final Operator MUL;
    public static final Operator NE;
    public static final Operator OR;
    public static final Operator SUB;
    private final Operator operator;
    private final AstNode left;
    private final AstNode right;
    
    public AstBinary(final AstNode left, final AstNode right, final Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    
    public Operator getOperator() {
        return this.operator;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.operator.eval(bindings, context, this.left, this.right);
    }
    
    @Override
    public String toString() {
        return "'" + this.operator.toString() + "'";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        this.left.appendStructure(b, bindings);
        b.append(' ');
        b.append(this.operator);
        b.append(' ');
        this.right.appendStructure(b, bindings);
    }
    
    @Override
    public int getCardinality() {
        return 2;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.left : ((i == 1) ? this.right : null);
    }
    
    static {
        ADD = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return NumberOperations.add(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "+";
            }
        };
        AND = new Operator() {
            @Override
            public Object eval(final Bindings bindings, final ELContext context, final AstNode left, final AstNode right) {
                final Boolean l = bindings.convert(left.eval(bindings, context), Boolean.class);
                return Boolean.TRUE.equals(l) ? bindings.convert(right.eval(bindings, context), Boolean.class) : Boolean.FALSE;
            }
            
            @Override
            public String toString() {
                return "&&";
            }
        };
        DIV = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return NumberOperations.div(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "/";
            }
        };
        EQ = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.eq(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "==";
            }
        };
        GE = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.ge(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return ">=";
            }
        };
        GT = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.gt(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return ">";
            }
        };
        LE = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.le(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "<=";
            }
        };
        LT = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.lt(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "<";
            }
        };
        MOD = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return NumberOperations.mod(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "%";
            }
        };
        MUL = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return NumberOperations.mul(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "*";
            }
        };
        NE = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return BooleanOperations.ne(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "!=";
            }
        };
        OR = new Operator() {
            @Override
            public Object eval(final Bindings bindings, final ELContext context, final AstNode left, final AstNode right) {
                final Boolean l = bindings.convert(left.eval(bindings, context), Boolean.class);
                return Boolean.TRUE.equals(l) ? Boolean.TRUE : bindings.convert(right.eval(bindings, context), Boolean.class);
            }
            
            @Override
            public String toString() {
                return "||";
            }
        };
        SUB = new SimpleOperator() {
            public Object apply(final TypeConverter converter, final Object o1, final Object o2) {
                return NumberOperations.sub(converter, o1, o2);
            }
            
            @Override
            public String toString() {
                return "-";
            }
        };
    }
    
    public abstract static class SimpleOperator implements Operator
    {
        @Override
        public Object eval(final Bindings bindings, final ELContext context, final AstNode left, final AstNode right) {
            return this.apply(bindings, left.eval(bindings, context), right.eval(bindings, context));
        }
        
        protected abstract Object apply(final TypeConverter p0, final Object p1, final Object p2);
    }
    
    public interface Operator
    {
        Object eval(final Bindings p0, final ELContext p1, final AstNode p2, final AstNode p3);
    }
}
