// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

public class ProcessDefinitionTableMapping implements MyBatisTableMapping
{
    @Override
    public String getTableName() {
        return "ACT_RE_PROCDEF";
    }
    
    @Override
    public String getTableAlias() {
        return "P";
    }
    
    @Override
    public boolean isOneToOneRelation() {
        return true;
    }
}
