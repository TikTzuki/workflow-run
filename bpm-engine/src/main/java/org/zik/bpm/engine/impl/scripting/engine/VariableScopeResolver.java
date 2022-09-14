// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.Set;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.delegate.VariableScope;

public class VariableScopeResolver implements Resolver
{
    protected VariableScope variableScope;
    protected String variableScopeKey;
    
    public VariableScopeResolver(final VariableScope variableScope) {
        EnsureUtil.ensureNotNull("variableScope", variableScope);
        this.variableScopeKey = variableScope.getVariableScopeKey();
        this.variableScope = variableScope;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.variableScopeKey.equals(key) || this.variableScope.hasVariable((String)key);
    }
    
    @Override
    public Object get(final Object key) {
        if (this.variableScopeKey.equals(key)) {
            return this.variableScope;
        }
        return this.variableScope.getVariable((String)key);
    }
    
    @Override
    public Set<String> keySet() {
        final Set<String> variableNames = this.variableScope.getVariableNames();
        variableNames.add(this.variableScopeKey);
        return variableNames;
    }
}
