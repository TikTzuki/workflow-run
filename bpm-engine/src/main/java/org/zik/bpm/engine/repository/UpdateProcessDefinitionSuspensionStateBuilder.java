// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Date;

public interface UpdateProcessDefinitionSuspensionStateBuilder
{
    UpdateProcessDefinitionSuspensionStateBuilder includeProcessInstances(final boolean p0);
    
    UpdateProcessDefinitionSuspensionStateBuilder executionDate(final Date p0);
    
    void activate();
    
    void suspend();
}
