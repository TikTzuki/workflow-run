// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

public interface MigratingDependentInstanceParseHandler<S, T>
{
    void handle(final MigratingInstanceParseContext p0, final S p1, final T p2);
}
