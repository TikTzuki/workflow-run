// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.Set;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;

public class BeansResolverFactory implements ResolverFactory, Resolver
{
    @Override
    public Resolver createResolver(final VariableScope variableScope) {
        return this;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return Context.getProcessEngineConfiguration().getBeans().containsKey(key);
    }
    
    @Override
    public Object get(final Object key) {
        return Context.getProcessEngineConfiguration().getBeans().get(key);
    }
    
    @Override
    public Set<String> keySet() {
        return Context.getProcessEngineConfiguration().getBeans().keySet();
    }
}
