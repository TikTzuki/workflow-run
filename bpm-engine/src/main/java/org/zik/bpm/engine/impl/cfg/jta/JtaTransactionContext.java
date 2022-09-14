// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.jta;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import javax.transaction.SystemException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import javax.transaction.Synchronization;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.zik.bpm.engine.impl.cfg.TransactionLogger;
import org.zik.bpm.engine.impl.cfg.TransactionContext;

public class JtaTransactionContext implements TransactionContext
{
    public static final TransactionLogger LOG;
    protected final TransactionManager transactionManager;
    
    public JtaTransactionContext(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    @Override
    public void commit() {
    }
    
    @Override
    public void rollback() {
        try {
            final Transaction transaction = this.getTransaction();
            final int status = transaction.getStatus();
            if (status != 6 && status != 4) {
                transaction.setRollbackOnly();
            }
        }
        catch (Exception e) {
            throw JtaTransactionContext.LOG.exceptionWhileInteractingWithTransaction("setting transaction rollback only", e);
        }
    }
    
    protected Transaction getTransaction() {
        try {
            return this.transactionManager.getTransaction();
        }
        catch (Exception e) {
            throw JtaTransactionContext.LOG.exceptionWhileInteractingWithTransaction("getting transaction", e);
        }
    }
    
    @Override
    public void addTransactionListener(final TransactionState transactionState, final TransactionListener transactionListener) {
        final Transaction transaction = this.getTransaction();
        final CommandContext commandContext = Context.getCommandContext();
        try {
            transaction.registerSynchronization((Synchronization)new TransactionStateSynchronization(transactionState, transactionListener, commandContext));
        }
        catch (Exception e) {
            throw JtaTransactionContext.LOG.exceptionWhileInteractingWithTransaction("registering synchronization", e);
        }
    }
    
    @Override
    public boolean isTransactionActive() {
        try {
            return this.transactionManager.getStatus() != 1 && this.transactionManager.getStatus() != 6;
        }
        catch (SystemException e) {
            throw JtaTransactionContext.LOG.exceptionWhileInteractingWithTransaction("getting transaction state", (Throwable)e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.TX_LOGGER;
    }
    
    public static class TransactionStateSynchronization implements Synchronization
    {
        protected final TransactionListener transactionListener;
        protected final TransactionState transactionState;
        private final CommandContext commandContext;
        
        public TransactionStateSynchronization(final TransactionState transactionState, final TransactionListener transactionListener, final CommandContext commandContext) {
            this.transactionState = transactionState;
            this.transactionListener = transactionListener;
            this.commandContext = commandContext;
        }
        
        public void beforeCompletion() {
            if (TransactionState.COMMITTING.equals(this.transactionState) || TransactionState.ROLLINGBACK.equals(this.transactionState)) {
                this.transactionListener.execute(this.commandContext);
            }
        }
        
        public void afterCompletion(final int status) {
            if (4 == status && TransactionState.ROLLED_BACK.equals(this.transactionState)) {
                this.transactionListener.execute(this.commandContext);
            }
            else if (3 == status && TransactionState.COMMITTED.equals(this.transactionState)) {
                this.transactionListener.execute(this.commandContext);
            }
        }
    }
}
