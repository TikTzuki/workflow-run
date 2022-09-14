// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface UpdateProcessDefinitionSuspensionStateTenantBuilder extends UpdateProcessDefinitionSuspensionStateBuilder
{
    UpdateProcessDefinitionSuspensionStateBuilder processDefinitionWithoutTenantId();
    
    UpdateProcessDefinitionSuspensionStateBuilder processDefinitionTenantId(final String p0);
}
