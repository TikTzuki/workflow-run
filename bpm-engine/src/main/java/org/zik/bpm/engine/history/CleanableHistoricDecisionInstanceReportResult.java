// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface CleanableHistoricDecisionInstanceReportResult
{
    String getDecisionDefinitionId();
    
    String getDecisionDefinitionKey();
    
    String getDecisionDefinitionName();
    
    int getDecisionDefinitionVersion();
    
    Integer getHistoryTimeToLive();
    
    long getFinishedDecisionInstanceCount();
    
    long getCleanableDecisionInstanceCount();
    
    String getTenantId();
}
