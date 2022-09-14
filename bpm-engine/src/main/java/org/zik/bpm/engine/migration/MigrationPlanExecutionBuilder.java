// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;

public interface MigrationPlanExecutionBuilder
{
    MigrationPlanExecutionBuilder processInstanceIds(final List<String> p0);
    
    MigrationPlanExecutionBuilder processInstanceIds(final String... p0);
    
    MigrationPlanExecutionBuilder processInstanceQuery(final ProcessInstanceQuery p0);
    
    MigrationPlanExecutionBuilder skipCustomListeners();
    
    MigrationPlanExecutionBuilder skipIoMappings();
    
    void execute();
    
    Batch executeAsync();
}
