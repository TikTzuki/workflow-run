// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class TenantCheck implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected boolean isTenantCheckEnabled;
    protected List<String> authTenantIds;
    
    public TenantCheck() {
        this.isTenantCheckEnabled = true;
        this.authTenantIds = new ArrayList<String>();
    }
    
    public boolean isTenantCheckEnabled() {
        return this.isTenantCheckEnabled;
    }
    
    public boolean getIsTenantCheckEnabled() {
        return this.isTenantCheckEnabled;
    }
    
    public void setTenantCheckEnabled(final boolean isTenantCheckEnabled) {
        this.isTenantCheckEnabled = isTenantCheckEnabled;
    }
    
    public List<String> getAuthTenantIds() {
        return this.authTenantIds;
    }
    
    public void setAuthTenantIds(final List<String> tenantIds) {
        this.authTenantIds = tenantIds;
    }
}
