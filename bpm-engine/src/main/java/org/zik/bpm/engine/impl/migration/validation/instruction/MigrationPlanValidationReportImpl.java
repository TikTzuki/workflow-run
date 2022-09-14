// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import org.zik.bpm.engine.migration.MigrationVariableValidationReport;
import java.util.Map;
import org.zik.bpm.engine.migration.MigrationInstructionValidationReport;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.migration.MigrationPlanValidationReport;

public class MigrationPlanValidationReportImpl implements MigrationPlanValidationReport
{
    protected MigrationPlan migrationPlan;
    protected List<MigrationInstructionValidationReport> instructionReports;
    protected Map<String, MigrationVariableValidationReport> variableReports;
    
    public MigrationPlanValidationReportImpl(final MigrationPlan migrationPlan) {
        this.instructionReports = new ArrayList<MigrationInstructionValidationReport>();
        this.variableReports = new HashMap<String, MigrationVariableValidationReport>();
        this.migrationPlan = migrationPlan;
    }
    
    @Override
    public MigrationPlan getMigrationPlan() {
        return this.migrationPlan;
    }
    
    @Override
    public boolean hasReports() {
        return this.hasVariableReports() || this.hasInstructionReports();
    }
    
    public void addInstructionReport(final MigrationInstructionValidationReport instructionReport) {
        this.instructionReports.add(instructionReport);
    }
    
    public void addVariableReport(final String variableName, final MigrationVariableValidationReport variableReport) {
        this.variableReports.put(variableName, variableReport);
    }
    
    @Override
    public boolean hasInstructionReports() {
        return !this.instructionReports.isEmpty();
    }
    
    @Override
    public List<MigrationInstructionValidationReport> getInstructionReports() {
        return this.instructionReports;
    }
    
    @Override
    public boolean hasVariableReports() {
        return !this.variableReports.isEmpty();
    }
    
    @Override
    public Map<String, MigrationVariableValidationReport> getVariableReports() {
        return this.variableReports;
    }
    
    public void writeTo(final StringBuilder sb) {
        sb.append("Migration plan for process definition '").append(this.migrationPlan.getSourceProcessDefinitionId()).append("' to '").append(this.migrationPlan.getTargetProcessDefinitionId()).append("' is not valid:\n");
        for (final MigrationInstructionValidationReport instructionReport : this.instructionReports) {
            sb.append("\t Migration instruction ").append(instructionReport.getMigrationInstruction()).append(" is not valid:\n");
            for (final String failure : instructionReport.getFailures()) {
                sb.append("\t\t").append(failure).append("\n");
            }
        }
        final Iterator<String> iterator3;
        String failure2;
        this.variableReports.forEach((name, report) -> {
            sb.append("\t Migration variable ").append(name).append(" is not valid:\n");
            report.getFailures().iterator();
            while (iterator3.hasNext()) {
                failure2 = iterator3.next();
                sb.append("\t\t").append(failure2).append("\n");
            }
        });
    }
}
