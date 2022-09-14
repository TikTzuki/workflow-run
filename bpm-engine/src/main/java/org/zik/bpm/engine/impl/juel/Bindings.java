// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import java.lang.reflect.Method;

public class Bindings implements TypeConverter
{
    private static final long serialVersionUID = 1L;
    private static final Method[] NO_FUNCTIONS;
    private static final ValueExpression[] NO_VARIABLES;
    private transient Method[] functions;
    private final ValueExpression[] variables;
    private final TypeConverter converter;
    
    public Bindings(final Method[] functions, final ValueExpression[] variables) {
        this(functions, variables, TypeConverter.DEFAULT);
    }
    
    public Bindings(final Method[] functions, final ValueExpression[] variables, final TypeConverter converter) {
        this.functions = ((functions == null || functions.length == 0) ? Bindings.NO_FUNCTIONS : functions);
        this.variables = ((variables == null || variables.length == 0) ? Bindings.NO_VARIABLES : variables);
        this.converter = ((converter == null) ? TypeConverter.DEFAULT : converter);
    }
    
    public Method getFunction(final int index) {
        return this.functions[index];
    }
    
    public boolean isFunctionBound(final int index) {
        return index >= 0 && index < this.functions.length;
    }
    
    public ValueExpression getVariable(final int index) {
        return this.variables[index];
    }
    
    public boolean isVariableBound(final int index) {
        return index >= 0 && index < this.variables.length && this.variables[index] != null;
    }
    
    @Override
    public <T> T convert(final Object value, final Class<T> type) {
        return this.converter.convert(value, type);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Bindings) {
            final Bindings other = (Bindings)obj;
            return Arrays.equals(this.functions, other.functions) && Arrays.equals(this.variables, other.variables) && this.converter.equals(other.converter);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.functions) ^ Arrays.hashCode(this.variables) ^ this.converter.hashCode();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.defaultWriteObject();
        final MethodWrapper[] wrappers = new MethodWrapper[this.functions.length];
        for (int i = 0; i < wrappers.length; ++i) {
            wrappers[i] = new MethodWrapper(this.functions[i]);
        }
        out.writeObject(wrappers);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final MethodWrapper[] wrappers = (MethodWrapper[])in.readObject();
        if (wrappers.length == 0) {
            this.functions = Bindings.NO_FUNCTIONS;
        }
        else {
            this.functions = new Method[wrappers.length];
            for (int i = 0; i < this.functions.length; ++i) {
                this.functions[i] = wrappers[i].method;
            }
        }
    }
    
    static {
        NO_FUNCTIONS = new Method[0];
        NO_VARIABLES = new ValueExpression[0];
    }
    
    private static class MethodWrapper implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private transient Method method;
        
        private MethodWrapper(final Method method) {
            this.method = method;
        }
        
        private void writeObject(final ObjectOutputStream out) throws IOException, ClassNotFoundException {
            out.defaultWriteObject();
            out.writeObject(this.method.getDeclaringClass());
            out.writeObject(this.method.getName());
            out.writeObject(this.method.getParameterTypes());
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final Class<?> type = (Class<?>)in.readObject();
            final String name = (String)in.readObject();
            final Class<?>[] args = (Class<?>[])in.readObject();
            try {
                this.method = type.getDeclaredMethod(name, args);
            }
            catch (NoSuchMethodException e) {
                throw new IOException(e.getMessage());
            }
        }
    }
}
