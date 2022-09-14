// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface UpdateProcessDefinitionSuspensionStateSelectBuilder
{
    UpdateProcessDefinitionSuspensionStateBuilder byProcessDefinitionId(final String p0);
    
    UpdateProcessDefinitionSuspensionStateTenantBuilder byProcessDefinitionKey(final String p0);
}
