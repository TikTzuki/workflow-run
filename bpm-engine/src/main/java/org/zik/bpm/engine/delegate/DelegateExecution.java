// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.zik.bpm.engine.runtime.Incident;

public interface DelegateExecution extends BaseDelegateExecution, BpmnModelExecutionContext, ProcessEngineServicesAware
{
    String getProcessInstanceId();
    
    String getProcessBusinessKey();
    
    void setProcessBusinessKey(final String p0);
    
    String getProcessDefinitionId();
    
    String getParentId();
    
    String getCurrentActivityId();
    
    String getCurrentActivityName();
    
    String getActivityInstanceId();
    
    String getParentActivityInstanceId();
    
    String getCurrentTransitionId();
    
    DelegateExecution getProcessInstance();
    
    DelegateExecution getSuperExecution();
    
    boolean isCanceled();
    
    String getTenantId();
    
    void setVariable(final String p0, final Object p1, final String p2);
    
    Incident createIncident(final String p0, final String p1);
    
    Incident createIncident(final String p0, final String p1, final String p2);
    
    void resolveIncident(final String p0);
}
