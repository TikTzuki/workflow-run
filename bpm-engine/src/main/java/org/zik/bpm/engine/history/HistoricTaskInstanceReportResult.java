// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface HistoricTaskInstanceReportResult
{
    Long getCount();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionName();
    
    String getTaskName();
    
    String getTenantId();
}
