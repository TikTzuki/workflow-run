// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.standalone;

import org.zik.bpm.engine.impl.cfg.TransactionContext;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionContextFactory;

public class StandaloneTransactionContextFactory implements TransactionContextFactory
{
    @Override
    public TransactionContext openTransactionContext(final CommandContext commandContext) {
        return new StandaloneTransactionContext(commandContext);
    }
}
