// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinitionQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class DecisionRequirementsDefinitionQueryImpl extends AbstractQuery<DecisionRequirementsDefinitionQuery, DecisionRequirementsDefinition> implements DecisionRequirementsDefinitionQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] ids;
    protected String category;
    protected String categoryLike;
    protected String name;
    protected String nameLike;
    protected String deploymentId;
    protected String key;
    protected String keyLike;
    protected String resourceName;
    protected String resourceNameLike;
    protected Integer version;
    protected boolean latest;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeDefinitionsWithoutTenantId;
    
    public DecisionRequirementsDefinitionQueryImpl() {
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
    }
    
    public DecisionRequirementsDefinitionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionId(final String id) {
        EnsureUtil.ensureNotNull(NotValidException.class, "id", (Object)id);
        this.id = id;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionIdIn(final String... ids) {
        this.ids = ids;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionCategory(final String category) {
        EnsureUtil.ensureNotNull(NotValidException.class, "category", (Object)category);
        this.category = category;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionCategoryLike(final String categoryLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "categoryLike", (Object)categoryLike);
        this.categoryLike = categoryLike;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionName(final String name) {
        EnsureUtil.ensureNotNull(NotValidException.class, "name", (Object)name);
        this.name = name;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "nameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionKey(final String key) {
        EnsureUtil.ensureNotNull(NotValidException.class, "key", (Object)key);
        this.key = key;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionKeyLike(final String keyLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "keyLike", (Object)keyLike);
        this.keyLike = keyLike;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionVersion(final Integer version) {
        EnsureUtil.ensureNotNull(NotValidException.class, "version", version);
        EnsureUtil.ensurePositive(NotValidException.class, "version", (long)version);
        this.version = version;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery latestVersion() {
        this.latest = true;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionResourceName(final String resourceName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceName", (Object)resourceName);
        this.resourceName = resourceName;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionResourceNameLike(final String resourceNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceNameLike", (Object)resourceNameLike);
        this.resourceNameLike = resourceNameLike;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery includeDecisionRequirementsDefinitionsWithoutTenantId() {
        this.includeDefinitionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionCategory() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_CATEGORY);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionKey() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionId() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionVersion() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_VERSION);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionName() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DECISION_REQUIREMENTS_DEFINITION_NAME);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByDeploymentId() {
        this.orderBy(DecisionRequirementsDefinitionQueryProperty.DEPLOYMENT_ID);
        return this;
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery orderByTenantId() {
        return this.orderBy(DecisionRequirementsDefinitionQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getDecisionRequirementsDefinitionManager().findDecisionRequirementsDefinitionCountByQueryCriteria(this);
    }
    
    @Override
    public List<DecisionRequirementsDefinition> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getDecisionRequirementsDefinitionManager().findDecisionRequirementsDefinitionsByQueryCriteria(this, page);
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
    
    public boolean isLatest() {
        return this.latest;
    }
}
