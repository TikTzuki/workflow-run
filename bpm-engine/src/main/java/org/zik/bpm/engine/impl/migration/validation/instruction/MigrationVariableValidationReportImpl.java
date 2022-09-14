// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.migration.MigrationVariableValidationReport;

public class MigrationVariableValidationReportImpl implements MigrationVariableValidationReport
{
    protected TypedValue typedValue;
    protected List<String> failures;
    
    public MigrationVariableValidationReportImpl(final TypedValue typedValue) {
        this.failures = new ArrayList<String>();
        this.typedValue = typedValue;
    }
    
    @Override
    public <T extends TypedValue> T getTypedValue() {
        return (T)this.typedValue;
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
        return "MigrationVariableValidationReportImpl{, typedValue=" + this.typedValue + ", failures=" + this.failures + '}';
    }
}
