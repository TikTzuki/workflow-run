// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.Set;

public interface Resolver
{
    boolean containsKey(final Object p0);
    
    Object get(final Object p0);
    
    Set<String> keySet();
}
