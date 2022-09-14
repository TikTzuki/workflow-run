// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface UpdateJobDefinitionSuspensionStateTenantBuilder extends UpdateJobDefinitionSuspensionStateBuilder
{
    UpdateJobDefinitionSuspensionStateBuilder processDefinitionWithoutTenantId();
    
    UpdateJobDefinitionSuspensionStateBuilder processDefinitionTenantId(final String p0);
}
