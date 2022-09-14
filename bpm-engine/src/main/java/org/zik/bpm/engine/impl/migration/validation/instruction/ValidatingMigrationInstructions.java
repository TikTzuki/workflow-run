// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.Collections;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;
import java.util.Collection;

public class ValidatingMigrationInstructions
{
    protected Collection<ValidatingMigrationInstruction> instructions;
    protected Map<ScopeImpl, List<ValidatingMigrationInstruction>> instructionsBySourceScope;
    protected Map<ScopeImpl, List<ValidatingMigrationInstruction>> instructionsByTargetScope;
    
    public ValidatingMigrationInstructions(final Collection<ValidatingMigrationInstruction> instructions) {
        this.instructions = instructions;
        this.instructionsBySourceScope = new HashMap<ScopeImpl, List<ValidatingMigrationInstruction>>();
        this.instructionsByTargetScope = new HashMap<ScopeImpl, List<ValidatingMigrationInstruction>>();
        for (final ValidatingMigrationInstruction instruction : instructions) {
            this.indexInstruction(instruction);
        }
    }
    
    public ValidatingMigrationInstructions() {
        this(new HashSet<ValidatingMigrationInstruction>());
    }
    
    public void addInstruction(final ValidatingMigrationInstruction instruction) {
        this.instructions.add(instruction);
        this.indexInstruction(instruction);
    }
    
    public void addAll(final List<ValidatingMigrationInstruction> instructions) {
        for (final ValidatingMigrationInstruction instruction : instructions) {
            this.addInstruction(instruction);
        }
    }
    
    protected void indexInstruction(final ValidatingMigrationInstruction instruction) {
        CollectionUtil.addToMapOfLists(this.instructionsBySourceScope, instruction.getSourceActivity(), instruction);
        CollectionUtil.addToMapOfLists(this.instructionsByTargetScope, instruction.getTargetActivity(), instruction);
    }
    
    public List<ValidatingMigrationInstruction> getInstructions() {
        return new ArrayList<ValidatingMigrationInstruction>(this.instructions);
    }
    
    public List<ValidatingMigrationInstruction> getInstructionsBySourceScope(final ScopeImpl scope) {
        final List<ValidatingMigrationInstruction> instructions = this.instructionsBySourceScope.get(scope);
        if (instructions == null) {
            return Collections.emptyList();
        }
        return instructions;
    }
    
    public List<ValidatingMigrationInstruction> getInstructionsByTargetScope(final ScopeImpl scope) {
        final List<ValidatingMigrationInstruction> instructions = this.instructionsByTargetScope.get(scope);
        if (instructions == null) {
            return Collections.emptyList();
        }
        return instructions;
    }
    
    public void filterWith(final List<MigrationInstructionValidator> validators) {
        final List<ValidatingMigrationInstruction> validInstructions = new ArrayList<ValidatingMigrationInstruction>();
        for (final ValidatingMigrationInstruction instruction : this.instructions) {
            if (this.isValidInstruction(instruction, this, validators)) {
                validInstructions.add(instruction);
            }
        }
        this.instructionsBySourceScope.clear();
        this.instructionsByTargetScope.clear();
        this.instructions.clear();
        for (final ValidatingMigrationInstruction validInstruction : validInstructions) {
            this.addInstruction(validInstruction);
        }
    }
    
    public List<MigrationInstruction> asMigrationInstructions() {
        final List<MigrationInstruction> instructions = new ArrayList<MigrationInstruction>();
        for (final ValidatingMigrationInstruction instruction : this.instructions) {
            instructions.add(instruction.toMigrationInstruction());
        }
        return instructions;
    }
    
    public boolean contains(final ValidatingMigrationInstruction instruction) {
        return this.instructions.contains(instruction);
    }
    
    public boolean containsInstructionForSourceScope(final ScopeImpl sourceScope) {
        return this.instructionsBySourceScope.containsKey(sourceScope);
    }
    
    protected boolean isValidInstruction(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final List<MigrationInstructionValidator> migrationInstructionValidators) {
        return !this.validateInstruction(instruction, instructions, migrationInstructionValidators).hasFailures();
    }
    
    protected MigrationInstructionValidationReportImpl validateInstruction(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final List<MigrationInstructionValidator> migrationInstructionValidators) {
        final MigrationInstructionValidationReportImpl validationReport = new MigrationInstructionValidationReportImpl(instruction.toMigrationInstruction());
        for (final MigrationInstructionValidator migrationInstructionValidator : migrationInstructionValidators) {
            migrationInstructionValidator.validate(instruction, instructions, validationReport);
        }
        return validationReport;
    }
}
