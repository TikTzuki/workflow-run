// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import org.zik.bpm.engine.impl.interceptor.Session;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import javax.persistence.EntityManagerFactory;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;

public class EntityManagerSessionFactory implements SessionFactory
{
    protected EntityManagerFactory entityManagerFactory;
    protected boolean handleTransactions;
    protected boolean closeEntityManager;
    
    public EntityManagerSessionFactory(final Object entityManagerFactory, final boolean handleTransactions, final boolean closeEntityManager) {
        EnsureUtil.ensureNotNull("entityManagerFactory", entityManagerFactory);
        if (!(entityManagerFactory instanceof EntityManagerFactory)) {
            throw new ProcessEngineException("EntityManagerFactory must implement 'javax.persistence.EntityManagerFactory'");
        }
        this.entityManagerFactory = (EntityManagerFactory)entityManagerFactory;
        this.handleTransactions = handleTransactions;
        this.closeEntityManager = closeEntityManager;
    }
    
    @Override
    public Class<?> getSessionType() {
        return EntityManagerSession.class;
    }
    
    @Override
    public Session openSession() {
        return new EntityManagerSessionImpl(this.entityManagerFactory, this.handleTransactions, this.closeEntityManager);
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }
}
