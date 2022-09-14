// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity.db;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.TenantMembershipEntity;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.persistence.entity.MembershipEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.TenantEntity;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.impl.persistence.entity.GroupEntity;
import java.util.Date;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.identity.Tenant;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.Context;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.persistence.entity.UserEntity;
import org.zik.bpm.engine.impl.identity.IndentityLogger;
import org.zik.bpm.engine.impl.identity.WritableIdentityProvider;

public class DbIdentityServiceProvider extends DbReadOnlyIdentityServiceProvider implements WritableIdentityProvider
{
    protected static final IndentityLogger LOG;
    
    @Override
    public UserEntity createNewUser(final String userId) {
        this.checkAuthorization(Permissions.CREATE, Resources.USER, null);
        return new UserEntity(userId);
    }
    
    @Override
    public IdentityOperationResult saveUser(final User user) {
        final UserEntity userEntity = (UserEntity)user;
        userEntity.encryptPassword();
        String operation = null;
        if (userEntity.getRevision() == 0) {
            operation = "create";
            this.checkAuthorization(Permissions.CREATE, Resources.USER, null);
            this.getDbEntityManager().insert(userEntity);
            this.createDefaultAuthorizations(userEntity);
        }
        else {
            operation = "update";
            this.checkAuthorization(Permissions.UPDATE, Resources.USER, user.getId());
            this.getDbEntityManager().merge(userEntity);
        }
        return new IdentityOperationResult(userEntity, operation);
    }
    
    @Override
    public IdentityOperationResult deleteUser(final String userId) {
        this.checkAuthorization(Permissions.DELETE, Resources.USER, userId);
        final UserEntity user = this.findUserById(userId);
        if (user != null) {
            this.deleteMembershipsByUserId(userId);
            this.deleteTenantMembershipsOfUser(userId);
            this.deleteAuthorizations(Resources.USER, userId);
            Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final List<Tenant> tenants = ((Query<T, Tenant>)DbIdentityServiceProvider.this.createTenantQuery().userMember(userId)).list();
                    if (tenants != null && !tenants.isEmpty()) {
                        for (final Tenant tenant : tenants) {
                            AbstractManager.this.deleteAuthorizationsForUser(Resources.TENANT, tenant.getId(), userId);
                        }
                    }
                    return null;
                }
            });
            this.getDbEntityManager().delete(user);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    @Override
    public boolean checkPassword(final String userId, final String password) {
        final UserEntity user = this.findUserById(userId);
        if (user == null || password == null) {
            return false;
        }
        if (this.isUserLocked(user)) {
            return false;
        }
        if (this.matchPassword(password, user)) {
            this.unlockUser(user);
            return true;
        }
        this.lockUser(user);
        return false;
    }
    
    protected boolean isUserLocked(final UserEntity user) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final int maxAttempts = processEngineConfiguration.getLoginMaxAttempts();
        final int attempts = user.getAttempts();
        if (attempts >= maxAttempts) {
            return true;
        }
        final Date lockExpirationTime = user.getLockExpirationTime();
        final Date currentTime = ClockUtil.getCurrentTime();
        return lockExpirationTime != null && lockExpirationTime.after(currentTime);
    }
    
    protected void lockUser(final UserEntity user) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final int max = processEngineConfiguration.getLoginDelayMaxTime();
        final int baseTime = processEngineConfiguration.getLoginDelayBase();
        final int factor = processEngineConfiguration.getLoginDelayFactor();
        final int attempts = user.getAttempts() + 1;
        long delay = (long)(baseTime * Math.pow(factor, attempts - 1));
        delay = Math.min(delay, max) * 1000L;
        final long currentTime = ClockUtil.getCurrentTime().getTime();
        final Date lockExpirationTime = new Date(currentTime + delay);
        if (attempts >= processEngineConfiguration.getLoginMaxAttempts()) {
            DbIdentityServiceProvider.LOG.infoUserPermanentlyLocked(user.getId());
        }
        else {
            DbIdentityServiceProvider.LOG.infoUserTemporarilyLocked(user.getId(), lockExpirationTime);
        }
        this.getIdentityInfoManager().updateUserLock(user, attempts, lockExpirationTime);
    }
    
    @Override
    public IdentityOperationResult unlockUser(final String userId) {
        final UserEntity user = this.findUserById(userId);
        if (user != null) {
            return this.unlockUser(user);
        }
        return new IdentityOperationResult(null, "none");
    }
    
    protected IdentityOperationResult unlockUser(final UserEntity user) {
        if (user.getAttempts() > 0 || user.getLockExpirationTime() != null) {
            this.getIdentityInfoManager().updateUserLock(user, 0, null);
            return new IdentityOperationResult(user, "unlock");
        }
        return new IdentityOperationResult(user, "none");
    }
    
    @Override
    public GroupEntity createNewGroup(final String groupId) {
        this.checkAuthorization(Permissions.CREATE, Resources.GROUP, null);
        return new GroupEntity(groupId);
    }
    
    @Override
    public IdentityOperationResult saveGroup(final Group group) {
        final GroupEntity groupEntity = (GroupEntity)group;
        String operation = null;
        if (groupEntity.getRevision() == 0) {
            operation = "create";
            this.checkAuthorization(Permissions.CREATE, Resources.GROUP, null);
            this.getDbEntityManager().insert(groupEntity);
            this.createDefaultAuthorizations(group);
        }
        else {
            operation = "update";
            this.checkAuthorization(Permissions.UPDATE, Resources.GROUP, group.getId());
            this.getDbEntityManager().merge(groupEntity);
        }
        return new IdentityOperationResult(groupEntity, operation);
    }
    
    @Override
    public IdentityOperationResult deleteGroup(final String groupId) {
        this.checkAuthorization(Permissions.DELETE, Resources.GROUP, groupId);
        final GroupEntity group = this.findGroupById(groupId);
        if (group != null) {
            this.deleteMembershipsByGroupId(groupId);
            this.deleteTenantMembershipsOfGroup(groupId);
            this.deleteAuthorizations(Resources.GROUP, groupId);
            Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final List<Tenant> tenants = ((Query<T, Tenant>)DbIdentityServiceProvider.this.createTenantQuery().groupMember(groupId)).list();
                    if (tenants != null && !tenants.isEmpty()) {
                        for (final Tenant tenant : tenants) {
                            AbstractManager.this.deleteAuthorizationsForGroup(Resources.TENANT, tenant.getId(), groupId);
                        }
                    }
                    return null;
                }
            });
            this.getDbEntityManager().delete(group);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    @Override
    public Tenant createNewTenant(final String tenantId) {
        this.checkAuthorization(Permissions.CREATE, Resources.TENANT, null);
        return new TenantEntity(tenantId);
    }
    
    @Override
    public IdentityOperationResult saveTenant(final Tenant tenant) {
        final TenantEntity tenantEntity = (TenantEntity)tenant;
        String operation = null;
        if (tenantEntity.getRevision() == 0) {
            operation = "create";
            this.checkAuthorization(Permissions.CREATE, Resources.TENANT, null);
            this.getDbEntityManager().insert(tenantEntity);
            this.createDefaultAuthorizations(tenant);
        }
        else {
            operation = "update";
            this.checkAuthorization(Permissions.UPDATE, Resources.TENANT, tenant.getId());
            this.getDbEntityManager().merge(tenantEntity);
        }
        return new IdentityOperationResult(tenantEntity, operation);
    }
    
    @Override
    public IdentityOperationResult deleteTenant(final String tenantId) {
        this.checkAuthorization(Permissions.DELETE, Resources.TENANT, tenantId);
        final TenantEntity tenant = this.findTenantById(tenantId);
        if (tenant != null) {
            this.deleteTenantMembershipsOfTenant(tenantId);
            this.deleteAuthorizations(Resources.TENANT, tenantId);
            this.getDbEntityManager().delete(tenant);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    @Override
    public IdentityOperationResult createMembership(final String userId, final String groupId) {
        this.checkAuthorization(Permissions.CREATE, Resources.GROUP_MEMBERSHIP, groupId);
        final UserEntity user = this.findUserById(userId);
        EnsureUtil.ensureNotNull("No user found with id '" + userId + "'.", "user", user);
        final GroupEntity group = this.findGroupById(groupId);
        EnsureUtil.ensureNotNull("No group found with id '" + groupId + "'.", "group", group);
        final MembershipEntity membership = new MembershipEntity();
        membership.setUser(user);
        membership.setGroup(group);
        this.getDbEntityManager().insert(membership);
        this.createDefaultMembershipAuthorizations(userId, groupId);
        return new IdentityOperationResult(null, "create");
    }
    
    @Override
    public IdentityOperationResult deleteMembership(final String userId, final String groupId) {
        this.checkAuthorization(Permissions.DELETE, Resources.GROUP_MEMBERSHIP, groupId);
        if (this.existsMembership(userId, groupId)) {
            this.deleteAuthorizations(Resources.GROUP_MEMBERSHIP, groupId);
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("userId", userId);
            parameters.put("groupId", groupId);
            this.getDbEntityManager().delete(MembershipEntity.class, "deleteMembership", parameters);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    protected void deleteMembershipsByUserId(final String userId) {
        this.getDbEntityManager().delete(MembershipEntity.class, "deleteMembershipsByUserId", userId);
    }
    
    protected void deleteMembershipsByGroupId(final String groupId) {
        this.getDbEntityManager().delete(MembershipEntity.class, "deleteMembershipsByGroupId", groupId);
    }
    
    @Override
    public IdentityOperationResult createTenantUserMembership(final String tenantId, final String userId) {
        this.checkAuthorization(Permissions.CREATE, Resources.TENANT_MEMBERSHIP, tenantId);
        final TenantEntity tenant = this.findTenantById(tenantId);
        final UserEntity user = this.findUserById(userId);
        EnsureUtil.ensureNotNull("No tenant found with id '" + tenantId + "'.", "tenant", tenant);
        EnsureUtil.ensureNotNull("No user found with id '" + userId + "'.", "user", user);
        final TenantMembershipEntity membership = new TenantMembershipEntity();
        membership.setTenant(tenant);
        membership.setUser(user);
        this.getDbEntityManager().insert(membership);
        this.createDefaultTenantMembershipAuthorizations(tenant, user);
        return new IdentityOperationResult(null, "create");
    }
    
    @Override
    public IdentityOperationResult createTenantGroupMembership(final String tenantId, final String groupId) {
        this.checkAuthorization(Permissions.CREATE, Resources.TENANT_MEMBERSHIP, tenantId);
        final TenantEntity tenant = this.findTenantById(tenantId);
        final GroupEntity group = this.findGroupById(groupId);
        EnsureUtil.ensureNotNull("No tenant found with id '" + tenantId + "'.", "tenant", tenant);
        EnsureUtil.ensureNotNull("No group found with id '" + groupId + "'.", "group", group);
        final TenantMembershipEntity membership = new TenantMembershipEntity();
        membership.setTenant(tenant);
        membership.setGroup(group);
        this.getDbEntityManager().insert(membership);
        this.createDefaultTenantMembershipAuthorizations(tenant, group);
        return new IdentityOperationResult(null, "create");
    }
    
    @Override
    public IdentityOperationResult deleteTenantUserMembership(final String tenantId, final String userId) {
        this.checkAuthorization(Permissions.DELETE, Resources.TENANT_MEMBERSHIP, tenantId);
        if (this.existsTenantMembership(tenantId, userId, null)) {
            this.deleteAuthorizations(Resources.TENANT_MEMBERSHIP, userId);
            this.deleteAuthorizationsForUser(Resources.TENANT, tenantId, userId);
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tenantId", tenantId);
            parameters.put("userId", userId);
            this.getDbEntityManager().delete(TenantMembershipEntity.class, "deleteTenantMembership", parameters);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    @Override
    public IdentityOperationResult deleteTenantGroupMembership(final String tenantId, final String groupId) {
        this.checkAuthorization(Permissions.DELETE, Resources.TENANT_MEMBERSHIP, tenantId);
        if (this.existsTenantMembership(tenantId, null, groupId)) {
            this.deleteAuthorizations(Resources.TENANT_MEMBERSHIP, groupId);
            this.deleteAuthorizationsForGroup(Resources.TENANT, tenantId, groupId);
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tenantId", tenantId);
            parameters.put("groupId", groupId);
            this.getDbEntityManager().delete(TenantMembershipEntity.class, "deleteTenantMembership", parameters);
            return new IdentityOperationResult(null, "delete");
        }
        return new IdentityOperationResult(null, "none");
    }
    
    protected void deleteTenantMembershipsOfUser(final String userId) {
        this.getDbEntityManager().delete(TenantMembershipEntity.class, "deleteTenantMembershipsOfUser", userId);
    }
    
    protected void deleteTenantMembershipsOfGroup(final String groupId) {
        this.getDbEntityManager().delete(TenantMembershipEntity.class, "deleteTenantMembershipsOfGroup", groupId);
    }
    
    protected void deleteTenantMembershipsOfTenant(final String tenant) {
        this.getDbEntityManager().delete(TenantMembershipEntity.class, "deleteTenantMembershipsOfTenant", tenant);
    }
    
    protected void createDefaultAuthorizations(final UserEntity userEntity) {
        if (Context.getProcessEngineConfiguration().isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().newUser(userEntity));
        }
    }
    
    protected void createDefaultAuthorizations(final Group group) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().newGroup(group));
        }
    }
    
    protected void createDefaultAuthorizations(final Tenant tenant) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().newTenant(tenant));
        }
    }
    
    protected void createDefaultMembershipAuthorizations(final String userId, final String groupId) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().groupMembershipCreated(groupId, userId));
        }
    }
    
    protected void createDefaultTenantMembershipAuthorizations(final Tenant tenant, final User user) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().tenantMembershipCreated(tenant, user));
        }
    }
    
    protected void createDefaultTenantMembershipAuthorizations(final Tenant tenant, final Group group) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().tenantMembershipCreated(tenant, group));
        }
    }
    
    static {
        LOG = ProcessEngineLogger.INDENTITY_LOGGER;
    }
}
