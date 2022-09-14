// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface UpdateJobSuspensionStateSelectBuilder
{
    UpdateJobSuspensionStateBuilder byJobId(final String p0);
    
    UpdateJobSuspensionStateBuilder byJobDefinitionId(final String p0);
    
    UpdateJobSuspensionStateBuilder byProcessInstanceId(final String p0);
    
    UpdateJobSuspensionStateBuilder byProcessDefinitionId(final String p0);
    
    UpdateJobSuspensionStateTenantBuilder byProcessDefinitionKey(final String p0);
}
