// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.cfg.TransactionContextFactory;

public class TxContextCommandContextFactory extends CommandContextFactory
{
    protected TransactionContextFactory transactionContextFactory;
    
    @Override
    public CommandContext createCommandContext() {
        return new CommandContext(this.processEngineConfiguration, this.transactionContextFactory);
    }
    
    public TransactionContextFactory getTransactionContextFactory() {
        return this.transactionContextFactory;
    }
    
    public void setTransactionContextFactory(final TransactionContextFactory transactionContextFactory) {
        this.transactionContextFactory = transactionContextFactory;
    }
}
