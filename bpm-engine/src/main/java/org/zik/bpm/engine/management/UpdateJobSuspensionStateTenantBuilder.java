// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface UpdateJobSuspensionStateTenantBuilder extends UpdateJobSuspensionStateBuilder
{
    UpdateJobSuspensionStateBuilder processDefinitionWithoutTenantId();
    
    UpdateJobSuspensionStateBuilder processDefinitionTenantId(final String p0);
}
