// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.repository;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.CaseDefinitionQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class CaseDefinitionQueryImpl extends AbstractQuery<CaseDefinitionQuery, CaseDefinition> implements CaseDefinitionQuery
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
    
    public CaseDefinitionQueryImpl() {
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
    }
    
    public CaseDefinitionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionId", (Object)caseDefinitionId);
        this.id = caseDefinitionId;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionIdIn(final String... ids) {
        this.ids = ids;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionCategory(final String caseDefinitionCategory) {
        EnsureUtil.ensureNotNull(NotValidException.class, "category", (Object)caseDefinitionCategory);
        this.category = caseDefinitionCategory;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionCategoryLike(final String caseDefinitionCategoryLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "categoryLike", (Object)caseDefinitionCategoryLike);
        this.categoryLike = caseDefinitionCategoryLike;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionName(final String caseDefinitionName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "name", (Object)caseDefinitionName);
        this.name = caseDefinitionName;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionNameLike(final String caseDefinitionNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "nameLike", (Object)caseDefinitionNameLike);
        this.nameLike = caseDefinitionNameLike;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionKey(final String caseDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "key", (Object)caseDefinitionKey);
        this.key = caseDefinitionKey;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionKeyLike(final String caseDefinitionKeyLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "keyLike", (Object)caseDefinitionKeyLike);
        this.keyLike = caseDefinitionKeyLike;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionVersion(final Integer caseDefinitionVersion) {
        EnsureUtil.ensureNotNull(NotValidException.class, "version", caseDefinitionVersion);
        EnsureUtil.ensurePositive(NotValidException.class, "version", (long)caseDefinitionVersion);
        this.version = caseDefinitionVersion;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery latestVersion() {
        this.latest = true;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionResourceName(final String resourceName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceName", (Object)resourceName);
        this.resourceName = resourceName;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery caseDefinitionResourceNameLike(final String resourceNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceNameLike", (Object)resourceNameLike);
        this.resourceNameLike = resourceNameLike;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery includeCaseDefinitionsWithoutTenantId() {
        this.includeDefinitionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByCaseDefinitionCategory() {
        this.orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_CATEGORY);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByCaseDefinitionKey() {
        this.orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByCaseDefinitionId() {
        this.orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_ID);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByCaseDefinitionVersion() {
        this.orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_VERSION);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByCaseDefinitionName() {
        this.orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_NAME);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByDeploymentId() {
        this.orderBy(CaseDefinitionQueryProperty.DEPLOYMENT_ID);
        return this;
    }
    
    @Override
    public CaseDefinitionQuery orderByTenantId() {
        return ((AbstractQuery<CaseDefinitionQuery, U>)this).orderBy(CaseDefinitionQueryProperty.TENANT_ID);
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.elementIsNotContainedInArray(this.id, this.ids);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getCaseDefinitionManager().findCaseDefinitionCountByQueryCriteria(this);
    }
    
    @Override
    public List<CaseDefinition> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getCaseDefinitionManager().findCaseDefinitionsByQueryCriteria(this, page);
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
