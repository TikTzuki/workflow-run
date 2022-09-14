// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BpmnError;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;

public class ScriptTaskActivityBehavior extends TaskActivityBehavior
{
    protected ExecutableScript script;
    protected String resultVariable;
    
    public ScriptTaskActivityBehavior(final ExecutableScript script, final String resultVariable) {
        this.script = script;
        this.resultVariable = resultVariable;
    }
    
    public void performExecution(final ActivityExecution execution) throws Exception {
        this.executeWithErrorPropagation(execution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final ScriptInvocation invocation = new ScriptInvocation(ScriptTaskActivityBehavior.this.script, execution);
                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
                final Object result = invocation.getInvocationResult();
                if (result != null && ScriptTaskActivityBehavior.this.resultVariable != null) {
                    execution.setVariable(ScriptTaskActivityBehavior.this.resultVariable, result);
                }
                ScriptTaskActivityBehavior.this.leave(execution);
                return null;
            }
        });
    }
    
    protected BpmnError checkIfCauseOfExceptionIsBpmnError(final Throwable e) {
        if (e instanceof BpmnError) {
            return (BpmnError)e;
        }
        if (e.getCause() == null) {
            return null;
        }
        return this.checkIfCauseOfExceptionIsBpmnError(e.getCause());
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
