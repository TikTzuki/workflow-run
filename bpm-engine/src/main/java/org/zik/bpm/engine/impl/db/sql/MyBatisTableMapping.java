// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

public interface MyBatisTableMapping
{
    String getTableName();
    
    String getTableAlias();
    
    boolean isOneToOneRelation();
}
