// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

public class CaseDefinitionTableMapping implements MyBatisTableMapping
{
    @Override
    public String getTableName() {
        return "ACT_RE_CASEDEF";
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
