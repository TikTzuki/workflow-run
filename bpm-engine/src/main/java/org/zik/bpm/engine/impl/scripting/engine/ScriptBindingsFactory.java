// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.Iterator;
import java.util.ArrayList;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.List;

public class ScriptBindingsFactory
{
    protected List<ResolverFactory> resolverFactories;
    
    public ScriptBindingsFactory(final List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
    }
    
    public Bindings createBindings(final VariableScope variableScope, final Bindings engineBindings) {
        final List<Resolver> scriptResolvers = new ArrayList<Resolver>();
        for (final ResolverFactory scriptResolverFactory : this.resolverFactories) {
            final Resolver resolver = scriptResolverFactory.createResolver(variableScope);
            if (resolver != null) {
                scriptResolvers.add(resolver);
            }
        }
        return new ScriptBindings(scriptResolvers, variableScope, engineBindings);
    }
    
    public List<ResolverFactory> getResolverFactories() {
        return this.resolverFactories;
    }
    
    public void setResolverFactories(final List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
    }
}
