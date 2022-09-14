// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class AdditionalFlowScopeInstructionValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ValidatingMigrationInstruction ancestorScopeInstruction = this.getClosestPreservedAncestorScopeMigrationInstruction(instruction, instructions);
        final ScopeImpl targetScope = instruction.getTargetActivity();
        if (ancestorScopeInstruction != null && targetScope != null && targetScope != targetScope.getProcessDefinition()) {
            final ScopeImpl parentInstanceTargetScope = ancestorScopeInstruction.getTargetActivity();
            if (parentInstanceTargetScope != null && !parentInstanceTargetScope.isAncestorFlowScopeOf(targetScope)) {
                report.addFailure("The closest mapped ancestor '" + ancestorScopeInstruction.getSourceActivity().getId() + "' is mapped to scope '" + parentInstanceTargetScope.getId() + "' which is not an ancestor of target scope '" + targetScope.getId() + "'");
            }
        }
    }
    
    protected ValidatingMigrationInstruction getClosestPreservedAncestorScopeMigrationInstruction(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions) {
        ScopeImpl parent;
        for (parent = instruction.getSourceActivity().getFlowScope(); parent != null && instructions.getInstructionsBySourceScope(parent).isEmpty(); parent = parent.getFlowScope()) {}
        if (parent != null) {
            return instructions.getInstructionsBySourceScope(parent).get(0);
        }
        return null;
    }
}
