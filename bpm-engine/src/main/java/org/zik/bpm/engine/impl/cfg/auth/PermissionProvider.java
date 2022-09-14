// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.auth;

import org.zik.bpm.engine.authorization.Permission;

public interface PermissionProvider
{
    Permission getPermissionForName(final String p0, final int p1);
    
    Permission[] getPermissionsForResource(final int p0);
    
    String getNameForResource(final int p0);
}
