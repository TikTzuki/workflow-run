// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import org.camunda.bpm.engine.variable.VariableMap;
import java.util.List;

public interface MigrationPlan
{
    List<MigrationInstruction> getInstructions();
    
    String getSourceProcessDefinitionId();
    
    String getTargetProcessDefinitionId();
    
    VariableMap getVariables();
}
