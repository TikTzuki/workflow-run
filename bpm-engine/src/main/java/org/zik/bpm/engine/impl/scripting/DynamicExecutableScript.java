// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import javax.script.ScriptException;
import org.zik.bpm.engine.ScriptEvaluationException;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import javax.script.ScriptEngine;
import org.zik.bpm.engine.delegate.Expression;

public abstract class DynamicExecutableScript extends ExecutableScript
{
    protected final Expression scriptExpression;
    
    protected DynamicExecutableScript(final Expression scriptExpression, final String language) {
        super(language);
        this.scriptExpression = scriptExpression;
    }
    
    public Object evaluate(final ScriptEngine scriptEngine, final VariableScope variableScope, final Bindings bindings) {
        final String source = this.getScriptSource(variableScope);
        try {
            return scriptEngine.eval(source, bindings);
        }
        catch (ScriptException e) {
            final String activityIdMessage = this.getActivityIdExceptionMessage(variableScope);
            throw new ScriptEvaluationException("Unable to evaluate script" + activityIdMessage + ": " + e.getMessage(), e);
        }
    }
    
    protected String evaluateExpression(final VariableScope variableScope) {
        return (String)this.scriptExpression.getValue(variableScope);
    }
    
    public abstract String getScriptSource(final VariableScope p0);
}
