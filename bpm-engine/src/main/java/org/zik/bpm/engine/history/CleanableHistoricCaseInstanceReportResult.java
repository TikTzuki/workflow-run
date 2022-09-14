// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface CleanableHistoricCaseInstanceReportResult
{
    String getCaseDefinitionId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionName();
    
    int getCaseDefinitionVersion();
    
    Integer getHistoryTimeToLive();
    
    long getFinishedCaseInstanceCount();
    
    long getCleanableCaseInstanceCount();
    
    String getTenantId();
}
