// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CreateMigrationPlanCmd;
import org.zik.bpm.engine.migration.MigrationPlan;
import java.util.HashMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.migration.MigrationPlanBuilder;
import java.util.Map;
import java.util.ArrayList;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.migration.MigrationInstructionsBuilder;
import org.zik.bpm.engine.migration.MigrationInstructionBuilder;

public class MigrationPlanBuilderImpl implements MigrationInstructionBuilder, MigrationInstructionsBuilder
{
    protected CommandExecutor commandExecutor;
    protected String sourceProcessDefinitionId;
    protected String targetProcessDefinitionId;
    protected List<MigrationInstructionImpl> explicitMigrationInstructions;
    protected boolean mapEqualActivities;
    protected boolean updateEventTriggersForGeneratedInstructions;
    protected VariableMap variables;
    
    public MigrationPlanBuilderImpl(final CommandExecutor commandExecutor, final String sourceProcessDefinitionId, final String targetProcessDefinitionId) {
        this.mapEqualActivities = false;
        this.updateEventTriggersForGeneratedInstructions = false;
        this.commandExecutor = commandExecutor;
        this.sourceProcessDefinitionId = sourceProcessDefinitionId;
        this.targetProcessDefinitionId = targetProcessDefinitionId;
        this.explicitMigrationInstructions = new ArrayList<MigrationInstructionImpl>();
    }
    
    @Override
    public MigrationInstructionsBuilder mapEqualActivities() {
        this.mapEqualActivities = true;
        return this;
    }
    
    @Override
    public MigrationPlanBuilder setVariables(final Map<String, ?> variables) {
        if (variables instanceof VariableMapImpl) {
            this.variables = (VariableMap)variables;
        }
        else if (variables != null) {
            this.variables = (VariableMap)new VariableMapImpl((Map)new HashMap(variables));
        }
        return this;
    }
    
    @Override
    public MigrationInstructionBuilder mapActivities(final String sourceActivityId, final String targetActivityId) {
        this.explicitMigrationInstructions.add(new MigrationInstructionImpl(sourceActivityId, targetActivityId));
        return this;
    }
    
    @Override
    public MigrationInstructionBuilder updateEventTrigger() {
        this.explicitMigrationInstructions.get(this.explicitMigrationInstructions.size() - 1).setUpdateEventTrigger(true);
        return this;
    }
    
    @Override
    public MigrationInstructionsBuilder updateEventTriggers() {
        this.updateEventTriggersForGeneratedInstructions = true;
        return this;
    }
    
    public String getSourceProcessDefinitionId() {
        return this.sourceProcessDefinitionId;
    }
    
    public String getTargetProcessDefinitionId() {
        return this.targetProcessDefinitionId;
    }
    
    public boolean isMapEqualActivities() {
        return this.mapEqualActivities;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public boolean isUpdateEventTriggersForGeneratedInstructions() {
        return this.updateEventTriggersForGeneratedInstructions;
    }
    
    public List<MigrationInstructionImpl> getExplicitMigrationInstructions() {
        return this.explicitMigrationInstructions;
    }
    
    @Override
    public MigrationPlan build() {
        return this.commandExecutor.execute((Command<MigrationPlan>)new CreateMigrationPlanCmd(this));
    }
}
