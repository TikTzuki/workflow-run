// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.authorization.SystemPermissions;
import org.zik.bpm.engine.authorization.OptimizePermissions;
import org.zik.bpm.engine.authorization.UserOperationLogCategoryPermissions;
import org.zik.bpm.engine.authorization.HistoricProcessInstancePermissions;
import org.zik.bpm.engine.authorization.HistoricTaskPermissions;
import org.zik.bpm.engine.authorization.TaskPermissions;
import org.zik.bpm.engine.authorization.ProcessInstancePermissions;
import org.zik.bpm.engine.authorization.ProcessDefinitionPermissions;
import org.zik.bpm.engine.authorization.BatchPermissions;
import java.util.HashMap;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import java.util.Map;

public class ResourceTypeUtil
{
    protected static final Map<Integer, Class<? extends Enum<? extends Permission>>> PERMISSION_ENUMS;
    
    public static boolean resourceIsContainedInArray(final Integer resourceTypeId, final Resource[] resources) {
        for (final Resource resource : resources) {
            if (resourceTypeId == resource.resourceType()) {
                return true;
            }
        }
        return false;
    }
    
    public static Map<Integer, Class<? extends Enum<? extends Permission>>> getPermissionEnums() {
        return ResourceTypeUtil.PERMISSION_ENUMS;
    }
    
    public static Permission[] getPermissionsByResourceType(final int givenResourceType) {
        final Class<? extends Enum<? extends Permission>> clazz = ResourceTypeUtil.PERMISSION_ENUMS.get(givenResourceType);
        if (clazz == null) {
            return Permissions.values();
        }
        return (Permission[])clazz.getEnumConstants();
    }
    
    public static Permission getPermissionByNameAndResourceType(final String permissionName, final int resourceType) {
        for (final Permission permission : getPermissionsByResourceType(resourceType)) {
            if (permission.getName().equals(permissionName)) {
                return permission;
            }
        }
        throw new BadUserRequestException(String.format("The permission '%s' is not valid for '%s' resource type.", permissionName, getResourceByType(resourceType)));
    }
    
    public static Resource getResourceByType(final int resourceType) {
        for (final Resource resource : Resources.values()) {
            if (resource.resourceType() == resourceType) {
                return resource;
            }
        }
        return null;
    }
    
    static {
        PERMISSION_ENUMS = new HashMap<Integer, Class<? extends Enum<? extends Permission>>>() {
            {
                ((HashMap<Integer, Class<BatchPermissions>>)this).put(Resources.BATCH.resourceType(), BatchPermissions.class);
                ((HashMap<Integer, Class<ProcessDefinitionPermissions>>)this).put(Resources.PROCESS_DEFINITION.resourceType(), ProcessDefinitionPermissions.class);
                ((HashMap<Integer, Class<ProcessInstancePermissions>>)this).put(Resources.PROCESS_INSTANCE.resourceType(), ProcessInstancePermissions.class);
                ((HashMap<Integer, Class<TaskPermissions>>)this).put(Resources.TASK.resourceType(), TaskPermissions.class);
                ((HashMap<Integer, Class<HistoricTaskPermissions>>)this).put(Resources.HISTORIC_TASK.resourceType(), HistoricTaskPermissions.class);
                ((HashMap<Integer, Class<HistoricProcessInstancePermissions>>)this).put(Resources.HISTORIC_PROCESS_INSTANCE.resourceType(), HistoricProcessInstancePermissions.class);
                ((HashMap<Integer, Class<UserOperationLogCategoryPermissions>>)this).put(Resources.OPERATION_LOG_CATEGORY.resourceType(), UserOperationLogCategoryPermissions.class);
                ((HashMap<Integer, Class<OptimizePermissions>>)this).put(Resources.OPTIMIZE.resourceType(), OptimizePermissions.class);
                ((HashMap<Integer, Class<SystemPermissions>>)this).put(Resources.SYSTEM.resourceType(), SystemPermissions.class);
            }
        };
        for (final Permission permission : Permissions.values()) {
            if (!permission.equals(Permissions.ALL)) {
                if (!permission.equals(Permissions.NONE)) {
                    for (final Resource resource : permission.getTypes()) {
                        final int resourceType = resource.resourceType();
                        if (!ResourceTypeUtil.PERMISSION_ENUMS.containsKey(resourceType)) {
                            ResourceTypeUtil.PERMISSION_ENUMS.put(resourceType, Permissions.class);
                        }
                    }
                }
            }
        }
    }
}
