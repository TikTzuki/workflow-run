// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.auth;

import java.util.Iterator;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.authorization.TaskPermissions;
import org.zik.bpm.engine.authorization.HistoricTaskPermissions;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Date;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.identity.Tenant;
import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.identity.User;

public class DefaultAuthorizationProvider implements ResourceAuthorizationProvider
{
    @Override
    public AuthorizationEntity[] newUser(final User user) {
        final String userId = user.getId();
        EnsureUtil.ensureValidIndividualResourceId("Cannot create default authorization for user " + userId, userId);
        final AuthorizationEntity resourceOwnerAuthorization = this.createGrantAuthorization(userId, null, Resources.USER, userId, Permissions.ALL);
        return new AuthorizationEntity[] { resourceOwnerAuthorization };
    }
    
    @Override
    public AuthorizationEntity[] newGroup(final Group group) {
        final List<AuthorizationEntity> authorizations = new ArrayList<AuthorizationEntity>();
        final String groupId = group.getId();
        EnsureUtil.ensureValidIndividualResourceId("Cannot create default authorization for group " + groupId, groupId);
        final AuthorizationEntity groupMemberAuthorization = this.createGrantAuthorization(null, groupId, Resources.GROUP, groupId, Permissions.READ);
        authorizations.add(groupMemberAuthorization);
        return authorizations.toArray(new AuthorizationEntity[0]);
    }
    
    @Override
    public AuthorizationEntity[] newTenant(final Tenant tenant) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] groupMembershipCreated(final String groupId, final String userId) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] tenantMembershipCreated(final Tenant tenant, final User user) {
        final AuthorizationEntity userAuthorization = this.createGrantAuthorization(user.getId(), null, Resources.TENANT, tenant.getId(), Permissions.READ);
        return new AuthorizationEntity[] { userAuthorization };
    }
    
    @Override
    public AuthorizationEntity[] tenantMembershipCreated(final Tenant tenant, final Group group) {
        final AuthorizationEntity userAuthorization = this.createGrantAuthorization(null, group.getId(), Resources.TENANT, tenant.getId(), Permissions.READ);
        return new AuthorizationEntity[] { userAuthorization };
    }
    
    @Override
    public AuthorizationEntity[] newFilter(final Filter filter) {
        final String owner = filter.getOwner();
        if (owner != null) {
            final String filterId = filter.getId();
            EnsureUtil.ensureValidIndividualResourceId("Cannot create default authorization for filter owner " + owner, owner);
            final AuthorizationEntity filterOwnerAuthorization = this.createGrantAuthorization(owner, null, Resources.FILTER, filterId, Permissions.ALL);
            return new AuthorizationEntity[] { filterOwnerAuthorization };
        }
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newDeployment(final Deployment deployment) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final IdentityService identityService = processEngineConfiguration.getIdentityService();
        final Authentication currentAuthentication = identityService.getCurrentAuthentication();
        if (currentAuthentication != null && currentAuthentication.getUserId() != null) {
            final String userId = currentAuthentication.getUserId();
            final String deploymentId = deployment.getId();
            final AuthorizationEntity authorization = this.createGrantAuthorization(userId, null, Resources.DEPLOYMENT, deploymentId, Permissions.READ, Permissions.DELETE);
            return new AuthorizationEntity[] { authorization };
        }
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newProcessDefinition(final ProcessDefinition processDefinition) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newProcessInstance(final ProcessInstance processInstance) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newTask(final Task task) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newTaskAssignee(final Task task, final String oldAssignee, final String newAssignee) {
        if (newAssignee != null) {
            EnsureUtil.ensureValidIndividualResourceId("Cannot create default authorization for assignee " + newAssignee, newAssignee);
            return this.createOrUpdateAuthorizationsByUserId(task, newAssignee);
        }
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newTaskOwner(final Task task, final String oldOwner, final String newOwner) {
        if (newOwner != null) {
            EnsureUtil.ensureValidIndividualResourceId("Cannot create default authorization for owner " + newOwner, newOwner);
            return this.createOrUpdateAuthorizationsByUserId(task, newOwner);
        }
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newTaskUserIdentityLink(final Task task, final String userId, final String type) {
        EnsureUtil.ensureValidIndividualResourceId("Cannot grant default authorization for identity link to user " + userId, userId);
        return this.createOrUpdateAuthorizationsByUserId(task, userId);
    }
    
    @Override
    public AuthorizationEntity[] newTaskGroupIdentityLink(final Task task, final String groupId, final String type) {
        EnsureUtil.ensureValidIndividualResourceId("Cannot grant default authorization for identity link to group " + groupId, groupId);
        return this.createOrUpdateAuthorizationsByGroupId(task, groupId);
    }
    
    @Override
    public AuthorizationEntity[] deleteTaskUserIdentityLink(final Task task, final String userId, final String type) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] deleteTaskGroupIdentityLink(final Task task, final String groupId, final String type) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newDecisionDefinition(final DecisionDefinition decisionDefinition) {
        return null;
    }
    
    @Override
    public AuthorizationEntity[] newDecisionRequirementsDefinition(final DecisionRequirementsDefinition decisionRequirementsDefinition) {
        return null;
    }
    
    protected AuthorizationEntity[] createOrUpdateAuthorizationsByGroupId(final Task task, final String groupId) {
        return this.createOrUpdateAuthorizations(task, groupId, null);
    }
    
    protected AuthorizationEntity[] createOrUpdateAuthorizationsByUserId(final Task task, final String userId) {
        return this.createOrUpdateAuthorizations(task, null, userId);
    }
    
    protected AuthorizationEntity[] createOrUpdateAuthorizations(final Task task, final String groupId, final String userId) {
        final boolean enforceSpecificVariablePermission = this.isEnforceSpecificVariablePermission();
        final Permission[] runtimeTaskPermissions = this.getRuntimePermissions(enforceSpecificVariablePermission);
        final AuthorizationEntity runtimeAuthorization = this.createOrUpdateAuthorization(task, userId, groupId, Resources.TASK, false, runtimeTaskPermissions);
        if (!this.isHistoricInstancePermissionsEnabled()) {
            return new AuthorizationEntity[] { runtimeAuthorization };
        }
        final Permission[] historicTaskPermissions = this.getHistoricPermissions(enforceSpecificVariablePermission);
        final AuthorizationEntity historyAuthorization = this.createOrUpdateAuthorization(task, userId, groupId, Resources.HISTORIC_TASK, true, historicTaskPermissions);
        return new AuthorizationEntity[] { runtimeAuthorization, historyAuthorization };
    }
    
    protected AuthorizationEntity createOrUpdateAuthorization(final Task task, final String userId, final String groupId, final Resource resource, final boolean isHistoric, final Permission... permissions) {
        final String taskId = task.getId();
        AuthorizationEntity authorization = this.getGrantAuthorization(taskId, userId, groupId, resource);
        if (authorization == null) {
            authorization = this.createAuthorization(userId, groupId, resource, taskId, permissions);
            if (isHistoric) {
                this.provideRemovalTime(authorization, task);
            }
        }
        else {
            this.addPermissions(authorization, permissions);
        }
        return authorization;
    }
    
    protected void provideRemovalTime(final AuthorizationEntity authorization, final Task task) {
        final String rootProcessInstanceId = this.getRootProcessInstanceId(task);
        if (rootProcessInstanceId != null) {
            authorization.setRootProcessInstanceId(rootProcessInstanceId);
            if (this.isHistoryRemovalTimeStrategyStart()) {
                final HistoryEvent rootProcessInstance = this.findHistoricProcessInstance(rootProcessInstanceId);
                Date removalTime = null;
                if (rootProcessInstance != null) {
                    removalTime = rootProcessInstance.getRemovalTime();
                }
                authorization.setRemovalTime(removalTime);
            }
        }
    }
    
    protected String getRootProcessInstanceId(final Task task) {
        final ExecutionEntity execution = (ExecutionEntity)((DelegateTask)task).getExecution();
        if (execution != null) {
            return execution.getRootProcessInstanceId();
        }
        return null;
    }
    
    protected boolean isHistoryRemovalTimeStrategyStart() {
        return "start".equals(this.getHistoryRemovalTimeStrategy());
    }
    
    protected String getHistoryRemovalTimeStrategy() {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected HistoryEvent findHistoricProcessInstance(final String rootProcessInstanceId) {
        return Context.getCommandContext().getDbEntityManager().selectById(HistoricProcessInstanceEventEntity.class, rootProcessInstanceId);
    }
    
    protected Permission[] getHistoricPermissions(final boolean enforceSpecificVariablePermission) {
        final List<Permission> historicPermissions = new ArrayList<Permission>();
        historicPermissions.add(HistoricTaskPermissions.READ);
        if (enforceSpecificVariablePermission) {
            historicPermissions.add(HistoricTaskPermissions.READ_VARIABLE);
        }
        return historicPermissions.toArray(new Permission[0]);
    }
    
    protected Permission[] getRuntimePermissions(final boolean enforceSpecificVariablePermission) {
        final List<Permission> runtimePermissions = new ArrayList<Permission>();
        runtimePermissions.add(Permissions.READ);
        final Permission defaultUserPermissionForTask = this.getDefaultUserPermissionForTask();
        runtimePermissions.add(defaultUserPermissionForTask);
        if (enforceSpecificVariablePermission) {
            runtimePermissions.add(TaskPermissions.READ_VARIABLE);
        }
        return runtimePermissions.toArray(new Permission[0]);
    }
    
    protected boolean isHistoricInstancePermissionsEnabled() {
        return Context.getProcessEngineConfiguration().isEnableHistoricInstancePermissions();
    }
    
    protected AuthorizationManager getAuthorizationManager() {
        final CommandContext commandContext = Context.getCommandContext();
        return commandContext.getAuthorizationManager();
    }
    
    protected AuthorizationEntity getGrantAuthorization(final String taskId, final String userId, final String groupId, final Resource resource) {
        if (groupId != null) {
            return this.getGrantAuthorizationByGroupId(groupId, resource, taskId);
        }
        return this.getGrantAuthorizationByUserId(userId, resource, taskId);
    }
    
    protected AuthorizationEntity getGrantAuthorizationByUserId(final String userId, final Resource resource, final String resourceId) {
        final AuthorizationManager authorizationManager = this.getAuthorizationManager();
        return authorizationManager.findAuthorizationByUserIdAndResourceId(1, userId, resource, resourceId);
    }
    
    protected AuthorizationEntity getGrantAuthorizationByGroupId(final String groupId, final Resource resource, final String resourceId) {
        final AuthorizationManager authorizationManager = this.getAuthorizationManager();
        return authorizationManager.findAuthorizationByGroupIdAndResourceId(1, groupId, resource, resourceId);
    }
    
    protected AuthorizationEntity createAuthorization(final String userId, final String groupId, final Resource resource, final String resourceId, final Permission... permissions) {
        final AuthorizationEntity authorization = this.createGrantAuthorization(userId, groupId, resource, resourceId, permissions);
        this.updateAuthorizationBasedOnCacheEntries(authorization, userId, groupId, resource, resourceId);
        return authorization;
    }
    
    protected void addPermissions(final AuthorizationEntity authorization, final Permission... permissions) {
        if (permissions != null) {
            for (final Permission permission : permissions) {
                if (permission != null) {
                    authorization.addPermission(permission);
                }
            }
        }
    }
    
    protected AuthorizationEntity createGrantAuthorization(final String userId, final String groupId, final Resource resource, final String resourceId, final Permission... permissions) {
        if (userId != null) {
            EnsureUtil.ensureValidIndividualResourceId("Cannot create authorization for user " + userId, userId);
        }
        if (groupId != null) {
            EnsureUtil.ensureValidIndividualResourceId("Cannot create authorization for group " + groupId, groupId);
        }
        final AuthorizationEntity authorization = new AuthorizationEntity(1);
        authorization.setUserId(userId);
        authorization.setGroupId(groupId);
        authorization.setResource(resource);
        authorization.setResourceId(resourceId);
        this.addPermissions(authorization, permissions);
        return authorization;
    }
    
    protected Permission getDefaultUserPermissionForTask() {
        return Context.getProcessEngineConfiguration().getDefaultUserPermissionForTask();
    }
    
    protected boolean isEnforceSpecificVariablePermission() {
        return Context.getProcessEngineConfiguration().isEnforceSpecificVariablePermission();
    }
    
    protected void updateAuthorizationBasedOnCacheEntries(final AuthorizationEntity authorization, final String userId, final String groupId, final Resource resource, final String resourceId) {
        final DbEntityManager dbManager = Context.getCommandContext().getDbEntityManager();
        final List<AuthorizationEntity> list = dbManager.getCachedEntitiesByType(AuthorizationEntity.class);
        for (final AuthorizationEntity authEntity : list) {
            final boolean hasSameAuthRights = this.hasEntitySameAuthorizationRights(authEntity, userId, groupId, resource, resourceId);
            if (hasSameAuthRights) {
                final int previousPermissions = authEntity.getPermissions();
                authorization.setPermissions(previousPermissions);
                dbManager.getDbEntityCache().remove(authEntity);
            }
        }
    }
    
    protected boolean hasEntitySameAuthorizationRights(final AuthorizationEntity authEntity, final String userId, final String groupId, final Resource resource, final String resourceId) {
        final boolean sameUserId = this.areIdsEqual(authEntity.getUserId(), userId);
        final boolean sameGroupId = this.areIdsEqual(authEntity.getGroupId(), groupId);
        final boolean sameResourceId = this.areIdsEqual(authEntity.getResourceId(), resourceId);
        final boolean sameResourceType = authEntity.getResourceType() == resource.resourceType();
        final boolean sameAuthorizationType = authEntity.getAuthorizationType() == 1;
        return sameUserId && sameGroupId && sameResourceType && sameResourceId && sameAuthorizationType;
    }
    
    protected boolean areIdsEqual(final String firstId, final String secondId) {
        if (firstId == null || secondId == null) {
            return firstId == secondId;
        }
        return firstId.equals(secondId);
    }
}
