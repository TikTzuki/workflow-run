// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class SimpleContext extends ELContext
{
    private Functions functions;
    private Variables variables;
    private ELResolver resolver;
    
    public SimpleContext() {
        this(null);
    }
    
    public SimpleContext(final ELResolver resolver) {
        this.resolver = resolver;
    }
    
    public void setFunction(final String prefix, final String localName, final Method method) {
        if (this.functions == null) {
            this.functions = new Functions();
        }
        this.functions.setFunction(prefix, localName, method);
    }
    
    public ValueExpression setVariable(final String name, final ValueExpression expression) {
        if (this.variables == null) {
            this.variables = new Variables();
        }
        return this.variables.setVariable(name, expression);
    }
    
    @Override
    public FunctionMapper getFunctionMapper() {
        if (this.functions == null) {
            this.functions = new Functions();
        }
        return this.functions;
    }
    
    @Override
    public VariableMapper getVariableMapper() {
        if (this.variables == null) {
            this.variables = new Variables();
        }
        return this.variables;
    }
    
    @Override
    public ELResolver getELResolver() {
        if (this.resolver == null) {
            this.resolver = new SimpleResolver();
        }
        return this.resolver;
    }
    
    public void setELResolver(final ELResolver resolver) {
        this.resolver = resolver;
    }
    
    static class Functions extends FunctionMapper
    {
        Map<String, Method> map;
        
        Functions() {
            this.map = Collections.emptyMap();
        }
        
        @Override
        public Method resolveFunction(final String prefix, final String localName) {
            return this.map.get(prefix + ":" + localName);
        }
        
        public void setFunction(final String prefix, final String localName, final Method method) {
            if (this.map.isEmpty()) {
                this.map = new HashMap<String, Method>();
            }
            this.map.put(prefix + ":" + localName, method);
        }
    }
    
    static class Variables extends VariableMapper
    {
        Map<String, ValueExpression> map;
        
        Variables() {
            this.map = Collections.emptyMap();
        }
        
        @Override
        public ValueExpression resolveVariable(final String variable) {
            return this.map.get(variable);
        }
        
        @Override
        public ValueExpression setVariable(final String variable, final ValueExpression expression) {
            if (this.map.isEmpty()) {
                this.map = new HashMap<String, ValueExpression>();
            }
            return this.map.put(variable, expression);
        }
    }
}
