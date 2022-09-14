// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.migration.MigratingActivityInstanceValidationReport;

public class MigratingActivityInstanceValidationReportImpl implements MigratingActivityInstanceValidationReport
{
    protected String activityInstanceId;
    protected String sourceScopeId;
    protected MigrationInstruction migrationInstruction;
    protected List<String> failures;
    
    public MigratingActivityInstanceValidationReportImpl(final MigratingActivityInstance migratingActivityInstance) {
        this.failures = new ArrayList<String>();
        this.activityInstanceId = migratingActivityInstance.getActivityInstance().getId();
        this.sourceScopeId = migratingActivityInstance.getSourceScope().getId();
        this.migrationInstruction = migratingActivityInstance.getMigrationInstruction();
    }
    
    @Override
    public String getSourceScopeId() {
        return this.sourceScopeId;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
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
