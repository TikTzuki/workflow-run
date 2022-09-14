// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

public class VariableTableMapping implements MyBatisTableMapping
{
    @Override
    public String getTableName() {
        return "ACT_RU_VARIABLE";
    }
    
    @Override
    public String getTableAlias() {
        return "V";
    }
    
    @Override
    public boolean isOneToOneRelation() {
        return false;
    }
}
