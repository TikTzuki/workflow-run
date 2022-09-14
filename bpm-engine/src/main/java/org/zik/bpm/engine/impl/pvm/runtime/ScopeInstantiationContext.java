// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import java.util.Map;

public class ScopeInstantiationContext
{
    protected InstantiationStack instantiationStack;
    protected Map<String, Object> variables;
    protected Map<String, Object> variablesLocal;
    
    public void applyVariables(final CoreExecution execution) {
        execution.setVariables(this.variables);
        execution.setVariablesLocal(this.variablesLocal);
    }
    
    public InstantiationStack getInstantiationStack() {
        return this.instantiationStack;
    }
    
    public void setInstantiationStack(final InstantiationStack instantiationStack) {
        this.instantiationStack = instantiationStack;
    }
    
    public void setVariables(final Map<String, Object> variables) {
        this.variables = variables;
    }
    
    public void setVariablesLocal(final Map<String, Object> variablesLocal) {
        this.variablesLocal = variablesLocal;
    }
}
