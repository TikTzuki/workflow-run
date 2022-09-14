// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.standalone;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.PersistenceSession;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import java.util.List;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionLogger;
import org.zik.bpm.engine.impl.cfg.TransactionContext;

public class StandaloneTransactionContext implements TransactionContext
{
    private static final TransactionLogger LOG;
    protected CommandContext commandContext;
    protected Map<TransactionState, List<TransactionListener>> stateTransactionListeners;
    private TransactionState lastTransactionState;
    
    public StandaloneTransactionContext(final CommandContext commandContext) {
        this.stateTransactionListeners = null;
        this.commandContext = commandContext;
    }
    
    @Override
    public void addTransactionListener(final TransactionState transactionState, final TransactionListener transactionListener) {
        if (this.stateTransactionListeners == null) {
            this.stateTransactionListeners = new HashMap<TransactionState, List<TransactionListener>>();
        }
        List<TransactionListener> transactionListeners = this.stateTransactionListeners.get(transactionState);
        if (transactionListeners == null) {
            transactionListeners = new ArrayList<TransactionListener>();
            this.stateTransactionListeners.put(transactionState, transactionListeners);
        }
        transactionListeners.add(transactionListener);
    }
    
    @Override
    public void commit() {
        StandaloneTransactionContext.LOG.debugTransactionOperation("firing event committing...");
        this.fireTransactionEvent(TransactionState.COMMITTING);
        StandaloneTransactionContext.LOG.debugTransactionOperation("committing the persistence session...");
        this.getPersistenceProvider().commit();
        StandaloneTransactionContext.LOG.debugTransactionOperation("firing event committed...");
        this.fireTransactionEvent(TransactionState.COMMITTED);
    }
    
    protected void fireTransactionEvent(final TransactionState transactionState) {
        this.setLastTransactionState(transactionState);
        if (this.stateTransactionListeners == null) {
            return;
        }
        final List<TransactionListener> transactionListeners = this.stateTransactionListeners.get(transactionState);
        if (transactionListeners == null) {
            return;
        }
        for (final TransactionListener transactionListener : transactionListeners) {
            transactionListener.execute(this.commandContext);
        }
    }
    
    protected void setLastTransactionState(final TransactionState transactionState) {
        this.lastTransactionState = transactionState;
    }
    
    private PersistenceSession getPersistenceProvider() {
        return this.commandContext.getSession(PersistenceSession.class);
    }
    
    @Override
    public void rollback() {
        try {
            try {
                StandaloneTransactionContext.LOG.debugTransactionOperation("firing event rollback...");
                this.fireTransactionEvent(TransactionState.ROLLINGBACK);
            }
            catch (Throwable exception) {
                StandaloneTransactionContext.LOG.exceptionWhileFiringEvent(TransactionState.ROLLINGBACK, exception);
                Context.getCommandInvocationContext().trySetThrowable(exception);
            }
            finally {
                StandaloneTransactionContext.LOG.debugTransactionOperation("rolling back the persistence session...");
                this.getPersistenceProvider().rollback();
            }
        }
        catch (Throwable exception) {
            StandaloneTransactionContext.LOG.exceptionWhileFiringEvent(TransactionState.ROLLINGBACK, exception);
            Context.getCommandInvocationContext().trySetThrowable(exception);
        }
        finally {
            StandaloneTransactionContext.LOG.debugFiringEventRolledBack();
            this.fireTransactionEvent(TransactionState.ROLLED_BACK);
        }
    }
    
    @Override
    public boolean isTransactionActive() {
        return !TransactionState.ROLLINGBACK.equals(this.lastTransactionState) && !TransactionState.ROLLED_BACK.equals(this.lastTransactionState);
    }
    
    static {
        LOG = ProcessEngineLogger.TX_LOGGER;
    }
}
