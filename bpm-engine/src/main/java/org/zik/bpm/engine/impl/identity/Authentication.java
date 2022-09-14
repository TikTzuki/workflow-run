// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Authentication
{
    protected String authenticatedUserId;
    protected List<String> authenticatedGroupIds;
    protected List<String> authenticatedTenantIds;
    
    public Authentication() {
    }
    
    public Authentication(final String authenticatedUserId, final List<String> groupIds) {
        this(authenticatedUserId, groupIds, null);
    }
    
    public Authentication(final String authenticatedUserId, final List<String> authenticatedGroupIds, final List<String> authenticatedTenantIds) {
        this.authenticatedUserId = authenticatedUserId;
        if (authenticatedGroupIds != null) {
            this.authenticatedGroupIds = new ArrayList<String>(authenticatedGroupIds);
        }
        if (authenticatedTenantIds != null) {
            this.authenticatedTenantIds = new ArrayList<String>(authenticatedTenantIds);
        }
    }
    
    public List<String> getGroupIds() {
        return this.authenticatedGroupIds;
    }
    
    public String getUserId() {
        return this.authenticatedUserId;
    }
    
    public List<String> getTenantIds() {
        return this.authenticatedTenantIds;
    }
}
