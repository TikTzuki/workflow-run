// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.camunda.bpm.engine.variable.value.TypedValue;

public interface VariableInstance
{
    String getId();
    
    String getName();
    
    String getTypeName();
    
    Object getValue();
    
    TypedValue getTypedValue();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getProcessDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getTaskId();
    
    String getBatchId();
    
    String getActivityInstanceId();
    
    String getErrorMessage();
    
    String getTenantId();
}
