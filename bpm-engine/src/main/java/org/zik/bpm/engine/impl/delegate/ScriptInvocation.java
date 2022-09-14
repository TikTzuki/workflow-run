// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;

public class ScriptInvocation extends DelegateInvocation
{
    protected ExecutableScript script;
    protected VariableScope scope;
    
    public ScriptInvocation(final ExecutableScript script, final VariableScope scope) {
        this(script, scope, null);
    }
    
    public ScriptInvocation(final ExecutableScript script, final VariableScope scope, final BaseDelegateExecution contextExecution) {
        super(contextExecution, null);
        this.script = script;
        this.scope = scope;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.invocationResult = Context.getProcessEngineConfiguration().getScriptingEnvironment().execute(this.script, this.scope);
    }
}
