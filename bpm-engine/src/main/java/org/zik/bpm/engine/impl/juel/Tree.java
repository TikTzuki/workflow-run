// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.Iterator;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import java.util.Collection;

public class Tree
{
    private final ExpressionNode root;
    private final Collection<FunctionNode> functions;
    private final Collection<IdentifierNode> identifiers;
    private final boolean deferred;
    
    public Tree(final ExpressionNode root, final Collection<FunctionNode> functions, final Collection<IdentifierNode> identifiers, final boolean deferred) {
        this.root = root;
        this.functions = functions;
        this.identifiers = identifiers;
        this.deferred = deferred;
    }
    
    public Iterable<FunctionNode> getFunctionNodes() {
        return this.functions;
    }
    
    public Iterable<IdentifierNode> getIdentifierNodes() {
        return this.identifiers;
    }
    
    public ExpressionNode getRoot() {
        return this.root;
    }
    
    public boolean isDeferred() {
        return this.deferred;
    }
    
    @Override
    public String toString() {
        return this.getRoot().getStructuralId(null);
    }
    
    public Bindings bind(final FunctionMapper fnMapper, final VariableMapper varMapper) {
        return this.bind(fnMapper, varMapper, null);
    }
    
    public Bindings bind(final FunctionMapper fnMapper, final VariableMapper varMapper, final TypeConverter converter) {
        Method[] methods = null;
        if (!this.functions.isEmpty()) {
            if (fnMapper == null) {
                throw new ELException(LocalMessages.get("error.function.nomapper", new Object[0]));
            }
            methods = new Method[this.functions.size()];
            for (final FunctionNode node : this.functions) {
                final String image = node.getName();
                Method method = null;
                final int colon = image.indexOf(58);
                if (colon < 0) {
                    method = fnMapper.resolveFunction("", image);
                }
                else {
                    method = fnMapper.resolveFunction(image.substring(0, colon), image.substring(colon + 1));
                }
                if (method == null) {
                    throw new ELException(LocalMessages.get("error.function.notfound", image));
                }
                if (node.isVarArgs() && method.isVarArgs()) {
                    if (method.getParameterTypes().length > node.getParamCount() + 1) {
                        throw new ELException(LocalMessages.get("error.function.params", image));
                    }
                }
                else if (method.getParameterTypes().length != node.getParamCount()) {
                    throw new ELException(LocalMessages.get("error.function.params", image));
                }
                methods[node.getIndex()] = method;
            }
        }
        ValueExpression[] expressions = null;
        if (this.identifiers.size() > 0) {
            expressions = new ValueExpression[this.identifiers.size()];
            for (final IdentifierNode node2 : this.identifiers) {
                ValueExpression expression = null;
                if (varMapper != null) {
                    expression = varMapper.resolveVariable(node2.getName());
                }
                expressions[node2.getIndex()] = expression;
            }
        }
        return new Bindings(methods, expressions, converter);
    }
}
