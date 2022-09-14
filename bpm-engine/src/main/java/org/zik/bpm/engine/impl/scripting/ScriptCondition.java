// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.ScriptEvaluationException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.Condition;

public class ScriptCondition implements Condition
{
    protected final ExecutableScript script;
    
    public ScriptCondition(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public boolean evaluate(final DelegateExecution execution) {
        return this.evaluate(execution, execution);
    }
    
    @Override
    public boolean evaluate(final VariableScope scope, final DelegateExecution execution) {
        final ScriptInvocation invocation = new ScriptInvocation(this.script, scope, execution);
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
        final Object result = invocation.getInvocationResult();
        EnsureUtil.ensureNotNull("condition script returns null", "result", result);
        EnsureUtil.ensureInstanceOf("condition script returns non-Boolean", "result", result, Boolean.class);
        return (boolean)result;
    }
    
    @Override
    public boolean tryEvaluate(final VariableScope scope, final DelegateExecution execution) {
        boolean result = false;
        try {
            result = this.evaluate(scope, execution);
        }
        catch (ProcessEngineException pee) {
            if (!pee.getMessage().contains("No such property") && !(pee.getCause() instanceof ScriptEvaluationException)) {
                throw pee;
            }
        }
        return result;
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
