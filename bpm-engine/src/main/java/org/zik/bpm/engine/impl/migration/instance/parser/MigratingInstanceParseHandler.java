// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

public interface MigratingInstanceParseHandler<T>
{
    void handle(final MigratingInstanceParseContext p0, final T p1);
}
