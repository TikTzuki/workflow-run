// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

public class DeploymentTableMapping implements MyBatisTableMapping
{
    @Override
    public String getTableName() {
        return "ACT_RE_DEPLOYMENT";
    }
    
    @Override
    public String getTableAlias() {
        return "DEP";
    }
    
    @Override
    public boolean isOneToOneRelation() {
        return true;
    }
}
