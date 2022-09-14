// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import java.util.List;

public interface MigratingProcessInstanceValidationReport
{
    String getProcessInstanceId();
    
    List<String> getFailures();
    
    boolean hasFailures();
    
    List<MigratingActivityInstanceValidationReport> getActivityInstanceReports();
    
    List<MigratingTransitionInstanceValidationReport> getTransitionInstanceReports();
}
