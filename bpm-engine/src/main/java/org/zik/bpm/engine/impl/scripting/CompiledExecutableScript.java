// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import javax.script.ScriptException;
import org.zik.bpm.engine.ScriptEvaluationException;
import org.zik.bpm.engine.delegate.BpmnError;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import javax.script.ScriptEngine;
import javax.script.CompiledScript;

public class CompiledExecutableScript extends ExecutableScript
{
    private static final ScriptLogger LOG;
    protected CompiledScript compiledScript;
    
    protected CompiledExecutableScript(final String language) {
        this(language, null);
    }
    
    protected CompiledExecutableScript(final String language, final CompiledScript compiledScript) {
        super(language);
        this.compiledScript = compiledScript;
    }
    
    public CompiledScript getCompiledScript() {
        return this.compiledScript;
    }
    
    public void setCompiledScript(final CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }
    
    public Object evaluate(final ScriptEngine scriptEngine, final VariableScope variableScope, final Bindings bindings) {
        try {
            CompiledExecutableScript.LOG.debugEvaluatingCompiledScript(this.language);
            return this.getCompiledScript().eval(bindings);
        }
        catch (ScriptException e) {
            if (e.getCause() instanceof BpmnError) {
                throw (BpmnError)e.getCause();
            }
            final String activityIdMessage = this.getActivityIdExceptionMessage(variableScope);
            throw new ScriptEvaluationException("Unable to evaluate script" + activityIdMessage + ": " + e.getMessage(), e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.SCRIPT_LOGGER;
    }
}
