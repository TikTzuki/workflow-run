// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class ScriptValueProvider implements ParameterValueProvider
{
    protected ExecutableScript script;
    
    public ScriptValueProvider(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        final ScriptInvocation invocation = new ScriptInvocation(this.script, variableScope);
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
        return invocation.getInvocationResult();
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
    
    public void setScript(final ExecutableScript script) {
        this.script = script;
    }
}
