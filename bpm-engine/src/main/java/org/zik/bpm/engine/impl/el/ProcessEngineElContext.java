// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import java.util.List;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class ProcessEngineElContext extends ELContext
{
    protected ELResolver elResolver;
    protected FunctionMapper functionMapper;
    
    public ProcessEngineElContext(final List<FunctionMapper> functionMappers, final ELResolver elResolver) {
        this(functionMappers);
        this.elResolver = elResolver;
    }
    
    public ProcessEngineElContext(final List<FunctionMapper> functionMappers) {
        this.functionMapper = new CompositeFunctionMapper(functionMappers);
    }
    
    @Override
    public ELResolver getELResolver() {
        return this.elResolver;
    }
    
    @Override
    public FunctionMapper getFunctionMapper() {
        return this.functionMapper;
    }
    
    @Override
    public VariableMapper getVariableMapper() {
        return null;
    }
}
