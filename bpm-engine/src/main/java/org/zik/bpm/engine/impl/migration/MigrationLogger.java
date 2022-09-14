// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.migration.MigratingProcessInstanceValidationReport;
import org.zik.bpm.engine.migration.MigratingProcessInstanceValidationException;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingProcessInstanceValidationReportImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.migration.MigrationPlanValidationReport;
import org.zik.bpm.engine.migration.MigrationPlanValidationException;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationPlanValidationReportImpl;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class MigrationLogger extends ProcessEngineLogger
{
    public MigrationPlanValidationException failingMigrationPlanValidation(final MigrationPlanValidationReportImpl validationReport) {
        final StringBuilder sb = new StringBuilder();
        validationReport.writeTo(sb);
        return new MigrationPlanValidationException(this.exceptionMessage("001", "{}", new Object[] { sb.toString() }), validationReport);
    }
    
    public ProcessEngineException processDefinitionOfInstanceDoesNotMatchMigrationPlan(final ExecutionEntity processInstance, final String processDefinitionId) {
        return new ProcessEngineException(this.exceptionMessage("002", "Process instance '{}' cannot be migrated. Its process definition '{}' does not match the source process definition of the migration plan '{}'", new Object[] { processInstance.getId(), processInstance.getProcessDefinitionId(), processDefinitionId }));
    }
    
    public ProcessEngineException processInstanceDoesNotExist(final String processInstanceId) {
        return new ProcessEngineException(this.exceptionMessage("003", "Process instance '{}' cannot be migrated. The process instance does not exist", new Object[] { processInstanceId }));
    }
    
    public MigratingProcessInstanceValidationException failingMigratingProcessInstanceValidation(final MigratingProcessInstanceValidationReportImpl validationReport) {
        final StringBuilder sb = new StringBuilder();
        validationReport.writeTo(sb);
        return new MigratingProcessInstanceValidationException(this.exceptionMessage("004", "{}", new Object[] { sb.toString() }), validationReport);
    }
    
    public ProcessEngineException cannotBecomeSubordinateInNonScope(final MigratingActivityInstance activityInstance) {
        return new ProcessEngineException(this.exceptionMessage("005", "{}", new Object[] { "Cannot attach a subordinate to activity instance '{}'. Activity '{}' is not a scope", activityInstance.getActivityInstance().getId(), activityInstance.getActivityInstance().getActivityId() }));
    }
    
    public ProcessEngineException cannotDestroySubordinateInNonScope(final MigratingActivityInstance activityInstance) {
        return new ProcessEngineException(this.exceptionMessage("006", "{}", new Object[] { "Cannot destroy a subordinate of activity instance '{}'. Activity '{}' is not a scope", activityInstance.getActivityInstance().getId(), activityInstance.getActivityInstance().getActivityId() }));
    }
    
    public ProcessEngineException cannotAttachToTransitionInstance(final MigratingInstance attachingInstance) {
        return new ProcessEngineException(this.exceptionMessage("007", "{}", new Object[] { "Cannot attach instance '{}' to a transition instance", attachingInstance }));
    }
    
    public BadUserRequestException processDefinitionDoesNotExist(final String processDefinitionId, final String type) {
        return new BadUserRequestException(this.exceptionMessage("008", "{} process definition with id '{}' does not exist", new Object[] { type, processDefinitionId }));
    }
    
    public ProcessEngineException cannotMigrateBetweenTenants(final String sourceTenantId, final String targetTenantId) {
        return new ProcessEngineException(this.exceptionMessage("09", "Cannot migrate process instances between processes of different tenants ('{}' != '{}')", new Object[] { sourceTenantId, targetTenantId }));
    }
    
    public ProcessEngineException cannotMigrateInstanceBetweenTenants(final String processInstanceId, final String sourceTenantId, final String targetTenantId) {
        String detailMessage = null;
        if (sourceTenantId != null) {
            detailMessage = this.exceptionMessage("010", "Cannot migrate process instance '{}' to a process definition of a different tenant ('{}' != '{}')", new Object[] { processInstanceId, sourceTenantId, targetTenantId });
        }
        else {
            detailMessage = this.exceptionMessage("010", "Cannot migrate process instance '{}' without tenant to a process definition with a tenant ('{}')", new Object[] { processInstanceId, targetTenantId });
        }
        return new ProcessEngineException(detailMessage);
    }
    
    public ProcessEngineException cannotHandleChild(final MigratingScopeInstance scopeInstance, final MigratingProcessElementInstance childCandidate) {
        return new ProcessEngineException(this.exceptionMessage("011", "Scope instance of type {} cannot have child of type {}", new Object[] { scopeInstance.getClass().getSimpleName(), childCandidate.getClass().getSimpleName() }));
    }
}
