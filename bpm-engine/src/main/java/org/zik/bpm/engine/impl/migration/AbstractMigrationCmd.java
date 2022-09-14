// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import java.util.HashSet;
import java.util.Collection;
import org.zik.bpm.engine.impl.db.CompositePermissionCheck;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.db.PermissionCheckBuilder;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public abstract class AbstractMigrationCmd
{
    protected MigrationPlanExecutionBuilderImpl executionBuilder;
    
    public AbstractMigrationCmd(final MigrationPlanExecutionBuilderImpl executionBuilder) {
        this.executionBuilder = executionBuilder;
    }
    
    protected void checkAuthorizations(final CommandContext commandContext, final ProcessDefinitionEntity sourceDefinition, final ProcessDefinitionEntity targetDefinition) {
        final CompositePermissionCheck migrateInstanceCheck = new PermissionCheckBuilder().conjunctive().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, sourceDefinition.getKey(), Permissions.MIGRATE_INSTANCE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, targetDefinition.getKey(), Permissions.MIGRATE_INSTANCE).build();
        commandContext.getAuthorizationManager().checkAuthorization(migrateInstanceCheck);
    }
    
    protected Collection<String> collectProcessInstanceIds() {
        final Set<String> collectedProcessInstanceIds = new HashSet<String>();
        final List<String> processInstanceIds = this.executionBuilder.getProcessInstanceIds();
        if (processInstanceIds != null) {
            collectedProcessInstanceIds.addAll(processInstanceIds);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.executionBuilder.getProcessInstanceQuery();
        if (processInstanceQuery != null) {
            collectedProcessInstanceIds.addAll(processInstanceQuery.listIds());
        }
        return collectedProcessInstanceIds;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final ProcessDefinitionEntity sourceProcessDefinition, final ProcessDefinitionEntity targetProcessDefinition, final int numInstances, final Map<String, Object> variables, final boolean async) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("processDefinitionId", sourceProcessDefinition.getId(), targetProcessDefinition.getId()));
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        if (variables != null) {
            propertyChanges.add(new PropertyChange("nrOfSetVariables", null, variables.size()));
        }
        propertyChanges.add(new PropertyChange("async", null, async));
        commandContext.getOperationLogManager().logProcessInstanceOperation("Migrate", null, sourceProcessDefinition.getId(), sourceProcessDefinition.getKey(), propertyChanges);
    }
    
    protected ProcessDefinitionEntity resolveSourceProcessDefinition(final CommandContext commandContext) {
        final String sourceProcessDefinitionId = this.executionBuilder.getMigrationPlan().getSourceProcessDefinitionId();
        final ProcessDefinitionEntity sourceProcessDefinition = this.getProcessDefinition(commandContext, sourceProcessDefinitionId);
        EnsureUtil.ensureNotNull("sourceProcessDefinition", sourceProcessDefinition);
        return sourceProcessDefinition;
    }
    
    protected ProcessDefinitionEntity resolveTargetProcessDefinition(final CommandContext commandContext) {
        final String targetProcessDefinitionId = this.executionBuilder.getMigrationPlan().getTargetProcessDefinitionId();
        final ProcessDefinitionEntity sourceProcessDefinition = this.getProcessDefinition(commandContext, targetProcessDefinitionId);
        EnsureUtil.ensureNotNull("sourceProcessDefinition", sourceProcessDefinition);
        return sourceProcessDefinition;
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
}
