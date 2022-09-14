// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import javax.transaction.RollbackException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.sql.DbSqlSession;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.NotSupportedException;
import javax.transaction.Transaction;
import java.lang.reflect.UndeclaredThrowableException;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import javax.transaction.TransactionManager;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class JtaTransactionInterceptor extends CommandInterceptor
{
    protected static final CommandLogger LOG;
    protected final TransactionManager transactionManager;
    protected final boolean requiresNew;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    
    public JtaTransactionInterceptor(final TransactionManager transactionManager, final boolean requiresNew, final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.transactionManager = transactionManager;
        this.requiresNew = requiresNew;
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    @Override
    public <T> T execute(final Command<T> command) {
        Transaction oldTx = null;
        try {
            final boolean existing = this.isExisting();
            final boolean isNew = !existing || this.requiresNew;
            if (existing && this.requiresNew) {
                oldTx = this.doSuspend();
            }
            if (isNew) {
                this.doBegin();
            }
            T result;
            try {
                result = this.next.execute(command);
            }
            catch (RuntimeException ex) {
                this.doRollback(isNew);
                throw ex;
            }
            catch (Error err) {
                this.doRollback(isNew);
                throw err;
            }
            catch (Exception ex2) {
                this.doRollback(isNew);
                throw new UndeclaredThrowableException(ex2, "TransactionCallback threw undeclared checked exception");
            }
            if (isNew) {
                this.doCommit();
            }
            return result;
        }
        finally {
            this.doResume(oldTx);
        }
    }
    
    private void doBegin() {
        try {
            this.transactionManager.begin();
        }
        catch (NotSupportedException e) {
            throw new TransactionException("Unable to begin transaction", (Throwable)e);
        }
        catch (SystemException e2) {
            throw new TransactionException("Unable to begin transaction", (Throwable)e2);
        }
    }
    
    private boolean isExisting() {
        try {
            return this.transactionManager.getStatus() != 6;
        }
        catch (SystemException e) {
            throw new TransactionException("Unable to retrieve transaction status", (Throwable)e);
        }
    }
    
    private Transaction doSuspend() {
        try {
            return this.transactionManager.suspend();
        }
        catch (SystemException e) {
            throw new TransactionException("Unable to suspend transaction", (Throwable)e);
        }
    }
    
    private void doResume(final Transaction tx) {
        if (tx != null) {
            try {
                this.transactionManager.resume(tx);
            }
            catch (SystemException e) {
                throw new TransactionException("Unable to resume transaction", (Throwable)e);
            }
            catch (InvalidTransactionException e2) {
                throw new TransactionException("Unable to resume transaction", (Throwable)e2);
            }
        }
    }
    
    private void doCommit() {
        try {
            this.transactionManager.commit();
        }
        catch (HeuristicMixedException e) {
            throw new TransactionException("Unable to commit transaction", (Throwable)e);
        }
        catch (HeuristicRollbackException e2) {
            throw new TransactionException("Unable to commit transaction", (Throwable)e2);
        }
        catch (RollbackException e3) {
            if (DbSqlSession.isCrdbConcurrencyConflictOnCommit((Throwable)e3, this.processEngineConfiguration)) {
                throw ProcessEngineLogger.PERSISTENCE_LOGGER.crdbTransactionRetryExceptionOnCommit((Throwable)e3);
            }
            throw new TransactionException("Unable to commit transaction", (Throwable)e3);
        }
        catch (SystemException e4) {
            throw new TransactionException("Unable to commit transaction", (Throwable)e4);
        }
        catch (RuntimeException e5) {
            this.doRollback(true);
            throw e5;
        }
        catch (Error e6) {
            this.doRollback(true);
            throw e6;
        }
    }
    
    private void doRollback(final boolean isNew) {
        try {
            if (isNew) {
                this.transactionManager.rollback();
            }
            else {
                this.transactionManager.setRollbackOnly();
            }
        }
        catch (SystemException e) {
            JtaTransactionInterceptor.LOG.exceptionWhileRollingBackTransaction((Exception)e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
    
    public static class TransactionException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        
        private TransactionException() {
        }
        
        private TransactionException(final String s) {
            super(s);
        }
        
        private TransactionException(final String s, final Throwable throwable) {
            super(s, throwable);
        }
        
        private TransactionException(final Throwable throwable) {
            super(throwable);
        }
    }
}
