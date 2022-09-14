// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.multitenancy;

public interface TenantIdProvider
{
    String provideTenantIdForProcessInstance(final TenantIdProviderProcessInstanceContext p0);
    
    String provideTenantIdForCaseInstance(final TenantIdProviderCaseInstanceContext p0);
    
    String provideTenantIdForHistoricDecisionInstance(final TenantIdProviderHistoricDecisionInstanceContext p0);
}
