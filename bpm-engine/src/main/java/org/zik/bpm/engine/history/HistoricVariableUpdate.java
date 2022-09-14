// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.camunda.bpm.engine.variable.value.TypedValue;

public interface HistoricVariableUpdate extends HistoricDetail
{
    String getVariableName();
    
    String getVariableInstanceId();
    
    String getTypeName();
    
    @Deprecated
    String getVariableTypeName();
    
    Object getValue();
    
    TypedValue getTypedValue();
    
    int getRevision();
    
    String getErrorMessage();
    
    Boolean isInitial();
}
