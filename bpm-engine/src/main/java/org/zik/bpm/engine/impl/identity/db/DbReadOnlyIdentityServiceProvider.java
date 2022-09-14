// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity.db;

import java.util.HashMap;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.TenantQuery;
import org.zik.bpm.engine.impl.persistence.entity.TenantEntity;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.identity.GroupQuery;
import org.zik.bpm.engine.impl.persistence.entity.GroupEntity;
import org.zik.bpm.engine.impl.util.EncryptionUtil;
import java.util.Map;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.identity.User;
import java.util.List;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.NativeUserQueryImpl;
import org.zik.bpm.engine.identity.NativeUserQuery;
import org.zik.bpm.engine.impl.UserQueryImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.identity.UserQuery;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.persistence.entity.UserEntity;
import org.zik.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class DbReadOnlyIdentityServiceProvider extends AbstractManager implements ReadOnlyIdentityProvider
{
    @Override
    public UserEntity findUserById(final String userId) {
        this.checkAuthorization(Permissions.READ, Resources.USER, userId);
        return this.getDbEntityManager().selectById(UserEntity.class, userId);
    }
    
    @Override
    public UserQuery createUserQuery() {
        return new DbUserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }
    
    @Override
    public UserQueryImpl createUserQuery(final CommandContext commandContext) {
        return new DbUserQueryImpl();
    }
    
    @Override
    public NativeUserQuery createNativeUserQuery() {
        return new NativeUserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }
    
    public long findUserCountByQueryCriteria(final DbUserQueryImpl query) {
        this.configureQuery(query, Resources.USER);
        return (long)this.getDbEntityManager().selectOne("selectUserCountByQueryCriteria", query);
    }
    
    public List<User> findUserByQueryCriteria(final DbUserQueryImpl query) {
        this.configureQuery(query, Resources.USER);
        return (List<User>)this.getDbEntityManager().selectList("selectUserByQueryCriteria", query);
    }
    
    public List<User> findUserByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<User>)this.getDbEntityManager().selectListWithRawParameter("selectUserByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findUserCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectUserCountByNativeQuery", parameterMap);
    }
    
    @Override
    public boolean checkPassword(final String userId, final String password) {
        final UserEntity user = this.findUserById(userId);
        return user != null && password != null && this.matchPassword(password, user);
    }
    
    protected boolean matchPassword(final String password, final UserEntity user) {
        final String saltedPassword = EncryptionUtil.saltPassword(password, user.getSalt());
        return Context.getProcessEngineConfiguration().getPasswordManager().check(saltedPassword, user.getPassword());
    }
    
    @Override
    public GroupEntity findGroupById(final String groupId) {
        this.checkAuthorization(Permissions.READ, Resources.GROUP, groupId);
        return this.getDbEntityManager().selectById(GroupEntity.class, groupId);
    }
    
    @Override
    public GroupQuery createGroupQuery() {
        return new DbGroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }
    
    @Override
    public GroupQuery createGroupQuery(final CommandContext commandContext) {
        return new DbGroupQueryImpl();
    }
    
    public long findGroupCountByQueryCriteria(final DbGroupQueryImpl query) {
        this.configureQuery(query, Resources.GROUP);
        return (long)this.getDbEntityManager().selectOne("selectGroupCountByQueryCriteria", query);
    }
    
    public List<Group> findGroupByQueryCriteria(final DbGroupQueryImpl query) {
        this.configureQuery(query, Resources.GROUP);
        return (List<Group>)this.getDbEntityManager().selectList("selectGroupByQueryCriteria", query);
    }
    
    @Override
    public TenantEntity findTenantById(final String tenantId) {
        this.checkAuthorization(Permissions.READ, Resources.TENANT, tenantId);
        return this.getDbEntityManager().selectById(TenantEntity.class, tenantId);
    }
    
    @Override
    public TenantQuery createTenantQuery() {
        return new DbTenantQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }
    
    @Override
    public TenantQuery createTenantQuery(final CommandContext commandContext) {
        return new DbTenantQueryImpl();
    }
    
    public long findTenantCountByQueryCriteria(final DbTenantQueryImpl query) {
        this.configureQuery(query, Resources.TENANT);
        return (long)this.getDbEntityManager().selectOne("selectTenantCountByQueryCriteria", query);
    }
    
    public List<Tenant> findTenantByQueryCriteria(final DbTenantQueryImpl query) {
        this.configureQuery(query, Resources.TENANT);
        return (List<Tenant>)this.getDbEntityManager().selectList("selectTenantByQueryCriteria", query);
    }
    
    protected boolean existsMembership(final String userId, final String groupId) {
        final Map<String, String> key = new HashMap<String, String>();
        key.put("userId", userId);
        key.put("groupId", groupId);
        return (long)this.getDbEntityManager().selectOne("selectMembershipCount", key) > 0L;
    }
    
    protected boolean existsTenantMembership(final String tenantId, final String userId, final String groupId) {
        final Map<String, String> key = new HashMap<String, String>();
        key.put("tenantId", tenantId);
        if (userId != null) {
            key.put("userId", userId);
        }
        if (groupId != null) {
            key.put("groupId", groupId);
        }
        return (long)this.getDbEntityManager().selectOne("selectTenantMembershipCount", key) > 0L;
    }
    
    @Override
    protected void configureQuery(final AbstractQuery query, final Resource resource) {
        Context.getCommandContext().getAuthorizationManager().configureQuery(query, resource);
    }
    
    @Override
    protected void checkAuthorization(final Permission permission, final Resource resource, final String resourceId) {
        Context.getCommandContext().getAuthorizationManager().checkAuthorization(permission, resource, resourceId);
    }
}
