// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.ProcessEngineConfiguration;

public class PermissionConverter
{
    public static Permission[] getPermissionsForNames(final String[] names, final int resourceType, final ProcessEngineConfiguration engineConfiguration) {
        final Permission[] permissions = new Permission[names.length];
        for (int i = 0; i < names.length; ++i) {
            permissions[i] = ((ProcessEngineConfigurationImpl)engineConfiguration).getPermissionProvider().getPermissionForName(names[i], resourceType);
        }
        return permissions;
    }
    
    public static String[] getNamesForPermissions(final Authorization authorization, final Permission[] permissions) {
        final int type = authorization.getAuthorizationType();
        if ((type == 0 || type == 1) && authorization.isEveryPermissionGranted()) {
            return new String[] { Permissions.ALL.getName() };
        }
        if (type == 2 && authorization.isEveryPermissionRevoked()) {
            return new String[] { Permissions.ALL.getName() };
        }
        final List<String> names = new ArrayList<String>();
        for (final Permission permission : permissions) {
            final String name = permission.getName();
            if (!name.equals(Permissions.NONE.getName()) && !name.equals(Permissions.ALL.getName())) {
                names.add(name);
            }
        }
        return names.toArray(new String[names.size()]);
    }
}
