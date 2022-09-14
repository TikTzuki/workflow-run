// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager;

import org.zik.bpm.engine.impl.interceptor.Session;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;

public class DbEntityManagerFactory implements SessionFactory
{
    protected IdGenerator idGenerator;
    
    public DbEntityManagerFactory(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
    
    @Override
    public Class<?> getSessionType() {
        return DbEntityManager.class;
    }
    
    @Override
    public DbEntityManager openSession() {
        final PersistenceSession persistenceSession = Context.getCommandContext().getSession(PersistenceSession.class);
        return new DbEntityManager(this.idGenerator, persistenceSession);
    }
}
