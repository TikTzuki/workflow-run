// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.Expression;

public class DynamicSourceExecutableScript extends DynamicExecutableScript
{
    public DynamicSourceExecutableScript(final String language, final Expression scriptSourceExpression) {
        super(scriptSourceExpression, language);
    }
    
    @Override
    public String getScriptSource(final VariableScope variableScope) {
        return this.evaluateExpression(variableScope);
    }
}
