// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.List;
import java.util.Date;

public interface HistoricDecisionInstance
{
    String getId();
    
    String getDecisionDefinitionId();
    
    String getDecisionDefinitionKey();
    
    String getDecisionDefinitionName();
    
    Date getEvaluationTime();
    
    Date getRemovalTime();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getProcessInstanceId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getActivityId();
    
    String getActivityInstanceId();
    
    String getUserId();
    
    List<HistoricDecisionInputInstance> getInputs();
    
    List<HistoricDecisionOutputInstance> getOutputs();
    
    Double getCollectResultValue();
    
    String getRootDecisionInstanceId();
    
    String getRootProcessInstanceId();
    
    String getDecisionRequirementsDefinitionId();
    
    String getDecisionRequirementsDefinitionKey();
    
    String getTenantId();
}
