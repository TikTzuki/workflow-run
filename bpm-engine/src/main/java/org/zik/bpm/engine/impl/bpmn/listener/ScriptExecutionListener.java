// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.delegate.ExecutionListener;

public class ScriptExecutionListener implements ExecutionListener
{
    protected final ExecutableScript script;
    
    public ScriptExecutionListener(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        final ScriptInvocation invocation = new ScriptInvocation(this.script, execution);
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
