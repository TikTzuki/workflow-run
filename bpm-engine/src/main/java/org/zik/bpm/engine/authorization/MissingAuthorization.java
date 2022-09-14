// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public class MissingAuthorization
{
    private String permissionName;
    private String resourceType;
    protected String resourceId;
    
    public MissingAuthorization(final String permissionName, final String resourceType, final String resourceId) {
        this.permissionName = permissionName;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public String getViolatedPermissionName() {
        return this.permissionName;
    }
    
    public String getResourceType() {
        return this.resourceType;
    }
    
    public String getResourceId() {
        return this.resourceId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[permissionName=" + this.permissionName + ", resourceType=" + this.resourceType + ", resourceId=" + this.resourceId + "]";
    }
}
