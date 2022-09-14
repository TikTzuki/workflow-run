// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import java.util.List;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class SameEventScopeInstructionValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        if (this.isCompensationBoundaryEvent(sourceActivity)) {
            return;
        }
        final ScopeImpl sourceEventScope = instruction.getSourceActivity().getEventScope();
        final ScopeImpl targetEventScope = instruction.getTargetActivity().getEventScope();
        if (sourceEventScope == null || sourceEventScope == sourceActivity.getFlowScope()) {
            return;
        }
        if (targetEventScope == null) {
            if (!this.isUserTaskWithTimeoutListener(sourceActivity)) {
                report.addFailure("The source activity's event scope (" + sourceEventScope.getId() + ") must be mapped but the target activity has no event scope");
            }
        }
        else {
            final ScopeImpl mappedSourceEventScope = this.findMappedEventScope(sourceEventScope, instruction, instructions);
            if (mappedSourceEventScope == null || !mappedSourceEventScope.getId().equals(targetEventScope.getId())) {
                report.addFailure("The source activity's event scope (" + sourceEventScope.getId() + ") must be mapped to the target activity's event scope (" + targetEventScope.getId() + ")");
            }
        }
    }
    
    protected boolean isCompensationBoundaryEvent(final ActivityImpl sourceActivity) {
        final String activityType = sourceActivity.getProperties().get(BpmnProperties.TYPE);
        return "compensationBoundaryCatch".equals(activityType);
    }
    
    protected boolean isUserTaskWithTimeoutListener(final ActivityImpl sourceActivity) {
        return "userTask".equals(sourceActivity.getProperties().get(BpmnProperties.TYPE)) && sourceActivity.isScope() && sourceActivity.equals(sourceActivity.getEventScope()) && sourceActivity.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS) != null && !sourceActivity.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS).isEmpty();
    }
    
    protected ScopeImpl findMappedEventScope(final ScopeImpl sourceEventScope, final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions) {
        if (sourceEventScope != null) {
            if (sourceEventScope == sourceEventScope.getProcessDefinition()) {
                return instruction.getTargetActivity().getProcessDefinition();
            }
            final List<ValidatingMigrationInstruction> eventScopeInstructions = instructions.getInstructionsBySourceScope(sourceEventScope);
            if (eventScopeInstructions.size() > 0) {
                return eventScopeInstructions.get(0).getTargetActivity();
            }
        }
        return null;
    }
    
    protected void addFailure(final ValidatingMigrationInstruction instruction, final MigrationInstructionValidationReportImpl report, final String sourceScopeId, final String targetScopeId) {
        report.addFailure("The source activity's event scope (" + sourceScopeId + ") must be mapped to the target activity's event scope (" + targetScopeId + ")");
    }
}
