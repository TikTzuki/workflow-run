// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.EngineUtilLogger;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstructionImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationInstructionValidationReportImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationInstructionValidator;
import org.zik.bpm.engine.migration.MigrationInstructionValidationReport;
import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstruction;
import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstructions;
import org.zik.bpm.engine.impl.migration.MigrationInstructionGenerator;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Iterator;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.migration.MigrationVariableValidationReport;
import java.text.MessageFormat;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationVariableValidationReportImpl;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationPlanValidationReportImpl;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.migration.MigrationPlanImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.migration.MigrationPlanBuilderImpl;
import org.zik.bpm.engine.impl.migration.MigrationLogger;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateMigrationPlanCmd implements Command<MigrationPlan>
{
    public static final MigrationLogger LOG;
    protected MigrationPlanBuilderImpl migrationBuilder;
    
    public CreateMigrationPlanCmd(final MigrationPlanBuilderImpl migrationPlanBuilderImpl) {
        this.migrationBuilder = migrationPlanBuilderImpl;
    }
    
    @Override
    public MigrationPlan execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity sourceProcessDefinition = this.getProcessDefinition(commandContext, this.migrationBuilder.getSourceProcessDefinitionId(), "Source");
        final ProcessDefinitionEntity targetProcessDefinition = this.getProcessDefinition(commandContext, this.migrationBuilder.getTargetProcessDefinitionId(), "Target");
        this.checkAuthorization(commandContext, sourceProcessDefinition, targetProcessDefinition);
        final MigrationPlanImpl migrationPlan = new MigrationPlanImpl(sourceProcessDefinition.getId(), targetProcessDefinition.getId());
        final List<MigrationInstruction> instructions = new ArrayList<MigrationInstruction>();
        if (this.migrationBuilder.isMapEqualActivities()) {
            instructions.addAll(this.generateInstructions(commandContext, sourceProcessDefinition, targetProcessDefinition, this.migrationBuilder.isUpdateEventTriggersForGeneratedInstructions()));
        }
        instructions.addAll(this.migrationBuilder.getExplicitMigrationInstructions());
        migrationPlan.setInstructions(instructions);
        final VariableMap variables = this.migrationBuilder.getVariables();
        if (variables != null) {
            migrationPlan.setVariables(variables);
        }
        this.validateMigration(commandContext, migrationPlan, sourceProcessDefinition, targetProcessDefinition);
        return migrationPlan;
    }
    
    protected void validateMigration(final CommandContext commandContext, final MigrationPlanImpl migrationPlan, final ProcessDefinitionEntity sourceProcessDefinition, final ProcessDefinitionEntity targetProcessDefinition) {
        final MigrationPlanValidationReportImpl planReport = new MigrationPlanValidationReportImpl(migrationPlan);
        final VariableMap variables = migrationPlan.getVariables();
        if (variables != null) {
            this.validateVariables(variables, planReport);
        }
        this.validateMigrationInstructions(commandContext, planReport, migrationPlan, sourceProcessDefinition, targetProcessDefinition);
        if (planReport.hasReports()) {
            throw CreateMigrationPlanCmd.LOG.failingMigrationPlanValidation(planReport);
        }
    }
    
    protected void validateVariables(final VariableMap variables, final MigrationPlanValidationReportImpl planReport) {
        final TypedValue valueTyped;
        final boolean javaSerializationProhibited;
        MigrationVariableValidationReportImpl report;
        String failureMessage;
        variables.keySet().forEach(name -> {
            valueTyped = variables.getValueTyped(name);
            javaSerializationProhibited = VariableUtil.isJavaSerializationProhibited(valueTyped);
            if (javaSerializationProhibited) {
                report = new MigrationVariableValidationReportImpl(valueTyped);
                failureMessage = MessageFormat.format("Cannot set variable with name {0}. Java serialization format is prohibited", name);
                report.addFailure(failureMessage);
                planReport.addVariableReport(name, report);
            }
        });
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String id, final String type) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, type + " process definition id", (Object)id);
        try {
            return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(id);
        }
        catch (NullValueException e) {
            throw CreateMigrationPlanCmd.LOG.processDefinitionDoesNotExist(id, type);
        }
    }
    
    protected void checkAuthorization(final CommandContext commandContext, final ProcessDefinitionEntity sourceProcessDefinition, final ProcessDefinitionEntity targetProcessDefinition) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateMigrationPlan(sourceProcessDefinition, targetProcessDefinition);
        }
    }
    
    protected List<MigrationInstruction> generateInstructions(final CommandContext commandContext, final ProcessDefinitionImpl sourceProcessDefinition, final ProcessDefinitionImpl targetProcessDefinition, final boolean updateEventTriggers) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        final MigrationInstructionGenerator migrationInstructionGenerator = processEngineConfiguration.getMigrationInstructionGenerator();
        final ValidatingMigrationInstructions generatedInstructions = migrationInstructionGenerator.generate(sourceProcessDefinition, targetProcessDefinition, updateEventTriggers);
        generatedInstructions.filterWith(processEngineConfiguration.getMigrationInstructionValidators());
        return generatedInstructions.asMigrationInstructions();
    }
    
    protected void validateMigrationInstructions(final CommandContext commandContext, final MigrationPlanValidationReportImpl planReport, final MigrationPlanImpl migrationPlan, final ProcessDefinitionImpl sourceProcessDefinition, final ProcessDefinitionImpl targetProcessDefinition) {
        final List<MigrationInstructionValidator> migrationInstructionValidators = commandContext.getProcessEngineConfiguration().getMigrationInstructionValidators();
        final ValidatingMigrationInstructions validatingMigrationInstructions = this.wrapMigrationInstructions(migrationPlan, sourceProcessDefinition, targetProcessDefinition, planReport);
        for (final ValidatingMigrationInstruction validatingMigrationInstruction : validatingMigrationInstructions.getInstructions()) {
            final MigrationInstructionValidationReportImpl instructionReport = this.validateInstruction(validatingMigrationInstruction, validatingMigrationInstructions, migrationInstructionValidators);
            if (instructionReport.hasFailures()) {
                planReport.addInstructionReport(instructionReport);
            }
        }
    }
    
    protected MigrationInstructionValidationReportImpl validateInstruction(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final List<MigrationInstructionValidator> migrationInstructionValidators) {
        final MigrationInstructionValidationReportImpl validationReport = new MigrationInstructionValidationReportImpl(instruction.toMigrationInstruction());
        for (final MigrationInstructionValidator migrationInstructionValidator : migrationInstructionValidators) {
            migrationInstructionValidator.validate(instruction, instructions, validationReport);
        }
        return validationReport;
    }
    
    protected ValidatingMigrationInstructions wrapMigrationInstructions(final MigrationPlan migrationPlan, final ProcessDefinitionImpl sourceProcessDefinition, final ProcessDefinitionImpl targetProcessDefinition, final MigrationPlanValidationReportImpl planReport) {
        final ValidatingMigrationInstructions validatingMigrationInstructions = new ValidatingMigrationInstructions();
        for (final MigrationInstruction migrationInstruction : migrationPlan.getInstructions()) {
            final MigrationInstructionValidationReportImpl instructionReport = new MigrationInstructionValidationReportImpl(migrationInstruction);
            final String sourceActivityId = migrationInstruction.getSourceActivityId();
            final String targetActivityId = migrationInstruction.getTargetActivityId();
            if (sourceActivityId != null && targetActivityId != null) {
                final ActivityImpl sourceActivity = sourceProcessDefinition.findActivity(sourceActivityId);
                final ActivityImpl targetActivity = targetProcessDefinition.findActivity(migrationInstruction.getTargetActivityId());
                if (sourceActivity != null && targetActivity != null) {
                    validatingMigrationInstructions.addInstruction(new ValidatingMigrationInstructionImpl(sourceActivity, targetActivity, migrationInstruction.isUpdateEventTrigger()));
                }
                else {
                    if (sourceActivity == null) {
                        instructionReport.addFailure("Source activity '" + sourceActivityId + "' does not exist");
                    }
                    if (targetActivity == null) {
                        instructionReport.addFailure("Target activity '" + targetActivityId + "' does not exist");
                    }
                }
            }
            else {
                if (sourceActivityId == null) {
                    instructionReport.addFailure("Source activity id is null");
                }
                if (targetActivityId == null) {
                    instructionReport.addFailure("Target activity id is null");
                }
            }
            if (instructionReport.hasFailures()) {
                planReport.addInstructionReport(instructionReport);
            }
        }
        return validatingMigrationInstructions;
    }
    
    static {
        LOG = EngineUtilLogger.MIGRATION_LOGGER;
    }
}
