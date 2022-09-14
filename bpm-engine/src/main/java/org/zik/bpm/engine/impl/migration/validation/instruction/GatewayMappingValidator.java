// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.InclusiveGatewayActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class GatewayMappingValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        if (this.isWaitStateGateway(targetActivity)) {
            this.validateIncomingSequenceFlows(instruction, instructions, report);
            this.validateParentScopeMigrates(instruction, instructions, report);
            this.validateSingleInstruction(instruction, instructions, report);
        }
    }
    
    protected void validateIncomingSequenceFlows(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        final int numSourceIncomingFlows = sourceActivity.getIncomingTransitions().size();
        final int numTargetIncomingFlows = targetActivity.getIncomingTransitions().size();
        if (numSourceIncomingFlows > numTargetIncomingFlows) {
            report.addFailure("The target gateway must have at least the same number of incoming sequence flows that the source gateway has");
        }
    }
    
    protected void validateParentScopeMigrates(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        final ScopeImpl flowScope = sourceActivity.getFlowScope();
        if (flowScope != flowScope.getProcessDefinition() && instructions.getInstructionsBySourceScope(flowScope).isEmpty()) {
            report.addFailure("The gateway's flow scope '" + flowScope.getId() + "' must be mapped");
        }
    }
    
    protected void validateSingleInstruction(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        final List<ValidatingMigrationInstruction> instructionsToTargetGateway = instructions.getInstructionsByTargetScope(targetActivity);
        if (instructionsToTargetGateway.size() > 1) {
            report.addFailure("Only one gateway can be mapped to gateway '" + targetActivity.getId() + "'");
        }
    }
    
    protected boolean isWaitStateGateway(final ActivityImpl activity) {
        final ActivityBehavior behavior = activity.getActivityBehavior();
        return behavior instanceof ParallelGatewayActivityBehavior || behavior instanceof InclusiveGatewayActivityBehavior;
    }
}
