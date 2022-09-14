// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.Collection;
import java.util.HashSet;
import org.zik.bpm.engine.impl.batch.BatchStatisticsQueryImpl;
import org.zik.bpm.engine.impl.batch.BatchQueryImpl;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionQueryImpl;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionQueryImpl;
import org.zik.bpm.engine.impl.ExternalTaskQueryImpl;
import org.zik.bpm.engine.impl.ActivityStatisticsQueryImpl;
import org.zik.bpm.engine.impl.ProcessDefinitionStatisticsQueryImpl;
import org.zik.bpm.engine.impl.DeploymentStatisticsQueryImpl;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchQueryImpl;
import org.zik.bpm.engine.impl.UserOperationLogQueryImpl;
import org.zik.bpm.engine.impl.HistoricExternalTaskLogQueryImpl;
import org.zik.bpm.engine.impl.HistoricDecisionInstanceQueryImpl;
import org.zik.bpm.engine.impl.HistoricIdentityLinkLogQueryImpl;
import org.zik.bpm.engine.impl.HistoricIncidentQueryImpl;
import org.zik.bpm.engine.impl.HistoricJobLogQueryImpl;
import org.zik.bpm.engine.impl.persistence.entity.util.AuthManagerUtil;
import org.zik.bpm.engine.impl.HistoricDetailQueryImpl;
import org.zik.bpm.engine.impl.HistoricVariableInstanceQueryImpl;
import org.zik.bpm.engine.authorization.HistoricTaskPermissions;
import org.zik.bpm.engine.impl.HistoricTaskInstanceQueryImpl;
import org.zik.bpm.engine.impl.HistoricActivityInstanceQueryImpl;
import org.zik.bpm.engine.authorization.HistoricProcessInstancePermissions;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.JobQueryImpl;
import org.zik.bpm.engine.impl.JobDefinitionQueryImpl;
import org.zik.bpm.engine.authorization.TaskPermissions;
import org.zik.bpm.engine.authorization.ProcessDefinitionPermissions;
import org.zik.bpm.engine.impl.VariableInstanceQueryImpl;
import org.zik.bpm.engine.impl.IncidentQueryImpl;
import org.zik.bpm.engine.impl.EventSubscriptionQueryImpl;
import org.zik.bpm.engine.impl.TaskQueryImpl;
import org.zik.bpm.engine.impl.ProcessDefinitionQueryImpl;
import org.zik.bpm.engine.impl.DeploymentQueryImpl;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.function.Consumer;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.ResourceTypeUtil;
import java.util.Arrays;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.AuthorizationCheck;
import java.util.Iterator;
import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.AuthorizationException;
import org.zik.bpm.engine.impl.db.PermissionCheck;
import org.zik.bpm.engine.authorization.MissingAuthorization;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.CompositePermissionCheck;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.AuthorizationQueryImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.impl.db.PermissionCheckBuilder;
import java.util.Set;
import java.util.List;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class AuthorizationManager extends AbstractManager
{
    protected static final EnginePersistenceLogger LOG;
    protected static final List<String> EMPTY_LIST;
    protected Set<String> availableAuthorizedGroupIds;
    protected Boolean isRevokeAuthCheckUsed;
    
    public AuthorizationManager() {
        this.availableAuthorizedGroupIds = null;
        this.isRevokeAuthCheckUsed = null;
    }
    
    public PermissionCheckBuilder newPermissionCheckBuilder() {
        return new PermissionCheckBuilder();
    }
    
    public Authorization createNewAuthorization(final int type) {
        this.checkAuthorization(Permissions.CREATE, Resources.AUTHORIZATION, null);
        return new AuthorizationEntity(type);
    }
    
    @Override
    public void insert(final DbEntity authorization) {
        this.checkAuthorization(Permissions.CREATE, Resources.AUTHORIZATION, null);
        this.getDbEntityManager().insert(authorization);
    }
    
    public List<Authorization> selectAuthorizationByQueryCriteria(final AuthorizationQueryImpl authorizationQuery) {
        this.configureQuery(authorizationQuery, Resources.AUTHORIZATION);
        return (List<Authorization>)this.getDbEntityManager().selectList("selectAuthorizationByQueryCriteria", authorizationQuery);
    }
    
    public Long selectAuthorizationCountByQueryCriteria(final AuthorizationQueryImpl authorizationQuery) {
        this.configureQuery(authorizationQuery, Resources.AUTHORIZATION);
        return (Long)this.getDbEntityManager().selectOne("selectAuthorizationCountByQueryCriteria", authorizationQuery);
    }
    
    public AuthorizationEntity findAuthorizationByUserIdAndResourceId(final int type, final String userId, final Resource resource, final String resourceId) {
        return this.findAuthorization(type, userId, null, resource, resourceId);
    }
    
    public AuthorizationEntity findAuthorizationByGroupIdAndResourceId(final int type, final String groupId, final Resource resource, final String resourceId) {
        return this.findAuthorization(type, null, groupId, resource, resourceId);
    }
    
    public AuthorizationEntity findAuthorization(final int type, final String userId, final String groupId, final Resource resource, final String resourceId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("userId", userId);
        params.put("groupId", groupId);
        params.put("resourceId", resourceId);
        if (resource != null) {
            params.put("resourceType", resource.resourceType());
        }
        return (AuthorizationEntity)this.getDbEntityManager().selectOne("selectAuthorizationByParameters", params);
    }
    
    public void update(final AuthorizationEntity authorization) {
        this.checkAuthorization(Permissions.UPDATE, Resources.AUTHORIZATION, authorization.getId());
        this.getDbEntityManager().merge(authorization);
    }
    
    @Override
    public void delete(final DbEntity authorization) {
        this.checkAuthorization(Permissions.DELETE, Resources.AUTHORIZATION, authorization.getId());
        this.deleteAuthorizationsByResourceId(Resources.AUTHORIZATION, authorization.getId());
        super.delete(authorization);
    }
    
    public void checkAuthorization(final CompositePermissionCheck compositePermissionCheck) {
        if (this.isAuthCheckExecuted()) {
            final Authentication currentAuthentication = this.getCurrentAuthentication();
            final String userId = currentAuthentication.getUserId();
            final boolean isAuthorized = this.isAuthorized(compositePermissionCheck);
            if (!isAuthorized) {
                final List<MissingAuthorization> missingAuthorizations = new ArrayList<MissingAuthorization>();
                for (final PermissionCheck check : compositePermissionCheck.getAllPermissionChecks()) {
                    missingAuthorizations.add(new MissingAuthorization(check.getPermission().getName(), check.getResource().resourceName(), check.getResourceId()));
                }
                throw new AuthorizationException(userId, missingAuthorizations);
            }
        }
    }
    
    public void checkAuthorization(final Permission permission, final Resource resource) {
        this.checkAuthorization(permission, resource, null);
    }
    
    public void checkAuthorization(final Permission permission, final Resource resource, final String resourceId) {
        if (this.isAuthCheckExecuted()) {
            final Authentication currentAuthentication = this.getCurrentAuthentication();
            final boolean isAuthorized = this.isAuthorized(currentAuthentication.getUserId(), currentAuthentication.getGroupIds(), permission, resource, resourceId);
            if (!isAuthorized) {
                throw new AuthorizationException(currentAuthentication.getUserId(), permission.getName(), resource.resourceName(), resourceId);
            }
        }
    }
    
    public boolean isAuthorized(final Permission permission, final Resource resource, final String resourceId) {
        final Authentication currentAuthentication = this.getCurrentAuthentication();
        return !this.isAuthorizationEnabled() || currentAuthentication == null || currentAuthentication.getUserId() == null || this.isAuthorized(currentAuthentication.getUserId(), currentAuthentication.getGroupIds(), permission, resource, resourceId);
    }
    
    public boolean isAuthorized(final String userId, final List<String> groupIds, final Permission permission, final Resource resource, final String resourceId) {
        if (!this.isPermissionDisabled(permission)) {
            final PermissionCheck permCheck = new PermissionCheck();
            permCheck.setPermission(permission);
            permCheck.setResource(resource);
            permCheck.setResourceId(resourceId);
            return this.isAuthorized(userId, groupIds, permCheck);
        }
        return true;
    }
    
    public boolean isAuthorized(final String userId, final List<String> groupIds, final PermissionCheck permissionCheck) {
        if (!this.isAuthorizationEnabled()) {
            return true;
        }
        if (!this.isResourceValidForPermission(permissionCheck)) {
            throw AuthorizationManager.LOG.invalidResourceForPermission(permissionCheck.getResource().resourceName(), permissionCheck.getPermission().getName());
        }
        final List<String> filteredGroupIds = this.filterAuthenticatedGroupIds(groupIds);
        final boolean isRevokeAuthorizationCheckEnabled = this.isRevokeAuthCheckEnabled(userId, groupIds);
        final CompositePermissionCheck compositePermissionCheck = this.createCompositePermissionCheck(permissionCheck);
        final AuthorizationCheck authCheck = new AuthorizationCheck(userId, filteredGroupIds, compositePermissionCheck, isRevokeAuthorizationCheckEnabled);
        return this.getDbEntityManager().selectBoolean("isUserAuthorizedForResource", authCheck);
    }
    
    protected boolean isRevokeAuthCheckEnabled(final String userId, final List<String> groupIds) {
        Boolean isRevokeAuthCheckEnabled = this.isRevokeAuthCheckUsed;
        if (isRevokeAuthCheckEnabled == null) {
            String configuredMode = Context.getProcessEngineConfiguration().getAuthorizationCheckRevokes();
            if (configuredMode != null) {
                configuredMode = configuredMode.toLowerCase();
            }
            if ("always".equals(configuredMode)) {
                isRevokeAuthCheckEnabled = true;
            }
            else if ("never".equals(configuredMode)) {
                isRevokeAuthCheckEnabled = false;
            }
            else {
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("userId", userId);
                params.put("authGroupIds", this.filterAuthenticatedGroupIds(groupIds));
                isRevokeAuthCheckEnabled = this.getDbEntityManager().selectBoolean("selectRevokeAuthorization", params);
            }
            this.isRevokeAuthCheckUsed = isRevokeAuthCheckEnabled;
        }
        return isRevokeAuthCheckEnabled;
    }
    
    protected CompositePermissionCheck createCompositePermissionCheck(final PermissionCheck permissionCheck) {
        final CompositePermissionCheck compositePermissionCheck = new CompositePermissionCheck();
        compositePermissionCheck.setAtomicChecks(Arrays.asList(permissionCheck));
        return compositePermissionCheck;
    }
    
    public boolean isAuthorized(final String userId, final List<String> groupIds, final CompositePermissionCheck compositePermissionCheck) {
        for (final PermissionCheck permissionCheck : compositePermissionCheck.getAllPermissionChecks()) {
            if (!this.isResourceValidForPermission(permissionCheck)) {
                throw AuthorizationManager.LOG.invalidResourceForPermission(permissionCheck.getResource().resourceName(), permissionCheck.getPermission().getName());
            }
        }
        final List<String> filteredGroupIds = this.filterAuthenticatedGroupIds(groupIds);
        final boolean isRevokeAuthorizationCheckEnabled = this.isRevokeAuthCheckEnabled(userId, groupIds);
        final AuthorizationCheck authCheck = new AuthorizationCheck(userId, filteredGroupIds, compositePermissionCheck, isRevokeAuthorizationCheckEnabled);
        return this.getDbEntityManager().selectBoolean("isUserAuthorizedForResource", authCheck);
    }
    
    public boolean isAuthorized(final CompositePermissionCheck compositePermissionCheck) {
        final Authentication currentAuthentication = this.getCurrentAuthentication();
        return currentAuthentication == null || this.isAuthorized(currentAuthentication.getUserId(), currentAuthentication.getGroupIds(), compositePermissionCheck);
    }
    
    protected boolean isResourceValidForPermission(final PermissionCheck permissionCheck) {
        final Resource[] permissionResources = permissionCheck.getPermission().getTypes();
        final Resource givenResource = permissionCheck.getResource();
        return ResourceTypeUtil.resourceIsContainedInArray(givenResource.resourceType(), permissionResources);
    }
    
    public void validateResourceCompatibility(final AuthorizationEntity authorization) {
        final int resourceType = authorization.getResourceType();
        final Set<Permission> permissionSet = authorization.getCachedPermissions();
        for (final Permission permission : permissionSet) {
            if (!ResourceTypeUtil.resourceIsContainedInArray(resourceType, permission.getTypes())) {
                throw AuthorizationManager.LOG.invalidResourceForAuthorization(resourceType, permission.getName());
            }
        }
    }
    
    public void configureQuery(final ListQueryParameterObject query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        authCheck.getPermissionChecks().clear();
        if (this.isAuthCheckExecuted()) {
            final Authentication currentAuthentication = this.getCurrentAuthentication();
            authCheck.setAuthUserId(currentAuthentication.getUserId());
            authCheck.setAuthGroupIds(currentAuthentication.getGroupIds());
            this.enableQueryAuthCheck(authCheck);
        }
        else {
            authCheck.setAuthorizationCheckEnabled(false);
            authCheck.setAuthUserId(null);
            authCheck.setAuthGroupIds(null);
        }
    }
    
    public void configureQueryHistoricFinishedInstanceReport(final ListQueryParameterObject query, final Resource resource) {
        this.configureQuery(query);
        final CompositePermissionCheck compositePermissionCheck = new PermissionCheckBuilder().conjunctive().atomicCheck(resource, "RES.KEY_", Permissions.READ).atomicCheck(resource, "RES.KEY_", Permissions.READ_HISTORY).build();
        query.getAuthCheck().setPermissionChecks(compositePermissionCheck);
    }
    
    public void enableQueryAuthCheck(final AuthorizationCheck authCheck) {
        final List<String> authGroupIds = authCheck.getAuthGroupIds();
        final String authUserId = authCheck.getAuthUserId();
        authCheck.setAuthorizationCheckEnabled(true);
        authCheck.setAuthGroupIds(this.filterAuthenticatedGroupIds(authGroupIds));
        authCheck.setRevokeAuthorizationCheckEnabled(this.isRevokeAuthCheckEnabled(authUserId, authGroupIds));
    }
    
    public void configureQuery(final AbstractQuery query, final Resource resource) {
        this.configureQuery(query, resource, "RES.ID_");
    }
    
    public void configureQuery(final AbstractQuery query, final Resource resource, final String queryParam) {
        this.configureQuery(query, resource, queryParam, Permissions.READ);
    }
    
    public void configureQuery(final AbstractQuery query, final Resource resource, final String queryParam, final Permission permission) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().atomicCheck(resource, queryParam, permission).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public boolean isPermissionDisabled(final Permission permission) {
        final List<String> disabledPermissions = this.getCommandContext().getProcessEngineConfiguration().getDisabledPermissions();
        if (disabledPermissions != null) {
            for (final String disabledPermission : disabledPermissions) {
                if (permission.getName().equals(disabledPermission)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void addPermissionCheck(final AuthorizationCheck authCheck, final CompositePermissionCheck compositeCheck) {
        final CommandContext commandContext = this.getCommandContext();
        if (this.isAuthorizationEnabled() && this.getCurrentAuthentication() != null && commandContext.isAuthorizationCheckEnabled()) {
            authCheck.setPermissionChecks(compositeCheck);
        }
    }
    
    public void deleteAuthorizationsByResourceIds(final Resources resource, final List<String> resourceIds) {
        if (resourceIds == null) {
            throw new IllegalArgumentException("Resource ids cannot be null");
        }
        resourceIds.forEach(resourceId -> this.deleteAuthorizationsByResourceId(resource, resourceId));
    }
    
    public void deleteAuthorizationsByResourceId(final Resource resource, final String resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource id cannot be null");
        }
        if (this.isAuthorizationEnabled()) {
            final Map<String, Object> deleteParams = new HashMap<String, Object>();
            deleteParams.put("resourceType", resource.resourceType());
            deleteParams.put("resourceId", resourceId);
            this.getDbEntityManager().delete(AuthorizationEntity.class, "deleteAuthorizationsForResourceId", deleteParams);
        }
    }
    
    public void deleteAuthorizationsByResourceIdAndUserId(final Resource resource, final String resourceId, final String userId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource id cannot be null");
        }
        if (this.isAuthorizationEnabled()) {
            final Map<String, Object> deleteParams = new HashMap<String, Object>();
            deleteParams.put("resourceType", resource.resourceType());
            deleteParams.put("resourceId", resourceId);
            deleteParams.put("userId", userId);
            this.getDbEntityManager().delete(AuthorizationEntity.class, "deleteAuthorizationsForResourceId", deleteParams);
        }
    }
    
    public void deleteAuthorizationsByResourceIdAndGroupId(final Resource resource, final String resourceId, final String groupId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource id cannot be null");
        }
        if (this.isAuthorizationEnabled()) {
            final Map<String, Object> deleteParams = new HashMap<String, Object>();
            deleteParams.put("resourceType", resource.resourceType());
            deleteParams.put("resourceId", resourceId);
            deleteParams.put("groupId", groupId);
            this.getDbEntityManager().delete(AuthorizationEntity.class, "deleteAuthorizationsForResourceId", deleteParams);
        }
    }
    
    public void checkCamundaAdmin() {
        final Authentication currentAuthentication = this.getCurrentAuthentication();
        if (this.isAuthorizationEnabled() && this.getCommandContext().isAuthorizationCheckEnabled() && currentAuthentication != null && !this.isCamundaAdmin(currentAuthentication)) {
            throw AuthorizationManager.LOG.requiredCamundaAdmin();
        }
    }
    
    public void checkCamundaAdminOrPermission(final Consumer<CommandChecker> permissionCheck) {
        if (this.isAuthorizationEnabled() && this.getCommandContext().isAuthorizationCheckEnabled()) {
            AuthorizationException authorizationException = null;
            AuthorizationException adminException = null;
            try {
                for (final CommandChecker checker : this.getCommandContext().getProcessEngineConfiguration().getCommandCheckers()) {
                    permissionCheck.accept(checker);
                }
            }
            catch (AuthorizationException e) {
                authorizationException = e;
            }
            try {
                this.checkCamundaAdmin();
            }
            catch (AuthorizationException e) {
                adminException = e;
            }
            if (authorizationException != null && adminException != null) {
                final List<MissingAuthorization> info = authorizationException.getMissingAuthorizations();
                throw AuthorizationManager.LOG.requiredCamundaAdminOrPermissionException(info);
            }
        }
    }
    
    public boolean isCamundaAdmin(final Authentication authentication) {
        final List<String> groupIds = authentication.getGroupIds();
        if (groupIds != null) {
            final CommandContext commandContext = Context.getCommandContext();
            final List<String> adminGroups = commandContext.getProcessEngineConfiguration().getAdminGroups();
            for (final String adminGroup : adminGroups) {
                if (groupIds.contains(adminGroup)) {
                    return true;
                }
            }
        }
        final String userId = authentication.getUserId();
        if (userId != null) {
            final CommandContext commandContext2 = Context.getCommandContext();
            final List<String> adminUsers = commandContext2.getProcessEngineConfiguration().getAdminUsers();
            return adminUsers != null && adminUsers.contains(userId);
        }
        return false;
    }
    
    public void configureDeploymentQuery(final DeploymentQueryImpl query) {
        this.configureQuery(query, Resources.DEPLOYMENT);
    }
    
    public void configureProcessDefinitionQuery(final ProcessDefinitionQueryImpl query) {
        this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.KEY_");
        if (query.isStartablePermissionCheck()) {
            final AuthorizationCheck authorizationCheck = query.getAuthCheck();
            if (!authorizationCheck.isRevokeAuthorizationCheckEnabled()) {
                final CompositePermissionCheck permCheck = new PermissionCheckBuilder().atomicCheck(Resources.PROCESS_DEFINITION, "RES.KEY_", Permissions.CREATE_INSTANCE).build();
                query.addProcessDefinitionCreatePermissionCheck(permCheck);
            }
            else {
                final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().conjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.KEY_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "RES.KEY_", Permissions.CREATE_INSTANCE).build();
                this.addPermissionCheck(authorizationCheck, permissionCheck);
            }
        }
    }
    
    public void configureExecutionQuery(final AbstractQuery query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "P.KEY_", Permissions.READ_INSTANCE).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public void configureTaskQuery(final TaskQueryImpl query) {
        this.configureQuery(query);
        if (query.getAuthCheck().isAuthorizationCheckEnabled()) {
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.TASK, "RES.ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "D.KEY_", Permissions.READ_TASK).build();
            this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
        }
    }
    
    public void configureEventSubscriptionQuery(final EventSubscriptionQueryImpl query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public void configureConditionalEventSubscriptionQuery(final ListQueryParameterObject query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().atomicCheck(Resources.PROCESS_DEFINITION, "P.KEY_", Permissions.READ).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public void configureIncidentQuery(final IncidentQueryImpl query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    protected void configureVariableInstanceQuery(final VariableInstanceQueryImpl query) {
        this.configureQuery(query);
        if (query.getAuthCheck().isAuthorizationCheckEnabled()) {
            CompositePermissionCheck permissionCheck;
            if (this.isEnsureSpecificVariablePermission()) {
                permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", ProcessDefinitionPermissions.READ_INSTANCE_VARIABLE).atomicCheck(Resources.TASK, "RES.TASK_ID_", TaskPermissions.READ_VARIABLE).build();
            }
            else {
                permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).atomicCheck(Resources.TASK, "RES.TASK_ID_", Permissions.READ).build();
            }
            this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
        }
    }
    
    public void configureJobDefinitionQuery(final JobDefinitionQueryImpl query) {
        this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_");
    }
    
    public void configureJobQuery(final JobQueryImpl query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROCESS_INSTANCE_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROCESS_DEF_KEY_", Permissions.READ_INSTANCE).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public void configureHistoricProcessInstanceQuery(final HistoricProcessInstanceQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "SELF.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "SELF.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "SELF.ID_", HistoricProcessInstancePermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricActivityInstanceQuery(final HistoricActivityInstanceQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricTaskInstanceQuery(final HistoricTaskInstanceQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).atomicCheck(Resources.HISTORIC_TASK, "RES.ID_", HistoricTaskPermissions.READ).build();
            this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
        }
    }
    
    public void configureHistoricVariableInstanceQuery(final HistoricVariableInstanceQueryImpl query) {
        this.configureHistoricVariableAndDetailQuery(query);
    }
    
    public void configureHistoricDetailQuery(final HistoricDetailQueryImpl query) {
        this.configureHistoricVariableAndDetailQuery(query);
    }
    
    protected void configureHistoricVariableAndDetailQuery(final AbstractQuery query) {
        final boolean ensureSpecificVariablePermission = this.isEnsureSpecificVariablePermission();
        final AuthManagerUtil.VariablePermissions variablePermissions = AuthManagerUtil.getVariablePermissions(ensureSpecificVariablePermission);
        final Permission processDefinitionPermission = variablePermissions.getProcessDefinitionPermission();
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", processDefinitionPermission);
        }
        else {
            this.configureQuery(query);
            final Permission historicTaskPermission = variablePermissions.getHistoricTaskPermission();
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", processDefinitionPermission).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).atomicCheck(Resources.HISTORIC_TASK, "TI.ID_", historicTaskPermission).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricJobLogQuery(final HistoricJobLogQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROCESS_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROCESS_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROCESS_INSTANCE_ID_", HistoricProcessInstancePermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricIncidentQuery(final HistoricIncidentQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricIdentityLinkQuery(final HistoricIdentityLinkLogQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "TI.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).atomicCheck(Resources.HISTORIC_TASK, "RES.TASK_ID_", HistoricTaskPermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureHistoricDecisionInstanceQuery(final HistoricDecisionInstanceQueryImpl query) {
        this.configureQuery(query, Resources.DECISION_DEFINITION, "RES.DEC_DEF_KEY_", Permissions.READ_HISTORY);
    }
    
    public void configureHistoricExternalTaskLogQuery(final HistoricExternalTaskLogQueryImpl query) {
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (!isHistoricInstancePermissionsEnabled) {
            this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY);
        }
        else {
            this.configureQuery(query);
            final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).build();
            this.addPermissionCheck(authCheck, permissionCheck);
        }
    }
    
    public void configureUserOperationLogQuery(final UserOperationLogQueryImpl query) {
        this.configureQuery(query);
        final PermissionCheckBuilder permissionCheckBuilder = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_HISTORY).atomicCheck(Resources.OPERATION_LOG_CATEGORY, "RES.CATEGORY_", Permissions.READ);
        final AuthorizationCheck authCheck = query.getAuthCheck();
        final boolean isHistoricInstancePermissionsEnabled = this.isHistoricInstancePermissionsEnabled();
        authCheck.setHistoricInstancePermissionsEnabled(isHistoricInstancePermissionsEnabled);
        if (isHistoricInstancePermissionsEnabled) {
            permissionCheckBuilder.atomicCheck(Resources.HISTORIC_PROCESS_INSTANCE, "RES.PROC_INST_ID_", HistoricProcessInstancePermissions.READ).atomicCheck(Resources.HISTORIC_TASK, "RES.TASK_ID_", HistoricTaskPermissions.READ);
        }
        final CompositePermissionCheck permissionCheck = permissionCheckBuilder.build();
        this.addPermissionCheck(authCheck, permissionCheck);
    }
    
    public void configureHistoricBatchQuery(final HistoricBatchQueryImpl query) {
        this.configureQuery(query, Resources.BATCH, "RES.ID_", Permissions.READ_HISTORY);
    }
    
    public void configureDeploymentStatisticsQuery(final DeploymentStatisticsQueryImpl query) {
        this.configureQuery(query, Resources.DEPLOYMENT, "RES.ID_");
        query.getProcessInstancePermissionChecks().clear();
        query.getJobPermissionChecks().clear();
        query.getIncidentPermissionChecks().clear();
        if (query.getAuthCheck().isAuthorizationCheckEnabled()) {
            final CompositePermissionCheck processInstancePermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "EXECUTION.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).build();
            query.addProcessInstancePermissionCheck(processInstancePermissionCheck.getAllPermissionChecks());
            if (query.isFailedJobsToInclude()) {
                final CompositePermissionCheck jobPermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "JOB.PROCESS_INSTANCE_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "JOB.PROCESS_DEF_KEY_", Permissions.READ_INSTANCE).build();
                query.addJobPermissionCheck(jobPermissionCheck.getAllPermissionChecks());
            }
            if (query.isIncidentsToInclude()) {
                final CompositePermissionCheck incidentPermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "INC.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).build();
                query.addIncidentPermissionCheck(incidentPermissionCheck.getAllPermissionChecks());
            }
        }
    }
    
    public void configureProcessDefinitionStatisticsQuery(final ProcessDefinitionStatisticsQueryImpl query) {
        this.configureQuery(query, Resources.PROCESS_DEFINITION, "RES.KEY_");
    }
    
    public void configureActivityStatisticsQuery(final ActivityStatisticsQueryImpl query) {
        this.configureQuery(query);
        query.getProcessInstancePermissionChecks().clear();
        query.getJobPermissionChecks().clear();
        query.getIncidentPermissionChecks().clear();
        if (query.getAuthCheck().isAuthorizationCheckEnabled()) {
            final CompositePermissionCheck processInstancePermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "E.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "P.KEY_", Permissions.READ_INSTANCE).build();
            query.getAuthCheck().setPermissionChecks(processInstancePermissionCheck);
            query.addProcessInstancePermissionCheck(processInstancePermissionCheck.getAllPermissionChecks());
            if (query.isFailedJobsToInclude()) {
                final CompositePermissionCheck jobPermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "JOB.PROCESS_INSTANCE_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "JOB.PROCESS_DEF_KEY_", Permissions.READ_INSTANCE).build();
                query.getAuthCheck().setPermissionChecks(jobPermissionCheck);
                query.addJobPermissionCheck(jobPermissionCheck.getAllPermissionChecks());
            }
            if (query.isIncidentsToInclude()) {
                final CompositePermissionCheck incidentPermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "I.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "PROCDEF.KEY_", Permissions.READ_INSTANCE).build();
                query.getAuthCheck().setPermissionChecks(incidentPermissionCheck);
                query.addIncidentPermissionCheck(incidentPermissionCheck.getAllPermissionChecks());
            }
        }
    }
    
    public void configureExternalTaskQuery(final ExternalTaskQueryImpl query) {
        this.configureQuery(query);
        final CompositePermissionCheck permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_INSTANCE).build();
        this.addPermissionCheck(query.getAuthCheck(), permissionCheck);
    }
    
    public void configureExternalTaskFetch(final ListQueryParameterObject parameter) {
        this.configureQuery(parameter);
        final CompositePermissionCheck permissionCheck = this.newPermissionCheckBuilder().conjunctive().composite().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.READ).atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.READ_INSTANCE).done().composite().disjunctive().atomicCheck(Resources.PROCESS_INSTANCE, "RES.PROC_INST_ID_", Permissions.UPDATE).atomicCheck(Resources.PROCESS_DEFINITION, "RES.PROC_DEF_KEY_", Permissions.UPDATE_INSTANCE).done().build();
        this.addPermissionCheck(parameter.getAuthCheck(), permissionCheck);
    }
    
    public void configureDecisionDefinitionQuery(final DecisionDefinitionQueryImpl query) {
        this.configureQuery(query, Resources.DECISION_DEFINITION, "RES.KEY_");
    }
    
    public void configureDecisionRequirementsDefinitionQuery(final DecisionRequirementsDefinitionQueryImpl query) {
        this.configureQuery(query, Resources.DECISION_REQUIREMENTS_DEFINITION, "RES.KEY_");
    }
    
    public void configureBatchQuery(final BatchQueryImpl query) {
        this.configureQuery(query, Resources.BATCH, "RES.ID_", Permissions.READ);
    }
    
    public void configureBatchStatisticsQuery(final BatchStatisticsQueryImpl query) {
        this.configureQuery(query, Resources.BATCH, "RES.ID_", Permissions.READ);
    }
    
    public List<String> filterAuthenticatedGroupIds(final List<String> authenticatedGroupIds) {
        if (authenticatedGroupIds == null || authenticatedGroupIds.isEmpty()) {
            return AuthorizationManager.EMPTY_LIST;
        }
        final Set<String> groupIntersection = new HashSet<String>(this.getAllGroups());
        groupIntersection.retainAll(authenticatedGroupIds);
        return new ArrayList<String>(groupIntersection);
    }
    
    protected Set<String> getAllGroups() {
        if (this.availableAuthorizedGroupIds == null) {
            this.availableAuthorizedGroupIds = new HashSet<String>();
            final List<String> groupsFromDatabase = (List<String>)this.getDbEntityManager().selectList("selectAuthorizedGroupIds");
            final Stream<String> filter = groupsFromDatabase.stream().filter(Objects::nonNull);
            final Set<String> availableAuthorizedGroupIds = this.availableAuthorizedGroupIds;
            Objects.requireNonNull(availableAuthorizedGroupIds);
            filter.forEach(availableAuthorizedGroupIds::add);
        }
        return this.availableAuthorizedGroupIds;
    }
    
    protected boolean isAuthCheckExecuted() {
        final Authentication currentAuthentication = this.getCurrentAuthentication();
        final CommandContext commandContext = Context.getCommandContext();
        return this.isAuthorizationEnabled() && commandContext.isAuthorizationCheckEnabled() && currentAuthentication != null && currentAuthentication.getUserId() != null;
    }
    
    public boolean isEnsureSpecificVariablePermission() {
        return Context.getProcessEngineConfiguration().isEnforceSpecificVariablePermission();
    }
    
    protected boolean isHistoricInstancePermissionsEnabled() {
        return Context.getProcessEngineConfiguration().isEnableHistoricInstancePermissions();
    }
    
    public void addRemovalTimeToAuthorizationsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(AuthorizationEntity.class, "updateAuthorizationsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToAuthorizationsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(AuthorizationEntity.class, "updateAuthorizationsByProcessInstanceId", parameters);
    }
    
    public DbOperation deleteAuthorizationsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(AuthorizationEntity.class, "deleteAuthorizationsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
        EMPTY_LIST = new ArrayList<String>();
    }
}
