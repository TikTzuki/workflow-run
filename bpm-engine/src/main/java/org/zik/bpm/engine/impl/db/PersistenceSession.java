// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Session;

public interface PersistenceSession extends Session
{
    FlushResult executeDbOperations(final List<DbOperation> p0);
    
    void flushOperations();
    
    List<?> selectList(final String p0, final Object p1);
    
     <T extends DbEntity> T selectById(final Class<T> p0, final String p1);
    
    Object selectOne(final String p0, final Object p1);
    
    void lock(final String p0, final Object p1);
    
    int executeNonEmptyUpdateStmt(final String p0, final Object p1);
    
    void commit();
    
    void rollback();
    
    void dbSchemaCheckVersion();
    
    void dbSchemaCreate();
    
    void dbSchemaDrop();
    
    void dbSchemaPrune();
    
    void dbSchemaUpdate();
    
    List<String> getTableNamesPresent();
    
    void addEntityLoadListener(final EntityLoadListener p0);
}
