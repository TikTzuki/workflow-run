// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

import java.util.Date;

public interface Authorization
{
    public static final int AUTH_TYPE_GLOBAL = 0;
    public static final int AUTH_TYPE_GRANT = 1;
    public static final int AUTH_TYPE_REVOKE = 2;
    public static final String ANY = "*";
    
    void addPermission(final Permission p0);
    
    void removePermission(final Permission p0);
    
    boolean isPermissionGranted(final Permission p0);
    
    boolean isPermissionRevoked(final Permission p0);
    
    boolean isEveryPermissionGranted();
    
    boolean isEveryPermissionRevoked();
    
    Permission[] getPermissions(final Permission[] p0);
    
    void setPermissions(final Permission[] p0);
    
    String getId();
    
    void setResourceId(final String p0);
    
    String getResourceId();
    
    void setResourceType(final int p0);
    
    void setResource(final Resource p0);
    
    int getResourceType();
    
    void setUserId(final String p0);
    
    String getUserId();
    
    void setGroupId(final String p0);
    
    String getGroupId();
    
    int getAuthorizationType();
    
    Date getRemovalTime();
    
    String getRootProcessInstanceId();
}
