// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface UpdateJobDefinitionSuspensionStateSelectBuilder
{
    UpdateJobDefinitionSuspensionStateBuilder byJobDefinitionId(final String p0);
    
    UpdateJobDefinitionSuspensionStateBuilder byProcessDefinitionId(final String p0);
    
    UpdateJobDefinitionSuspensionStateTenantBuilder byProcessDefinitionKey(final String p0);
}
