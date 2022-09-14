// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;

public class PermissionCheck
{
    protected Permission permission;
    protected int perms;
    protected Resource resource;
    protected int resourceType;
    protected String resourceId;
    protected String resourceIdQueryParam;
    protected Long authorizationNotFoundReturnValue;
    
    public PermissionCheck() {
        this.authorizationNotFoundReturnValue = null;
    }
    
    public Permission getPermission() {
        return this.permission;
    }
    
    public void setPermission(final Permission permission) {
        this.permission = permission;
        if (permission != null) {
            this.perms = permission.getValue();
        }
    }
    
    public int getPerms() {
        return this.perms;
    }
    
    public Resource getResource() {
        return this.resource;
    }
    
    public void setResource(final Resource resource) {
        this.resource = resource;
        if (resource != null) {
            this.resourceType = resource.resourceType();
        }
    }
    
    public int getResourceType() {
        return this.resourceType;
    }
    
    public String getResourceId() {
        return this.resourceId;
    }
    
    public void setResourceId(final String resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getResourceIdQueryParam() {
        return this.resourceIdQueryParam;
    }
    
    public void setResourceIdQueryParam(final String resourceIdQueryParam) {
        this.resourceIdQueryParam = resourceIdQueryParam;
    }
    
    public Long getAuthorizationNotFoundReturnValue() {
        return this.authorizationNotFoundReturnValue;
    }
    
    public void setAuthorizationNotFoundReturnValue(final Long authorizationNotFoundReturnValue) {
        this.authorizationNotFoundReturnValue = authorizationNotFoundReturnValue;
    }
}
