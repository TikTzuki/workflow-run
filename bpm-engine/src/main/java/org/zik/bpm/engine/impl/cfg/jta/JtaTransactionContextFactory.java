// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.jta;

import org.zik.bpm.engine.impl.cfg.TransactionContext;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import javax.transaction.TransactionManager;
import org.zik.bpm.engine.impl.cfg.TransactionContextFactory;

public class JtaTransactionContextFactory implements TransactionContextFactory
{
    protected final TransactionManager transactionManager;
    
    public JtaTransactionContextFactory(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    @Override
    public TransactionContext openTransactionContext(final CommandContext commandContext) {
        return new JtaTransactionContext(this.transactionManager);
    }
}
