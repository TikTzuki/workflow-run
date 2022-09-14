// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.Session;
import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;

public class DbSqlPersistenceProviderFactory implements SessionFactory
{
    @Override
    public Class<?> getSessionType() {
        return PersistenceSession.class;
    }
    
    @Override
    public Session openSession() {
        return Context.getCommandContext().getDbSqlSession();
    }
}
