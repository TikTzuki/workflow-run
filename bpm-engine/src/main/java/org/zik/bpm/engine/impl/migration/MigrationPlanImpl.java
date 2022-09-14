// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import java.util.ArrayList;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationPlan;

public class MigrationPlanImpl implements MigrationPlan
{
    protected String sourceProcessDefinitionId;
    protected String targetProcessDefinitionId;
    protected List<MigrationInstruction> instructions;
    protected VariableMap variables;
    
    public MigrationPlanImpl(final String sourceProcessDefinitionId, final String targetProcessDefinitionId) {
        this.sourceProcessDefinitionId = sourceProcessDefinitionId;
        this.targetProcessDefinitionId = targetProcessDefinitionId;
        this.instructions = new ArrayList<MigrationInstruction>();
    }
    
    @Override
    public String getSourceProcessDefinitionId() {
        return this.sourceProcessDefinitionId;
    }
    
    public void setSourceProcessDefinitionId(final String sourceProcessDefinitionId) {
        this.sourceProcessDefinitionId = sourceProcessDefinitionId;
    }
    
    @Override
    public String getTargetProcessDefinitionId() {
        return this.targetProcessDefinitionId;
    }
    
    @Override
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public void setVariables(final VariableMap variables) {
        this.variables = variables;
    }
    
    public void setTargetProcessDefinitionId(final String targetProcessDefinitionId) {
        this.targetProcessDefinitionId = targetProcessDefinitionId;
    }
    
    @Override
    public List<MigrationInstruction> getInstructions() {
        return this.instructions;
    }
    
    public void setInstructions(final List<MigrationInstruction> instructions) {
        this.instructions = instructions;
    }
    
    @Override
    public String toString() {
        return "MigrationPlan[sourceProcessDefinitionId='" + this.sourceProcessDefinitionId + '\'' + ", targetProcessDefinitionId='" + this.targetProcessDefinitionId + '\'' + ", instructions=" + this.instructions + ']';
    }
}
