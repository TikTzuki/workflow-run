// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.migration.MigrationInstructionValidationReport;

public class MigrationInstructionValidationReportImpl implements MigrationInstructionValidationReport
{
    protected MigrationInstruction migrationInstruction;
    protected List<String> failures;
    
    public MigrationInstructionValidationReportImpl(final MigrationInstruction migrationInstruction) {
        this.failures = new ArrayList<String>();
        this.migrationInstruction = migrationInstruction;
    }
    
    @Override
    public MigrationInstruction getMigrationInstruction() {
        return this.migrationInstruction;
    }
    
    public void addFailure(final String failure) {
        this.failures.add(failure);
    }
    
    @Override
    public boolean hasFailures() {
        return !this.failures.isEmpty();
    }
    
    @Override
    public List<String> getFailures() {
        return this.failures;
    }
    
    @Override
    public String toString() {
        return "MigrationInstructionValidationReportImpl{migrationInstruction=" + this.migrationInstruction + ", failures=" + this.failures + '}';
    }
}
