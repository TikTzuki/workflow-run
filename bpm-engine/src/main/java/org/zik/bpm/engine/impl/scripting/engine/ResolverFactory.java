// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import org.zik.bpm.engine.delegate.VariableScope;

public interface ResolverFactory
{
    Resolver createResolver(final VariableScope p0);
}
