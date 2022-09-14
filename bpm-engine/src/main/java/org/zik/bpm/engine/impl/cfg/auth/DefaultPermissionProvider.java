// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.auth;

import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.impl.util.ResourceTypeUtil;
import org.zik.bpm.engine.authorization.Permission;

public class DefaultPermissionProvider implements PermissionProvider
{
    @Override
    public Permission getPermissionForName(final String name, final int resourceType) {
        return ResourceTypeUtil.getPermissionByNameAndResourceType(name, resourceType);
    }
    
    @Override
    public Permission[] getPermissionsForResource(final int resourceType) {
        return ResourceTypeUtil.getPermissionsByResourceType(resourceType);
    }
    
    @Override
    public String getNameForResource(final int resourceType) {
        final Resource resourceByType = ResourceTypeUtil.getResourceByType(resourceType);
        return (resourceByType == null) ? null : resourceByType.resourceName();
    }
}
