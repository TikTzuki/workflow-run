// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.builder;

import org.zik.bpm.engine.impl.interceptor.CommandContext;

public interface OperationLogInstanceCountHandler
{
    void write(final CommandContext p0, final int p1);
}
