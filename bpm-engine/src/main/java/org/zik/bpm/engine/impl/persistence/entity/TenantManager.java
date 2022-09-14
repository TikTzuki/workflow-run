// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.identity.Authentication;
import java.util.List;
import org.zik.bpm.engine.impl.db.TenantCheck;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class TenantManager extends AbstractManager
{
    public ListQueryParameterObject configureQuery(final ListQueryParameterObject query) {
        final TenantCheck tenantCheck = query.getTenantCheck();
        this.configureTenantCheck(tenantCheck);
        return query;
    }
    
    public void configureTenantCheck(final TenantCheck tenantCheck) {
        if (this.isTenantCheckEnabled()) {
            final Authentication currentAuthentication = this.getCurrentAuthentication();
            tenantCheck.setTenantCheckEnabled(true);
            tenantCheck.setAuthTenantIds(currentAuthentication.getTenantIds());
        }
        else {
            tenantCheck.setTenantCheckEnabled(false);
            tenantCheck.setAuthTenantIds(null);
        }
    }
    
    public ListQueryParameterObject configureQuery(final Object parameters) {
        final ListQueryParameterObject queryObject = new ListQueryParameterObject();
        queryObject.setParameter(parameters);
        return this.configureQuery(queryObject);
    }
    
    public boolean isAuthenticatedTenant(final String tenantId) {
        if (tenantId != null && this.isTenantCheckEnabled()) {
            final Authentication currentAuthentication = this.getCurrentAuthentication();
            final List<String> authenticatedTenantIds = currentAuthentication.getTenantIds();
            return authenticatedTenantIds != null && authenticatedTenantIds.contains(tenantId);
        }
        return true;
    }
    
    public boolean isTenantCheckEnabled() {
        return Context.getProcessEngineConfiguration().isTenantCheckEnabled() && Context.getCommandContext().isTenantCheckEnabled() && this.getCurrentAuthentication() != null && !this.getAuthorizationManager().isCamundaAdmin(this.getCurrentAuthentication());
    }
}
