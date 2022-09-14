// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface UpdateProcessInstanceSuspensionStateTenantBuilder extends UpdateProcessInstanceSuspensionStateBuilder
{
    UpdateProcessInstanceSuspensionStateTenantBuilder processDefinitionWithoutTenantId();
    
    UpdateProcessInstanceSuspensionStateTenantBuilder processDefinitionTenantId(final String p0);
}
