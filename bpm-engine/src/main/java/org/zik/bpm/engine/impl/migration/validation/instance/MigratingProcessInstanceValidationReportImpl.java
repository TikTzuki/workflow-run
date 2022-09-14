// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.migration.MigratingTransitionInstanceValidationReport;
import org.zik.bpm.engine.migration.MigratingActivityInstanceValidationReport;
import java.util.List;
import org.zik.bpm.engine.migration.MigratingProcessInstanceValidationReport;

public class MigratingProcessInstanceValidationReportImpl implements MigratingProcessInstanceValidationReport
{
    protected String processInstanceId;
    protected List<MigratingActivityInstanceValidationReport> activityInstanceReports;
    protected List<MigratingTransitionInstanceValidationReport> transitionInstanceReports;
    protected List<String> failures;
    
    public MigratingProcessInstanceValidationReportImpl() {
        this.activityInstanceReports = new ArrayList<MigratingActivityInstanceValidationReport>();
        this.transitionInstanceReports = new ArrayList<MigratingTransitionInstanceValidationReport>();
        this.failures = new ArrayList<String>();
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public void addActivityInstanceReport(final MigratingActivityInstanceValidationReport instanceReport) {
        this.activityInstanceReports.add(instanceReport);
    }
    
    public void addTransitionInstanceReport(final MigratingTransitionInstanceValidationReport instanceReport) {
        this.transitionInstanceReports.add(instanceReport);
    }
    
    @Override
    public List<MigratingActivityInstanceValidationReport> getActivityInstanceReports() {
        return this.activityInstanceReports;
    }
    
    @Override
    public List<MigratingTransitionInstanceValidationReport> getTransitionInstanceReports() {
        return this.transitionInstanceReports;
    }
    
    public void addFailure(final String failure) {
        this.failures.add(failure);
    }
    
    @Override
    public List<String> getFailures() {
        return this.failures;
    }
    
    @Override
    public boolean hasFailures() {
        return !this.failures.isEmpty() || !this.activityInstanceReports.isEmpty() || !this.transitionInstanceReports.isEmpty();
    }
    
    public void writeTo(final StringBuilder sb) {
        sb.append("Cannot migrate process instance '").append(this.processInstanceId).append("':\n");
        for (final String failure : this.failures) {
            sb.append("\t").append(failure).append("\n");
        }
        for (final MigratingActivityInstanceValidationReport report : this.activityInstanceReports) {
            sb.append("\tCannot migrate activity instance '").append(report.getActivityInstanceId()).append("':\n");
            for (final String failure2 : report.getFailures()) {
                sb.append("\t\t").append(failure2).append("\n");
            }
        }
        for (final MigratingTransitionInstanceValidationReport report2 : this.transitionInstanceReports) {
            sb.append("\tCannot migrate transition instance '").append(report2.getTransitionInstanceId()).append("':\n");
            for (final String failure2 : report2.getFailures()) {
                sb.append("\t\t").append(failure2).append("\n");
            }
        }
    }
}
