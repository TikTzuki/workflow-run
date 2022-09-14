// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test.mock;

import java.util.Set;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.scripting.engine.Resolver;
import org.zik.bpm.engine.impl.scripting.engine.ResolverFactory;

public class MocksResolverFactory implements ResolverFactory, Resolver
{
    @Override
    public Resolver createResolver(final VariableScope variableScope) {
        return this;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return Mocks.get(key) != null;
    }
    
    @Override
    public Object get(final Object key) {
        return Mocks.get(key);
    }
    
    @Override
    public Set<String> keySet() {
        return Mocks.getMocks().keySet();
    }
}
