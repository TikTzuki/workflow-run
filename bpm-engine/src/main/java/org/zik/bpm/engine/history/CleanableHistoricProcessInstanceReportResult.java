// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface CleanableHistoricProcessInstanceReportResult
{
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionName();
    
    int getProcessDefinitionVersion();
    
    Integer getHistoryTimeToLive();
    
    long getFinishedProcessInstanceCount();
    
    long getCleanableProcessInstanceCount();
    
    String getTenantId();
}
