// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.authorization.Resource;
import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.ResourceTypeUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.authorization.Permissions;
import java.util.HashSet;
import org.zik.bpm.engine.authorization.Permission;
import java.util.Set;
import java.util.Date;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.authorization.Authorization;

public class AuthorizationEntity implements Authorization, DbEntity, HasDbRevision, HasDbReferences, Serializable
{
    protected static final EnginePersistenceLogger LOG;
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected int authorizationType;
    protected int permissions;
    protected String userId;
    protected String groupId;
    protected Integer resourceType;
    protected String resourceId;
    protected Date removalTime;
    protected String rootProcessInstanceId;
    private Set<Permission> cachedPermissions;
    
    public AuthorizationEntity() {
        this.cachedPermissions = new HashSet<Permission>();
    }
    
    public AuthorizationEntity(final int type) {
        this.cachedPermissions = new HashSet<Permission>();
        this.authorizationType = type;
        if (this.authorizationType == 0) {
            this.userId = "*";
        }
        this.resetPermissions();
    }
    
    protected void resetPermissions() {
        this.cachedPermissions = new HashSet<Permission>();
        if (this.authorizationType == 0) {
            this.permissions = Permissions.NONE.getValue();
        }
        else if (this.authorizationType == 1) {
            this.permissions = Permissions.NONE.getValue();
        }
        else {
            if (this.authorizationType != 2) {
                throw AuthorizationEntity.LOG.engineAuthorizationTypeException(this.authorizationType, 0, 1, 2);
            }
            this.permissions = Permissions.ALL.getValue();
        }
    }
    
    @Override
    public void addPermission(final Permission p) {
        this.cachedPermissions.add(p);
        this.permissions |= p.getValue();
    }
    
    @Override
    public void removePermission(final Permission p) {
        this.cachedPermissions.add(p);
        this.permissions &= ~p.getValue();
    }
    
    @Override
    public boolean isPermissionGranted(final Permission p) {
        if (2 == this.authorizationType) {
            throw AuthorizationEntity.LOG.permissionStateException("isPermissionGranted", "REVOKE");
        }
        EnsureUtil.ensureNotNull("Authorization 'resourceType' cannot be null", "authorization.getResource()", this.resourceType);
        return ResourceTypeUtil.resourceIsContainedInArray(this.resourceType, p.getTypes()) && (this.permissions & p.getValue()) == p.getValue();
    }
    
    @Override
    public boolean isPermissionRevoked(final Permission p) {
        if (1 == this.authorizationType) {
            throw AuthorizationEntity.LOG.permissionStateException("isPermissionRevoked", "GRANT");
        }
        EnsureUtil.ensureNotNull("Authorization 'resourceType' cannot be null", "authorization.getResource()", this.resourceType);
        return ResourceTypeUtil.resourceIsContainedInArray(this.resourceType, p.getTypes()) && (this.permissions & p.getValue()) != p.getValue();
    }
    
    @Override
    public boolean isEveryPermissionGranted() {
        if (2 == this.authorizationType) {
            throw AuthorizationEntity.LOG.permissionStateException("isEveryPermissionGranted", "REVOKE");
        }
        return this.permissions == Permissions.ALL.getValue();
    }
    
    @Override
    public boolean isEveryPermissionRevoked() {
        if (this.authorizationType == 1) {
            throw AuthorizationEntity.LOG.permissionStateException("isEveryPermissionRevoked", "GRANT");
        }
        return this.permissions == 0;
    }
    
    @Override
    public Permission[] getPermissions(final Permission[] permissions) {
        final List<Permission> result = new ArrayList<Permission>();
        for (final Permission permission : permissions) {
            if ((0 == this.authorizationType || 1 == this.authorizationType) && this.isPermissionGranted(permission)) {
                result.add(permission);
            }
            else if (2 == this.authorizationType && this.isPermissionRevoked(permission)) {
                result.add(permission);
            }
        }
        return result.toArray(new Permission[result.size()]);
    }
    
    @Override
    public void setPermissions(final Permission[] permissions) {
        this.resetPermissions();
        for (final Permission permission : permissions) {
            if (2 == this.authorizationType) {
                this.removePermission(permission);
            }
            else {
                this.addPermission(permission);
            }
        }
    }
    
    @Override
    public int getAuthorizationType() {
        return this.authorizationType;
    }
    
    public void setAuthorizationType(final int authorizationType) {
        this.authorizationType = authorizationType;
    }
    
    @Override
    public String getGroupId() {
        return this.groupId;
    }
    
    @Override
    public void setGroupId(final String groupId) {
        if (groupId != null && this.authorizationType == 0) {
            throw AuthorizationEntity.LOG.notUsableGroupIdForGlobalAuthorizationException();
        }
        this.groupId = groupId;
    }
    
    @Override
    public String getUserId() {
        return this.userId;
    }
    
    @Override
    public void setUserId(final String userId) {
        if (userId != null && this.authorizationType == 0 && !"*".equals(userId)) {
            throw AuthorizationEntity.LOG.illegalValueForUserIdException(userId, "*");
        }
        this.userId = userId;
    }
    
    @Override
    public int getResourceType() {
        return this.resourceType;
    }
    
    @Override
    public void setResourceType(final int type) {
        this.resourceType = type;
    }
    
    public Integer getResource() {
        return this.resourceType;
    }
    
    @Override
    public void setResource(final Resource resource) {
        this.resourceType = resource.resourceType();
    }
    
    @Override
    public String getResourceId() {
        return this.resourceId;
    }
    
    @Override
    public void setResourceId(final String resourceId) {
        this.resourceId = resourceId;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public void setPermissions(final int permissions) {
        this.permissions = permissions;
    }
    
    public int getPermissions() {
        return this.permissions;
    }
    
    public Set<Permission> getCachedPermissions() {
        return this.cachedPermissions;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public Object getPersistentState() {
        final HashMap<String, Object> state = new HashMap<String, Object>();
        state.put("userId", this.userId);
        state.put("groupId", this.groupId);
        state.put("resourceType", this.resourceType);
        state.put("resourceId", this.resourceId);
        state.put("permissions", this.permissions);
        state.put("removalTime", this.removalTime);
        state.put("rootProcessInstanceId", this.rootProcessInstanceId);
        return state;
    }
    
    @Override
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        return referenceIdAndClass;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", authorizationType=" + this.authorizationType + ", permissions=" + this.permissions + ", userId=" + this.userId + ", groupId=" + this.groupId + ", resourceType=" + this.resourceType + ", resourceId=" + this.resourceId + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
