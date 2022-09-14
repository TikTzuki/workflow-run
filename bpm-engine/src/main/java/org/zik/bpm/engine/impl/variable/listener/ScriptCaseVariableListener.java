// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.delegate.DelegateVariableInstance;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.delegate.CaseVariableListener;

public class ScriptCaseVariableListener implements CaseVariableListener
{
    protected final ExecutableScript script;
    
    public ScriptCaseVariableListener(final ExecutableScript script) {
        this.script = script;
    }
    
    @Override
    public void notify(final DelegateCaseVariableInstance variableInstance) throws Exception {
        final DelegateCaseVariableInstanceImpl variableInstanceImpl = (DelegateCaseVariableInstanceImpl)variableInstance;
        final ScriptInvocation invocation = new ScriptInvocation(this.script, variableInstanceImpl.getScopeExecution());
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
    }
    
    public ExecutableScript getScript() {
        return this.script;
    }
}
