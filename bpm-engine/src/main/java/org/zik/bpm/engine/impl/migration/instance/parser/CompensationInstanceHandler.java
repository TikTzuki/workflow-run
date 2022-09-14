// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.List;
import java.util.Collection;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.migration.instance.MigratingCompensationEventSubscriptionInstance;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public class CompensationInstanceHandler implements MigratingInstanceParseHandler<EventSubscriptionEntity>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final EventSubscriptionEntity element) {
        MigratingProcessElementInstance migratingInstance;
        if (element.getConfiguration() != null) {
            migratingInstance = this.createMigratingEventScopeInstance(parseContext, element);
        }
        else {
            migratingInstance = this.createMigratingEventSubscriptionInstance(parseContext, element);
        }
        final ExecutionEntity owningExecution = element.getExecution();
        MigratingScopeInstance parentInstance = null;
        if (owningExecution.isEventScope()) {
            parentInstance = parseContext.getMigratingCompensationInstanceByExecutionId(owningExecution.getId());
        }
        else {
            parentInstance = parseContext.getMigratingActivityInstanceById(owningExecution.getParentActivityInstanceId());
        }
        migratingInstance.setParent(parentInstance);
    }
    
    protected MigratingProcessElementInstance createMigratingEventSubscriptionInstance(final MigratingInstanceParseContext parseContext, final EventSubscriptionEntity element) {
        final ActivityImpl compensationHandler = parseContext.getSourceProcessDefinition().findActivity(element.getActivityId());
        final MigrationInstruction migrationInstruction = this.getMigrationInstruction(parseContext, compensationHandler);
        ActivityImpl targetScope = null;
        if (migrationInstruction != null) {
            final ActivityImpl targetEventScope = (ActivityImpl)parseContext.getTargetActivity(migrationInstruction).getEventScope();
            targetScope = targetEventScope.findCompensationHandler();
        }
        final MigratingCompensationEventSubscriptionInstance migratingCompensationInstance = parseContext.getMigratingProcessInstance().addCompensationSubscriptionInstance(migrationInstruction, element, compensationHandler, targetScope);
        parseContext.consume(element);
        return migratingCompensationInstance;
    }
    
    protected MigratingProcessElementInstance createMigratingEventScopeInstance(final MigratingInstanceParseContext parseContext, final EventSubscriptionEntity element) {
        final ActivityImpl compensatingActivity = parseContext.getSourceProcessDefinition().findActivity(element.getActivityId());
        final MigrationInstruction migrationInstruction = this.getMigrationInstruction(parseContext, compensatingActivity);
        ActivityImpl eventSubscriptionTargetScope = null;
        if (migrationInstruction != null) {
            if (compensatingActivity.isCompensationHandler()) {
                final ActivityImpl targetEventScope = (ActivityImpl)parseContext.getTargetActivity(migrationInstruction).getEventScope();
                eventSubscriptionTargetScope = targetEventScope.findCompensationHandler();
            }
            else {
                eventSubscriptionTargetScope = parseContext.getTargetActivity(migrationInstruction);
            }
        }
        final ExecutionEntity eventScopeExecution = CompensationUtil.getCompensatingExecution(element);
        final MigrationInstruction eventScopeInstruction = parseContext.findSingleMigrationInstruction(eventScopeExecution.getActivityId());
        final ActivityImpl targetScope = parseContext.getTargetActivity(eventScopeInstruction);
        final MigratingEventScopeInstance migratingCompensationInstance = parseContext.getMigratingProcessInstance().addEventScopeInstance(eventScopeInstruction, eventScopeExecution, eventScopeExecution.getActivity(), targetScope, migrationInstruction, element, compensatingActivity, eventSubscriptionTargetScope);
        parseContext.consume(element);
        parseContext.submit(migratingCompensationInstance);
        this.parseDependentEntities(parseContext, migratingCompensationInstance);
        return migratingCompensationInstance;
    }
    
    protected MigrationInstruction getMigrationInstruction(final MigratingInstanceParseContext parseContext, final ActivityImpl activity) {
        if (activity.isCompensationHandler()) {
            final Properties compensationHandlerProperties = activity.getProperties();
            ActivityImpl eventTrigger = compensationHandlerProperties.get(BpmnProperties.COMPENSATION_BOUNDARY_EVENT);
            if (eventTrigger == null) {
                eventTrigger = compensationHandlerProperties.get(BpmnProperties.INITIAL_ACTIVITY);
            }
            return parseContext.findSingleMigrationInstruction(eventTrigger.getActivityId());
        }
        return parseContext.findSingleMigrationInstruction(activity.getActivityId());
    }
    
    protected void parseDependentEntities(final MigratingInstanceParseContext parseContext, final MigratingEventScopeInstance migratingInstance) {
        final ExecutionEntity representativeExecution = migratingInstance.resolveRepresentativeExecution();
        final List<VariableInstanceEntity> variables = new ArrayList<VariableInstanceEntity>(representativeExecution.getVariablesInternal());
        parseContext.handleDependentVariables(migratingInstance, variables);
    }
}
