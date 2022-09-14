// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import org.zik.bpm.engine.impl.cfg.TransactionContext;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import org.zik.bpm.engine.ProcessEngineException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerSessionImpl implements EntityManagerSession
{
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private boolean handleTransactions;
    private boolean closeEntityManager;
    
    public EntityManagerSessionImpl(final EntityManagerFactory entityManagerFactory, final EntityManager entityManager, final boolean handleTransactions, final boolean closeEntityManager) {
        this(entityManagerFactory, handleTransactions, closeEntityManager);
        this.entityManager = entityManager;
    }
    
    public EntityManagerSessionImpl(final EntityManagerFactory entityManagerFactory, final boolean handleTransactions, final boolean closeEntityManager) {
        this.entityManagerFactory = entityManagerFactory;
        this.handleTransactions = handleTransactions;
        this.closeEntityManager = closeEntityManager;
    }
    
    @Override
    public void flush() {
        if (this.entityManager != null) {
            if (this.handleTransactions) {
                if (!this.isTransactionActive()) {
                    return;
                }
            }
            try {
                this.entityManager.flush();
            }
            catch (IllegalStateException ise) {
                throw new ProcessEngineException("Error while flushing EntityManager, illegal state", ise);
            }
            catch (TransactionRequiredException tre) {
                throw new ProcessEngineException("Cannot flush EntityManager, an active transaction is required", (Throwable)tre);
            }
            catch (PersistenceException pe) {
                throw new ProcessEngineException("Error while flushing EntityManager: " + pe.getMessage(), (Throwable)pe);
            }
        }
    }
    
    protected boolean isTransactionActive() {
        return this.handleTransactions && this.entityManager.getTransaction() != null && this.entityManager.getTransaction().isActive();
    }
    
    @Override
    public void close() {
        if (this.closeEntityManager && this.entityManager != null && !this.entityManager.isOpen()) {
            try {
                this.entityManager.close();
            }
            catch (IllegalStateException ise) {
                throw new ProcessEngineException("Error while closing EntityManager, may have already been closed or it is container-managed", ise);
            }
        }
    }
    
    @Override
    public EntityManager getEntityManager() {
        if (this.entityManager == null) {
            this.entityManager = this.getEntityManagerFactory().createEntityManager();
            if (this.handleTransactions) {
                final TransactionListener jpaTransactionCommitListener = new TransactionListener() {
                    @Override
                    public void execute(final CommandContext commandContext) {
                        if (EntityManagerSessionImpl.this.isTransactionActive()) {
                            EntityManagerSessionImpl.this.entityManager.getTransaction().commit();
                        }
                    }
                };
                final TransactionListener jpaTransactionRollbackListener = new TransactionListener() {
                    @Override
                    public void execute(final CommandContext commandContext) {
                        if (EntityManagerSessionImpl.this.isTransactionActive()) {
                            EntityManagerSessionImpl.this.entityManager.getTransaction().rollback();
                        }
                    }
                };
                final TransactionContext transactionContext = Context.getCommandContext().getTransactionContext();
                transactionContext.addTransactionListener(TransactionState.COMMITTED, jpaTransactionCommitListener);
                transactionContext.addTransactionListener(TransactionState.ROLLED_BACK, jpaTransactionRollbackListener);
                if (!this.isTransactionActive()) {
                    this.entityManager.getTransaction().begin();
                }
            }
        }
        return this.entityManager;
    }
    
    private EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }
}
