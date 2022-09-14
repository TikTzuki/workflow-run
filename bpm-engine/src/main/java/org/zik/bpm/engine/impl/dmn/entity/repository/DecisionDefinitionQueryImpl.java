// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.repository.DecisionDefinitionQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class DecisionDefinitionQueryImpl extends AbstractQuery<DecisionDefinitionQuery, DecisionDefinition> implements DecisionDefinitionQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] ids;
    protected String category;
    protected String categoryLike;
    protected String name;
    protected String nameLike;
    protected String deploymentId;
    protected Date deployedAfter;
    protected Date deployedAt;
    protected String key;
    protected String keyLike;
    protected String resourceName;
    protected String resourceNameLike;
    protected Integer version;
    protected boolean latest;
    protected String decisionRequirementsDefinitionId;
    protected String decisionRequirementsDefinitionKey;
    protected boolean withoutDecisionRequirementsDefinition;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeDefinitionsWithoutTenantId;
    protected String versionTag;
    protected String versionTagLike;
    private boolean shouldJoinDeploymentTable;
    
    public DecisionDefinitionQueryImpl() {
        this.latest = false;
        this.withoutDecisionRequirementsDefinition = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
        this.shouldJoinDeploymentTable = false;
    }
    
    public DecisionDefinitionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.latest = false;
        this.withoutDecisionRequirementsDefinition = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
        this.shouldJoinDeploymentTable = false;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionId(final String decisionDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionId", (Object)decisionDefinitionId);
        this.id = decisionDefinitionId;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionIdIn(final String... ids) {
        this.ids = ids;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionCategory(final String decisionDefinitionCategory) {
        EnsureUtil.ensureNotNull(NotValidException.class, "category", (Object)decisionDefinitionCategory);
        this.category = decisionDefinitionCategory;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionCategoryLike(final String decisionDefinitionCategoryLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "categoryLike", (Object)decisionDefinitionCategoryLike);
        this.categoryLike = decisionDefinitionCategoryLike;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionName(final String decisionDefinitionName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "name", (Object)decisionDefinitionName);
        this.name = decisionDefinitionName;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionNameLike(final String decisionDefinitionNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "nameLike", (Object)decisionDefinitionNameLike);
        this.nameLike = decisionDefinitionNameLike;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionKey(final String decisionDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "key", (Object)decisionDefinitionKey);
        this.key = decisionDefinitionKey;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionKeyLike(final String decisionDefinitionKeyLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "keyLike", (Object)decisionDefinitionKeyLike);
        this.keyLike = decisionDefinitionKeyLike;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery deployedAfter(final Date deployedAfter) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deployedAfter", deployedAfter);
        this.shouldJoinDeploymentTable = true;
        this.deployedAfter = deployedAfter;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery deployedAt(final Date deployedAt) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deployedAt", deployedAt);
        this.shouldJoinDeploymentTable = true;
        this.deployedAt = deployedAt;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionVersion(final Integer decisionDefinitionVersion) {
        EnsureUtil.ensureNotNull(NotValidException.class, "version", decisionDefinitionVersion);
        EnsureUtil.ensurePositive(NotValidException.class, "version", (long)decisionDefinitionVersion);
        this.version = decisionDefinitionVersion;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery latestVersion() {
        this.latest = true;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionResourceName(final String resourceName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceName", (Object)resourceName);
        this.resourceName = resourceName;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionDefinitionResourceNameLike(final String resourceNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceNameLike", (Object)resourceNameLike);
        this.resourceNameLike = resourceNameLike;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionRequirementsDefinitionId(final String decisionRequirementsDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionRequirementsDefinitionId", (Object)decisionRequirementsDefinitionId);
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery decisionRequirementsDefinitionKey(final String decisionRequirementsDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionRequirementsDefinitionKey", (Object)decisionRequirementsDefinitionKey);
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery versionTag(final String versionTag) {
        EnsureUtil.ensureNotNull(NotValidException.class, "versionTag", (Object)versionTag);
        this.versionTag = versionTag;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery versionTagLike(final String versionTagLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "versionTagLike", (Object)versionTagLike);
        this.versionTagLike = versionTagLike;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery withoutDecisionRequirementsDefinition() {
        this.withoutDecisionRequirementsDefinition = true;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery includeDecisionDefinitionsWithoutTenantId() {
        this.includeDefinitionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionDefinitionCategory() {
        this.orderBy(DecisionDefinitionQueryProperty.DECISION_DEFINITION_CATEGORY);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionDefinitionKey() {
        this.orderBy(DecisionDefinitionQueryProperty.DECISION_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionDefinitionId() {
        this.orderBy(DecisionDefinitionQueryProperty.DECISION_DEFINITION_ID);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionDefinitionVersion() {
        this.orderBy(DecisionDefinitionQueryProperty.DECISION_DEFINITION_VERSION);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionDefinitionName() {
        this.orderBy(DecisionDefinitionQueryProperty.DECISION_DEFINITION_NAME);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDeploymentId() {
        this.orderBy(DecisionDefinitionQueryProperty.DEPLOYMENT_ID);
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByDeploymentTime() {
        this.shouldJoinDeploymentTable = true;
        this.orderBy(new QueryOrderingProperty("deployment", DecisionDefinitionQueryProperty.DEPLOY_TIME));
        return this;
    }
    
    @Override
    public DecisionDefinitionQuery orderByTenantId() {
        return ((AbstractQuery<DecisionDefinitionQuery, U>)this).orderBy(DecisionDefinitionQueryProperty.TENANT_ID);
    }
    
    @Override
    public DecisionDefinitionQuery orderByDecisionRequirementsDefinitionKey() {
        return ((AbstractQuery<DecisionDefinitionQuery, U>)this).orderBy(DecisionDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_KEY);
    }
    
    @Override
    public DecisionDefinitionQuery orderByVersionTag() {
        return ((AbstractQuery<DecisionDefinitionQuery, U>)this).orderBy(DecisionDefinitionQueryProperty.VERSION_TAG);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getDecisionDefinitionManager().findDecisionDefinitionCountByQueryCriteria(this);
    }
    
    @Override
    public List<DecisionDefinition> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getDecisionDefinitionManager().findDecisionDefinitionsByQueryCriteria(this, page);
    }
    
    public void checkQueryOk() {
        super.checkQueryOk();
        if (this.latest && (this.id != null || this.name != null || this.nameLike != null || this.version != null || this.deploymentId != null)) {
            throw new NotValidException("Calling latest() can only be used in combination with key(String) and keyLike(String)");
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public String[] getIds() {
        return this.ids;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getCategoryLike() {
        return this.categoryLike;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameLike() {
        return this.nameLike;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public Date getDeployedAfter() {
        return this.deployedAfter;
    }
    
    public Date getDeployedAt() {
        return this.deployedAt;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getKeyLike() {
        return this.keyLike;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public String getResourceNameLike() {
        return this.resourceNameLike;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public String getVersionTag() {
        return this.versionTag;
    }
    
    public String getVersionTagLike() {
        return this.versionTagLike;
    }
    
    public boolean isLatest() {
        return this.latest;
    }
    
    public boolean isShouldJoinDeploymentTable() {
        return this.shouldJoinDeploymentTable;
    }
}
