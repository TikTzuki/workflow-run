// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import org.zik.bpm.engine.delegate.VariableScope;

public class VariableScopeResolverFactory implements ResolverFactory
{
    @Override
    public Resolver createResolver(final VariableScope variableScope) {
        if (variableScope != null) {
            return new VariableScopeResolver(variableScope);
        }
        return null;
    }
}
