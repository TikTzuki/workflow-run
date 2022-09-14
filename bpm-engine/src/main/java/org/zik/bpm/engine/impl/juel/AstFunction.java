// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstFunction extends AstRightValue implements FunctionNode
{
    private final int index;
    private final String name;
    private final AstParameters params;
    private final boolean varargs;
    
    public AstFunction(final String name, final int index, final AstParameters params) {
        this(name, index, params, false);
    }
    
    public AstFunction(final String name, final int index, final AstParameters params, final boolean varargs) {
        this.name = name;
        this.index = index;
        this.params = params;
        this.varargs = varargs;
    }
    
    protected Object invoke(final Bindings bindings, final ELContext context, final Object base, final Method method) throws InvocationTargetException, IllegalAccessException {
        final Class<?>[] types = method.getParameterTypes();
        Object[] params = null;
        if (types.length > 0) {
            params = new Object[types.length];
            if (this.varargs && method.isVarArgs()) {
                for (int i = 0; i < params.length - 1; ++i) {
                    final Object param = this.getParam(i).eval(bindings, context);
                    if (param != null || types[i].isPrimitive()) {
                        params[i] = bindings.convert(param, types[i]);
                    }
                }
                final int varargIndex = types.length - 1;
                final Class<?> varargType = types[varargIndex].getComponentType();
                int length = this.getParamCount() - varargIndex;
                Object array = null;
                if (length == 1) {
                    final Object param2 = this.getParam(varargIndex).eval(bindings, context);
                    if (param2 != null && param2.getClass().isArray()) {
                        if (types[varargIndex].isInstance(param2)) {
                            array = param2;
                        }
                        else {
                            length = Array.getLength(param2);
                            array = Array.newInstance(varargType, length);
                            for (int j = 0; j < length; ++j) {
                                final Object elem = Array.get(param2, j);
                                if (elem != null || varargType.isPrimitive()) {
                                    Array.set(array, j, bindings.convert(elem, varargType));
                                }
                            }
                        }
                    }
                    else {
                        array = Array.newInstance(varargType, 1);
                        if (param2 != null || varargType.isPrimitive()) {
                            Array.set(array, 0, bindings.convert(param2, varargType));
                        }
                    }
                }
                else {
                    array = Array.newInstance(varargType, length);
                    for (int k = 0; k < length; ++k) {
                        final Object param3 = this.getParam(varargIndex + k).eval(bindings, context);
                        if (param3 != null || varargType.isPrimitive()) {
                            Array.set(array, k, bindings.convert(param3, varargType));
                        }
                    }
                }
                params[varargIndex] = array;
            }
            else {
                for (int i = 0; i < params.length; ++i) {
                    final Object param = this.getParam(i).eval(bindings, context);
                    if (param != null || types[i].isPrimitive()) {
                        params[i] = bindings.convert(param, types[i]);
                    }
                }
            }
        }
        return method.invoke(base, params);
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        final Method method = bindings.getFunction(this.index);
        try {
            return this.invoke(bindings, context, null, method);
        }
        catch (IllegalAccessException e) {
            throw new ELException(LocalMessages.get("error.function.access", this.name), e);
        }
        catch (InvocationTargetException e2) {
            throw new ELException(LocalMessages.get("error.function.invocation", this.name), e2.getCause());
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append((bindings != null && bindings.isFunctionBound(this.index)) ? "<fn>" : this.name);
        this.params.appendStructure(b, bindings);
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
    public boolean isVarArgs() {
        return this.varargs;
    }
    
    @Override
    public int getParamCount() {
        return this.params.getCardinality();
    }
    
    protected AstNode getParam(final int i) {
        return this.params.getChild(i);
    }
    
    @Override
    public int getCardinality() {
        return 1;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.params : null;
    }
}
