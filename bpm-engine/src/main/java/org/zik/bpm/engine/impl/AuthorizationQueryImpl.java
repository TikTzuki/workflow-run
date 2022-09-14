// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.ResourceTypeUtil;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.Arrays;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.HashSet;
import org.zik.bpm.engine.authorization.Resource;
import java.util.Set;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.authorization.AuthorizationQuery;

public class AuthorizationQueryImpl extends AbstractQuery<AuthorizationQuery, Authorization> implements AuthorizationQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] userIds;
    protected String[] groupIds;
    protected int resourceType;
    protected String resourceId;
    protected int permission;
    protected Integer authorizationType;
    protected boolean queryByPermission;
    protected boolean queryByResourceType;
    private Set<Resource> resourcesIntersection;
    
    public AuthorizationQueryImpl() {
        this.permission = 0;
        this.queryByPermission = false;
        this.queryByResourceType = false;
        this.resourcesIntersection = new HashSet<Resource>();
    }
    
    public AuthorizationQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.permission = 0;
        this.queryByPermission = false;
        this.queryByResourceType = false;
        this.resourcesIntersection = new HashSet<Resource>();
    }
    
    @Override
    public AuthorizationQuery authorizationId(final String id) {
        this.id = id;
        return this;
    }
    
    @Override
    public AuthorizationQuery userIdIn(final String... userIdIn) {
        if (this.groupIds != null) {
            throw new ProcessEngineException("Cannot query for user and group authorizations at the same time.");
        }
        this.userIds = userIdIn;
        return this;
    }
    
    @Override
    public AuthorizationQuery groupIdIn(final String... groupIdIn) {
        if (this.userIds != null) {
            throw new ProcessEngineException("Cannot query for user and group authorizations at the same time.");
        }
        this.groupIds = groupIdIn;
        return this;
    }
    
    @Override
    public AuthorizationQuery resourceType(final Resource resource) {
        return this.resourceType(resource.resourceType());
    }
    
    @Override
    public AuthorizationQuery resourceType(final int resourceType) {
        this.resourceType = resourceType;
        this.queryByResourceType = true;
        return this;
    }
    
    @Override
    public AuthorizationQuery resourceId(final String resourceId) {
        this.resourceId = resourceId;
        return this;
    }
    
    @Override
    public AuthorizationQuery hasPermission(final Permission p) {
        this.queryByPermission = true;
        if (this.resourcesIntersection.size() == 0) {
            this.resourcesIntersection.addAll(Arrays.asList(p.getTypes()));
        }
        else {
            this.resourcesIntersection.retainAll(new HashSet<Object>(Arrays.asList(p.getTypes())));
        }
        this.permission |= p.getValue();
        return this;
    }
    
    @Override
    public AuthorizationQuery authorizationType(final Integer type) {
        this.authorizationType = type;
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getAuthorizationManager().selectAuthorizationCountByQueryCriteria(this);
    }
    
    @Override
    public List<Authorization> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getAuthorizationManager().selectAuthorizationByQueryCriteria(this);
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || this.containsIncompatiblePermissions() || this.containsIncompatibleResourceType();
    }
    
    private boolean containsIncompatiblePermissions() {
        return this.queryByPermission && this.resourcesIntersection.isEmpty();
    }
    
    private boolean containsIncompatibleResourceType() {
        if (this.queryByResourceType && this.queryByPermission) {
            final Resource[] resources = this.resourcesIntersection.toArray(new Resource[this.resourcesIntersection.size()]);
            return !ResourceTypeUtil.resourceIsContainedInArray(this.resourceType, resources);
        }
        return false;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean isQueryByPermission() {
        return this.queryByPermission;
    }
    
    public String[] getUserIds() {
        return this.userIds;
    }
    
    public String[] getGroupIds() {
        return this.groupIds;
    }
    
    public int getResourceType() {
        return this.resourceType;
    }
    
    public String getResourceId() {
        return this.resourceId;
    }
    
    public int getPermission() {
        return this.permission;
    }
    
    public boolean isQueryByResourceType() {
        return this.queryByResourceType;
    }
    
    public Set<Resource> getResourcesIntersection() {
        return this.resourcesIntersection;
    }
    
    @Override
    public AuthorizationQuery orderByResourceType() {
        this.orderBy(AuthorizationQueryProperty.RESOURCE_TYPE);
        return this;
    }
    
    @Override
    public AuthorizationQuery orderByResourceId() {
        this.orderBy(AuthorizationQueryProperty.RESOURCE_ID);
        return this;
    }
}
