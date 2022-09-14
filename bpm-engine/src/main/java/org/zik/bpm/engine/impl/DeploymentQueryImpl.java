// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.repository.DeploymentQuery;

public class DeploymentQueryImpl extends AbstractQuery<DeploymentQuery, Deployment> implements DeploymentQuery, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    protected String name;
    protected String nameLike;
    protected boolean sourceQueryParamEnabled;
    protected String source;
    protected Date deploymentBefore;
    protected Date deploymentAfter;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeDeploymentsWithoutTenantId;
    
    public DeploymentQueryImpl() {
        this.isTenantIdSet = false;
        this.includeDeploymentsWithoutTenantId = false;
    }
    
    public DeploymentQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.includeDeploymentsWithoutTenantId = false;
    }
    
    @Override
    public DeploymentQueryImpl deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull("Deployment id", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public DeploymentQueryImpl deploymentName(final String deploymentName) {
        EnsureUtil.ensureNotNull("deploymentName", (Object)deploymentName);
        this.name = deploymentName;
        return this;
    }
    
    @Override
    public DeploymentQueryImpl deploymentNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("deploymentNameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public DeploymentQuery deploymentSource(final String source) {
        this.sourceQueryParamEnabled = true;
        this.source = source;
        return this;
    }
    
    @Override
    public DeploymentQuery deploymentBefore(final Date before) {
        EnsureUtil.ensureNotNull("deploymentBefore", before);
        this.deploymentBefore = before;
        return this;
    }
    
    @Override
    public DeploymentQuery deploymentAfter(final Date after) {
        EnsureUtil.ensureNotNull("deploymentAfter", after);
        this.deploymentAfter = after;
        return this;
    }
    
    @Override
    public DeploymentQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DeploymentQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public DeploymentQuery includeDeploymentsWithoutTenantId() {
        this.includeDeploymentsWithoutTenantId = true;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.deploymentAfter, this.deploymentBefore);
    }
    
    @Override
    public DeploymentQuery orderByDeploymentId() {
        return ((AbstractQuery<DeploymentQuery, U>)this).orderBy(DeploymentQueryProperty.DEPLOYMENT_ID);
    }
    
    @Override
    public DeploymentQuery orderByDeploymenTime() {
        return ((AbstractQuery<DeploymentQuery, U>)this).orderBy(DeploymentQueryProperty.DEPLOY_TIME);
    }
    
    @Override
    public DeploymentQuery orderByDeploymentTime() {
        return ((AbstractQuery<DeploymentQuery, U>)this).orderBy(DeploymentQueryProperty.DEPLOY_TIME);
    }
    
    @Override
    public DeploymentQuery orderByDeploymentName() {
        return ((AbstractQuery<DeploymentQuery, U>)this).orderBy(DeploymentQueryProperty.DEPLOYMENT_NAME);
    }
    
    @Override
    public DeploymentQuery orderByTenantId() {
        return ((AbstractQuery<DeploymentQuery, U>)this).orderBy(DeploymentQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getDeploymentManager().findDeploymentCountByQueryCriteria(this);
    }
    
    @Override
    public List<Deployment> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getDeploymentManager().findDeploymentsByQueryCriteria(this, page);
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameLike() {
        return this.nameLike;
    }
    
    public boolean isSourceQueryParamEnabled() {
        return this.sourceQueryParamEnabled;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public Date getDeploymentBefore() {
        return this.deploymentBefore;
    }
    
    public Date getDeploymentAfter() {
        return this.deploymentAfter;
    }
}
