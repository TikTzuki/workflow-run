// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.migration.instance.MigrationCompensationInstanceVisitor;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstanceVisitor;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstanceTopDownWalker;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingTransitionInstanceValidationReportImpl;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingCompensationInstanceValidator;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingTransitionInstanceValidator;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingActivityInstanceValidator;
import java.util.List;
import org.zik.bpm.engine.migration.MigratingTransitionInstanceValidationReport;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import org.zik.bpm.engine.migration.MigratingActivityInstanceValidationReport;
import org.zik.bpm.engine.impl.migration.instance.MigratingCompensationEventSubscriptionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingActivityInstanceValidationReportImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstanceBottomUpWalker;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.impl.migration.instance.DeleteUnmappedInstanceVisitor;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cmd.SetExecutionVariablesCmd;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParser;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingProcessInstanceValidationReportImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.migration.MigrationPlan;
import java.util.Map;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class MigrateProcessInstanceCmd extends AbstractMigrationCmd implements Command<Void>
{
    protected static final MigrationLogger LOGGER;
    protected boolean skipJavaSerializationFormatCheck;
    
    public MigrateProcessInstanceCmd(final MigrationPlanExecutionBuilderImpl migrationPlanExecutionBuilder, final boolean skipJavaSerializationFormatCheck) {
        super(migrationPlanExecutionBuilder);
        this.skipJavaSerializationFormatCheck = skipJavaSerializationFormatCheck;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final MigrationPlan migrationPlan = this.executionBuilder.getMigrationPlan();
        final Collection<String> processInstanceIds = this.collectProcessInstanceIds();
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Migration plan cannot be null", "migration plan", migrationPlan);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot empty", "process instance ids", processInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "process instance ids", processInstanceIds);
        final ProcessDefinitionEntity sourceDefinition = this.resolveSourceProcessDefinition(commandContext);
        final ProcessDefinitionEntity targetDefinition = this.resolveTargetProcessDefinition(commandContext);
        this.checkAuthorizations(commandContext, sourceDefinition, targetDefinition);
        this.writeUserOperationLog(commandContext, sourceDefinition, targetDefinition, processInstanceIds.size(), (Map<String, Object>)migrationPlan.getVariables(), false);
        final Collection<String> collection;
        final Iterator<String> iterator;
        String processInstanceId;
        final MigrationPlan migrationPlan2;
        final ProcessDefinitionEntity targetProcessDefinition;
        commandContext.runWithoutAuthorization(() -> {
            collection.iterator();
            while (iterator.hasNext()) {
                processInstanceId = iterator.next();
                this.migrateProcessInstance(commandContext, processInstanceId, migrationPlan2, targetProcessDefinition, this.skipJavaSerializationFormatCheck);
            }
            return null;
        });
        return null;
    }
    
    public Void migrateProcessInstance(final CommandContext commandContext, final String processInstanceId, final MigrationPlan migrationPlan, final ProcessDefinitionEntity targetProcessDefinition, final boolean skipJavaSerializationFormatCheck) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process instance id cannot be null", "process instance id", processInstanceId);
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(processInstanceId);
        this.ensureProcessInstanceExist(processInstanceId, processInstance);
        this.ensureOperationAllowed(commandContext, processInstance, targetProcessDefinition);
        this.ensureSameProcessDefinition(processInstance, migrationPlan.getSourceProcessDefinitionId());
        final MigratingProcessInstanceValidationReportImpl processInstanceReport = new MigratingProcessInstanceValidationReportImpl();
        final ProcessEngineImpl processEngine = commandContext.getProcessEngineConfiguration().getProcessEngine();
        final MigratingInstanceParser migratingInstanceParser = new MigratingInstanceParser(processEngine);
        final MigratingProcessInstance migratingProcessInstance = migratingInstanceParser.parse(processInstanceId, migrationPlan, processInstanceReport);
        this.validateInstructions(commandContext, migratingProcessInstance, processInstanceReport);
        if (processInstanceReport.hasFailures()) {
            throw MigrateProcessInstanceCmd.LOGGER.failingMigratingProcessInstanceValidation(processInstanceReport);
        }
        this.executeInContext(() -> this.deleteUnmappedActivityInstances(migratingProcessInstance), migratingProcessInstance.getSourceDefinition());
        this.executeInContext(() -> this.migrateProcessInstance(migratingProcessInstance), migratingProcessInstance.getTargetDefinition());
        final Map<String, ?> variables = (Map<String, ?>)migrationPlan.getVariables();
        if (variables != null) {
            commandContext.executeWithOperationLogPrevented((Command<Object>)new SetExecutionVariablesCmd(processInstanceId, variables, false, skipJavaSerializationFormatCheck));
        }
        return null;
    }
    
    protected <T> void executeInContext(final Runnable runnable, final ProcessDefinitionEntity contextDefinition) {
        ProcessApplicationContextUtil.doContextSwitch(runnable, contextDefinition);
    }
    
    protected void deleteUnmappedActivityInstances(final MigratingProcessInstance migratingProcessInstance) {
        final boolean isSkipCustomListeners = this.executionBuilder.isSkipCustomListeners();
        final boolean isSkipIoMappings = this.executionBuilder.isSkipIoMappings();
        final DeleteUnmappedInstanceVisitor visitor = new DeleteUnmappedInstanceVisitor(isSkipCustomListeners, isSkipIoMappings);
        final Set<MigratingScopeInstance> leafInstances = this.collectLeafInstances(migratingProcessInstance);
        for (final MigratingScopeInstance leafInstance : leafInstances) {
            final MigratingScopeInstanceBottomUpWalker walker = new MigratingScopeInstanceBottomUpWalker(leafInstance);
            walker.addPreVisitor(visitor);
            final DeleteUnmappedInstanceVisitor deleteUnmappedInstanceVisitor;
            walker.walkUntil(element -> element == null || !deleteUnmappedInstanceVisitor.hasVisitedAll(element.getChildScopeInstances()));
        }
    }
    
    protected Set<MigratingScopeInstance> collectLeafInstances(final MigratingProcessInstance migratingProcessInstance) {
        final Set<MigratingScopeInstance> leafInstances = new HashSet<MigratingScopeInstance>();
        final Collection<MigratingScopeInstance> migratingScopeInstances = migratingProcessInstance.getMigratingScopeInstances();
        for (final MigratingScopeInstance migratingScopeInstance : migratingScopeInstances) {
            if (migratingScopeInstance.getChildScopeInstances().isEmpty()) {
                leafInstances.add(migratingScopeInstance);
            }
        }
        return leafInstances;
    }
    
    protected void validateInstructions(final CommandContext commandContext, final MigratingProcessInstance migratingProcessInstance, final MigratingProcessInstanceValidationReportImpl processInstanceReport) {
        final List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators = commandContext.getProcessEngineConfiguration().getMigratingActivityInstanceValidators();
        final List<MigratingTransitionInstanceValidator> migratingTransitionInstanceValidators = commandContext.getProcessEngineConfiguration().getMigratingTransitionInstanceValidators();
        final List<MigratingCompensationInstanceValidator> migratingCompensationInstanceValidators = commandContext.getProcessEngineConfiguration().getMigratingCompensationInstanceValidators();
        final Map<MigratingActivityInstance, MigratingActivityInstanceValidationReportImpl> instanceReports = new HashMap<MigratingActivityInstance, MigratingActivityInstanceValidationReportImpl>();
        final Collection<MigratingActivityInstance> migratingActivityInstances = migratingProcessInstance.getMigratingActivityInstances();
        for (final MigratingActivityInstance migratingActivityInstance : migratingActivityInstances) {
            final MigratingActivityInstanceValidationReportImpl instanceReport = this.validateActivityInstance(migratingActivityInstance, migratingProcessInstance, migratingActivityInstanceValidators);
            instanceReports.put(migratingActivityInstance, instanceReport);
        }
        final Collection<MigratingEventScopeInstance> migratingEventScopeInstances = migratingProcessInstance.getMigratingEventScopeInstances();
        for (final MigratingEventScopeInstance migratingEventScopeInstance : migratingEventScopeInstances) {
            final MigratingActivityInstance ancestorInstance = migratingEventScopeInstance.getClosestAncestorActivityInstance();
            this.validateEventScopeInstance(migratingEventScopeInstance, migratingProcessInstance, migratingCompensationInstanceValidators, instanceReports.get(ancestorInstance));
        }
        for (final MigratingCompensationEventSubscriptionInstance migratingEventSubscriptionInstance : migratingProcessInstance.getMigratingCompensationSubscriptionInstances()) {
            final MigratingActivityInstance ancestorInstance = migratingEventSubscriptionInstance.getClosestAncestorActivityInstance();
            this.validateCompensateSubscriptionInstance(migratingEventSubscriptionInstance, migratingProcessInstance, migratingCompensationInstanceValidators, instanceReports.get(ancestorInstance));
        }
        final Iterator<MigratingActivityInstanceValidationReportImpl> iterator4 = instanceReports.values().iterator();
        while (iterator4.hasNext()) {
            final MigratingActivityInstanceValidationReportImpl instanceReport = iterator4.next();
            if (instanceReport.hasFailures()) {
                processInstanceReport.addActivityInstanceReport(instanceReport);
            }
        }
        final Collection<MigratingTransitionInstance> migratingTransitionInstances = migratingProcessInstance.getMigratingTransitionInstances();
        for (final MigratingTransitionInstance migratingTransitionInstance : migratingTransitionInstances) {
            final MigratingTransitionInstanceValidationReportImpl instanceReport2 = this.validateTransitionInstance(migratingTransitionInstance, migratingProcessInstance, migratingTransitionInstanceValidators);
            if (instanceReport2.hasFailures()) {
                processInstanceReport.addTransitionInstanceReport(instanceReport2);
            }
        }
    }
    
    protected MigratingActivityInstanceValidationReportImpl validateActivityInstance(final MigratingActivityInstance migratingActivityInstance, final MigratingProcessInstance migratingProcessInstance, final List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators) {
        final MigratingActivityInstanceValidationReportImpl instanceReport = new MigratingActivityInstanceValidationReportImpl(migratingActivityInstance);
        for (final MigratingActivityInstanceValidator migratingActivityInstanceValidator : migratingActivityInstanceValidators) {
            migratingActivityInstanceValidator.validate(migratingActivityInstance, migratingProcessInstance, instanceReport);
        }
        return instanceReport;
    }
    
    protected MigratingTransitionInstanceValidationReportImpl validateTransitionInstance(final MigratingTransitionInstance migratingTransitionInstance, final MigratingProcessInstance migratingProcessInstance, final List<MigratingTransitionInstanceValidator> migratingTransitionInstanceValidators) {
        final MigratingTransitionInstanceValidationReportImpl instanceReport = new MigratingTransitionInstanceValidationReportImpl(migratingTransitionInstance);
        for (final MigratingTransitionInstanceValidator migratingTransitionInstanceValidator : migratingTransitionInstanceValidators) {
            migratingTransitionInstanceValidator.validate(migratingTransitionInstance, migratingProcessInstance, instanceReport);
        }
        return instanceReport;
    }
    
    protected void validateEventScopeInstance(final MigratingEventScopeInstance eventScopeInstance, final MigratingProcessInstance migratingProcessInstance, final List<MigratingCompensationInstanceValidator> migratingTransitionInstanceValidators, final MigratingActivityInstanceValidationReportImpl instanceReport) {
        for (final MigratingCompensationInstanceValidator validator : migratingTransitionInstanceValidators) {
            validator.validate(eventScopeInstance, migratingProcessInstance, instanceReport);
        }
    }
    
    protected void validateCompensateSubscriptionInstance(final MigratingCompensationEventSubscriptionInstance eventSubscriptionInstance, final MigratingProcessInstance migratingProcessInstance, final List<MigratingCompensationInstanceValidator> migratingTransitionInstanceValidators, final MigratingActivityInstanceValidationReportImpl instanceReport) {
        for (final MigratingCompensationInstanceValidator validator : migratingTransitionInstanceValidators) {
            validator.validate(eventSubscriptionInstance, migratingProcessInstance, instanceReport);
        }
    }
    
    protected void migrateProcessInstance(final MigratingProcessInstance migratingProcessInstance) {
        final MigratingActivityInstance rootActivityInstance = migratingProcessInstance.getRootInstance();
        final MigratingProcessElementInstanceTopDownWalker walker = new MigratingProcessElementInstanceTopDownWalker(rootActivityInstance);
        walker.addPreVisitor(new MigratingActivityInstanceVisitor(this.executionBuilder.isSkipCustomListeners(), this.executionBuilder.isSkipIoMappings()));
        walker.addPreVisitor(new MigrationCompensationInstanceVisitor());
        walker.walkUntil();
    }
    
    protected void ensureProcessInstanceExist(final String processInstanceId, final ExecutionEntity processInstance) {
        if (processInstance == null) {
            throw MigrateProcessInstanceCmd.LOGGER.processInstanceDoesNotExist(processInstanceId);
        }
    }
    
    protected void ensureSameProcessDefinition(final ExecutionEntity processInstance, final String processDefinitionId) {
        if (!processDefinitionId.equals(processInstance.getProcessDefinitionId())) {
            throw MigrateProcessInstanceCmd.LOGGER.processDefinitionOfInstanceDoesNotMatchMigrationPlan(processInstance, processDefinitionId);
        }
    }
    
    protected void ensureOperationAllowed(final CommandContext commandContext, final ExecutionEntity processInstance, final ProcessDefinitionEntity targetProcessDefinition) {
        final List<CommandChecker> commandCheckers = commandContext.getProcessEngineConfiguration().getCommandCheckers();
        for (final CommandChecker checker : commandCheckers) {
            checker.checkMigrateProcessInstance(processInstance, targetProcessDefinition);
        }
    }
    
    static {
        LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
