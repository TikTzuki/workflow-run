// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface UpdateProcessInstanceSuspensionStateSelectBuilder extends UpdateProcessInstancesRequest
{
    UpdateProcessInstanceSuspensionStateBuilder byProcessInstanceId(final String p0);
    
    UpdateProcessInstanceSuspensionStateBuilder byProcessDefinitionId(final String p0);
    
    UpdateProcessInstanceSuspensionStateTenantBuilder byProcessDefinitionKey(final String p0);
}
