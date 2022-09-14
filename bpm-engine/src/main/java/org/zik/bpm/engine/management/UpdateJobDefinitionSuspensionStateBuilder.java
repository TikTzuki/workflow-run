// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.Date;

public interface UpdateJobDefinitionSuspensionStateBuilder
{
    UpdateJobDefinitionSuspensionStateBuilder includeJobs(final boolean p0);
    
    UpdateJobDefinitionSuspensionStateBuilder executionDate(final Date p0);
    
    void activate();
    
    void suspend();
}
