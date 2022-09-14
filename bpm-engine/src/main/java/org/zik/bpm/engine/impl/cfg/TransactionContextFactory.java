// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.impl.interceptor.CommandContext;

public interface TransactionContextFactory
{
    TransactionContext openTransactionContext(final CommandContext p0);
}
