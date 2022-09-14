// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface HistoricDecisionInputInstance
{
    String getId();
    
    String getDecisionInstanceId();
    
    String getClauseId();
    
    String getClauseName();
    
    String getTypeName();
    
    Object getValue();
    
    TypedValue getTypedValue();
    
    String getErrorMessage();
    
    Date getCreateTime();
    
    String getRootProcessInstanceId();
    
    Date getRemovalTime();
}
