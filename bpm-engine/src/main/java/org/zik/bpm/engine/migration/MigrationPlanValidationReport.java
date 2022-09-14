// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import java.util.Map;
import java.util.List;

public interface MigrationPlanValidationReport
{
    MigrationPlan getMigrationPlan();
    
    boolean hasReports();
    
    boolean hasInstructionReports();
    
    boolean hasVariableReports();
    
    List<MigrationInstructionValidationReport> getInstructionReports();
    
    Map<String, MigrationVariableValidationReport> getVariableReports();
}
