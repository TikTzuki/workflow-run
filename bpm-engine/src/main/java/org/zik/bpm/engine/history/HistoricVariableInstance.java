// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface HistoricVariableInstance
{
    public static final String STATE_CREATED = "CREATED";
    public static final String STATE_DELETED = "DELETED";
    
    String getId();
    
    String getName();
    
    String getTypeName();
    
    Object getValue();
    
    TypedValue getTypedValue();
    
    @Deprecated
    String getVariableName();
    
    @Deprecated
    String getVariableTypeName();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    @Deprecated
    String getActivtyInstanceId();
    
    String getActivityInstanceId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getTaskId();
    
    String getErrorMessage();
    
    String getTenantId();
    
    String getState();
    
    Date getCreateTime();
    
    Date getRemovalTime();
}
