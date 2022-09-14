// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface InstantiationBuilder<T extends InstantiationBuilder<T>>
{
    T startBeforeActivity(final String p0);
    
    T startAfterActivity(final String p0);
    
    T startTransition(final String p0);
}
