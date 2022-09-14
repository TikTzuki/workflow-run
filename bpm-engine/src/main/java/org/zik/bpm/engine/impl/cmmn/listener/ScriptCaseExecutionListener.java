// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.delegate.CaseExecutionListener;

public class ScriptCaseExecutionListener implements CaseExecutionListener
{
    protected final ExecutableScript script;
    
    public ScriptCaseExecutionListener(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        final ScriptInvocation invocation = new ScriptInvocation(this.script, caseExecution);
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
