// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task.listener;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.delegate.TaskListener;

public class ScriptTaskListener implements TaskListener
{
    protected final ExecutableScript script;
    
    public ScriptTaskListener(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public void notify(final DelegateTask delegateTask) {
        final ScriptInvocation invocation = new ScriptInvocation(this.script, delegateTask);
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
