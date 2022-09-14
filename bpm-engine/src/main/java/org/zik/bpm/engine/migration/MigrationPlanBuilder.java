// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import java.util.Map;

public interface MigrationPlanBuilder
{
    MigrationInstructionsBuilder mapEqualActivities();
    
    MigrationPlanBuilder setVariables(final Map<String, ?> p0);
    
    MigrationInstructionBuilder mapActivities(final String p0, final String p1);
    
    MigrationPlan build();
}
