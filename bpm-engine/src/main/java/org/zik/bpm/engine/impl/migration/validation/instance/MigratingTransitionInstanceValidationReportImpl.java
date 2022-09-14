// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.migration.MigratingTransitionInstanceValidationReport;

public class MigratingTransitionInstanceValidationReportImpl implements MigratingTransitionInstanceValidationReport
{
    protected String transitionInstanceId;
    protected String sourceScopeId;
    protected MigrationInstruction migrationInstruction;
    protected List<String> failures;
    
    public MigratingTransitionInstanceValidationReportImpl(final MigratingTransitionInstance migratingTransitionInstance) {
        this.failures = new ArrayList<String>();
        this.transitionInstanceId = migratingTransitionInstance.getTransitionInstance().getId();
        this.sourceScopeId = migratingTransitionInstance.getSourceScope().getId();
        this.migrationInstruction = migratingTransitionInstance.getMigrationInstruction();
    }
    
    @Override
    public String getSourceScopeId() {
        return this.sourceScopeId;
    }
    
    @Override
    public String getTransitionInstanceId() {
        return this.transitionInstanceId;
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
}
