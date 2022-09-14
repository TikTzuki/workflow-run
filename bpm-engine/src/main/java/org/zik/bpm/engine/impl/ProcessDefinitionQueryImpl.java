// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.Query;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.impl.db.CompositePermissionCheck;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Collection;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.util.Iterator;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.PermissionCheck;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Date;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.ProcessDefinitionQuery;

public class ProcessDefinitionQueryImpl extends AbstractQuery<ProcessDefinitionQuery, ProcessDefinition> implements ProcessDefinitionQuery
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
    protected String[] keys;
    protected String keyLike;
    protected String resourceName;
    protected String resourceNameLike;
    protected Integer version;
    protected boolean latest;
    protected SuspensionState suspensionState;
    protected String authorizationUserId;
    protected List<String> cachedCandidateGroups;
    protected String procDefId;
    protected String incidentType;
    protected String incidentId;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected String eventSubscriptionName;
    protected String eventSubscriptionType;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeDefinitionsWithoutTenantId;
    protected boolean isVersionTagSet;
    protected String versionTag;
    protected String versionTagLike;
    protected boolean isStartableInTasklist;
    protected boolean isNotStartableInTasklist;
    protected boolean startablePermissionCheck;
    protected List<PermissionCheck> processDefinitionCreatePermissionChecks;
    private boolean shouldJoinDeploymentTable;
    
    public ProcessDefinitionQueryImpl() {
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
        this.isVersionTagSet = false;
        this.isStartableInTasklist = false;
        this.isNotStartableInTasklist = false;
        this.startablePermissionCheck = false;
        this.processDefinitionCreatePermissionChecks = new ArrayList<PermissionCheck>();
        this.shouldJoinDeploymentTable = false;
    }
    
    public ProcessDefinitionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.latest = false;
        this.isTenantIdSet = false;
        this.includeDefinitionsWithoutTenantId = false;
        this.isVersionTagSet = false;
        this.isStartableInTasklist = false;
        this.isNotStartableInTasklist = false;
        this.startablePermissionCheck = false;
        this.processDefinitionCreatePermissionChecks = new ArrayList<PermissionCheck>();
        this.shouldJoinDeploymentTable = false;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionId(final String processDefinitionId) {
        this.id = processDefinitionId;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionIdIn(final String... ids) {
        this.ids = ids;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionCategory(final String category) {
        EnsureUtil.ensureNotNull("category", (Object)category);
        this.category = category;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionCategoryLike(final String categoryLike) {
        EnsureUtil.ensureNotNull("categoryLike", (Object)categoryLike);
        this.categoryLike = categoryLike;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionName(final String name) {
        EnsureUtil.ensureNotNull("name", (Object)name);
        this.name = name;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("nameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl deployedAfter(final Date deployedAfter) {
        EnsureUtil.ensureNotNull("deployedAfter", deployedAfter);
        this.shouldJoinDeploymentTable = true;
        this.deployedAfter = deployedAfter;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl deployedAt(final Date deployedAt) {
        EnsureUtil.ensureNotNull("deployedAt", deployedAt);
        this.shouldJoinDeploymentTable = true;
        this.deployedAt = deployedAt;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionKey(final String key) {
        EnsureUtil.ensureNotNull("key", (Object)key);
        this.key = key;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionKeysIn(final String... keys) {
        EnsureUtil.ensureNotNull("keys", (Object[])keys);
        this.keys = keys;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionKeyLike(final String keyLike) {
        EnsureUtil.ensureNotNull("keyLike", (Object)keyLike);
        this.keyLike = keyLike;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionResourceName(final String resourceName) {
        EnsureUtil.ensureNotNull("resourceName", (Object)resourceName);
        this.resourceName = resourceName;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionResourceNameLike(final String resourceNameLike) {
        EnsureUtil.ensureNotNull("resourceNameLike", (Object)resourceNameLike);
        this.resourceNameLike = resourceNameLike;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl processDefinitionVersion(final Integer version) {
        EnsureUtil.ensureNotNull("version", version);
        EnsureUtil.ensurePositive("version", (long)version);
        this.version = version;
        return this;
    }
    
    @Override
    public ProcessDefinitionQueryImpl latestVersion() {
        this.latest = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery messageEventSubscription(final String messageName) {
        return this.eventSubscription(EventType.MESSAGE, messageName);
    }
    
    @Override
    public ProcessDefinitionQuery messageEventSubscriptionName(final String messageName) {
        return this.eventSubscription(EventType.MESSAGE, messageName);
    }
    
    public ProcessDefinitionQuery processDefinitionStarter(final String procDefId) {
        this.procDefId = procDefId;
        return this;
    }
    
    public ProcessDefinitionQuery eventSubscription(final EventType eventType, final String eventName) {
        EnsureUtil.ensureNotNull("event type", eventType);
        EnsureUtil.ensureNotNull("event name", (Object)eventName);
        this.eventSubscriptionType = eventType.name();
        this.eventSubscriptionName = eventName;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery incidentType(final String incidentType) {
        EnsureUtil.ensureNotNull("incident type", (Object)incidentType);
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery incidentId(final String incidentId) {
        EnsureUtil.ensureNotNull("incident id", (Object)incidentId);
        this.incidentId = incidentId;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery incidentMessage(final String incidentMessage) {
        EnsureUtil.ensureNotNull("incident message", (Object)incidentMessage);
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery incidentMessageLike(final String incidentMessageLike) {
        EnsureUtil.ensureNotNull("incident messageLike", (Object)incidentMessageLike);
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.elementIsNotContainedInArray(this.id, this.ids);
    }
    
    @Override
    public ProcessDefinitionQueryImpl tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery includeProcessDefinitionsWithoutTenantId() {
        this.includeDefinitionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery versionTag(final String versionTag) {
        EnsureUtil.ensureNotNull("versionTag", (Object)versionTag);
        this.versionTag = versionTag;
        this.isVersionTagSet = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery versionTagLike(final String versionTagLike) {
        EnsureUtil.ensureNotNull("versionTagLike", (Object)versionTagLike);
        this.versionTagLike = versionTagLike;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery withoutVersionTag() {
        this.isVersionTagSet = true;
        this.versionTag = null;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery startableInTasklist() {
        this.isStartableInTasklist = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery notStartableInTasklist() {
        this.isNotStartableInTasklist = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery startablePermissionCheck() {
        this.startablePermissionCheck = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionQuery orderByDeploymentId() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.DEPLOYMENT_ID);
    }
    
    @Override
    public ProcessDefinitionQuery orderByDeploymentTime() {
        this.shouldJoinDeploymentTable = true;
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(new QueryOrderingProperty("deployment", ProcessDefinitionQueryProperty.DEPLOY_TIME));
    }
    
    @Override
    public ProcessDefinitionQuery orderByProcessDefinitionKey() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
    }
    
    @Override
    public ProcessDefinitionQuery orderByProcessDefinitionCategory() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
    }
    
    @Override
    public ProcessDefinitionQuery orderByProcessDefinitionId() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public ProcessDefinitionQuery orderByProcessDefinitionVersion() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
    }
    
    @Override
    public ProcessDefinitionQuery orderByProcessDefinitionName() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
    }
    
    @Override
    public ProcessDefinitionQuery orderByTenantId() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.TENANT_ID);
    }
    
    @Override
    public ProcessDefinitionQuery orderByVersionTag() {
        return ((AbstractQuery<ProcessDefinitionQuery, U>)this).orderBy(ProcessDefinitionQueryProperty.VERSION_TAG);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.getCandidateGroups();
        return commandContext.getProcessDefinitionManager().findProcessDefinitionCountByQueryCriteria(this);
    }
    
    @Override
    public List<ProcessDefinition> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.getCandidateGroups();
        final List<ProcessDefinition> list = commandContext.getProcessDefinitionManager().findProcessDefinitionsByQueryCriteria(this, page);
        final boolean shouldQueryAddBpmnModelInstancesToCache = commandContext.getProcessEngineConfiguration().getEnableFetchProcessDefinitionDescription();
        if (shouldQueryAddBpmnModelInstancesToCache) {
            this.addProcessDefinitionToCacheAndRetrieveDocumentation(list);
        }
        return list;
    }
    
    protected void addProcessDefinitionToCacheAndRetrieveDocumentation(final List<ProcessDefinition> list) {
        for (final ProcessDefinition processDefinition : list) {
            final BpmnModelInstance bpmnModelInstance = Context.getProcessEngineConfiguration().getDeploymentCache().findBpmnModelInstanceForProcessDefinition((ProcessDefinitionEntity)processDefinition);
            final ModelElementInstance processElement = bpmnModelInstance.getModelElementById(processDefinition.getKey());
            if (processElement != null) {
                final Collection<Documentation> documentations = (Collection<Documentation>)processElement.getChildElementsByType((Class)Documentation.class);
                final List<String> docStrings = new ArrayList<String>();
                for (final Documentation documentation : documentations) {
                    docStrings.add(documentation.getTextContent());
                }
                final ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)processDefinition;
                processDefinitionEntity.setProperty("documentation", BpmnParse.parseDocumentation(docStrings));
            }
        }
    }
    
    public void checkQueryOk() {
        super.checkQueryOk();
        if (this.latest && (this.id != null || this.version != null || this.deploymentId != null)) {
            throw new ProcessEngineException("Calling latest() can only be used in combination with key(String) and keyLike(String) or name(String) and nameLike(String)");
        }
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
    
    public String getId() {
        return this.id;
    }
    
    public String[] getIds() {
        return this.ids;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameLike() {
        return this.nameLike;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getKeyLike() {
        return this.keyLike;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public boolean isLatest() {
        return this.latest;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getCategoryLike() {
        return this.categoryLike;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public String getResourceNameLike() {
        return this.resourceNameLike;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }
    
    public String getIncidentId() {
        return this.incidentId;
    }
    
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public String getIncidentMessage() {
        return this.incidentMessage;
    }
    
    public String getIncidentMessageLike() {
        return this.incidentMessageLike;
    }
    
    public String getVersionTag() {
        return this.versionTag;
    }
    
    public boolean isStartableInTasklist() {
        return this.isStartableInTasklist;
    }
    
    public boolean isNotStartableInTasklist() {
        return this.isNotStartableInTasklist;
    }
    
    public boolean isStartablePermissionCheck() {
        return this.startablePermissionCheck;
    }
    
    public void setProcessDefinitionCreatePermissionChecks(final List<PermissionCheck> processDefinitionCreatePermissionChecks) {
        this.processDefinitionCreatePermissionChecks = processDefinitionCreatePermissionChecks;
    }
    
    public List<PermissionCheck> getProcessDefinitionCreatePermissionChecks() {
        return this.processDefinitionCreatePermissionChecks;
    }
    
    public boolean isShouldJoinDeploymentTable() {
        return this.shouldJoinDeploymentTable;
    }
    
    public void addProcessDefinitionCreatePermissionCheck(final CompositePermissionCheck processDefinitionCreatePermissionCheck) {
        this.processDefinitionCreatePermissionChecks.addAll(processDefinitionCreatePermissionCheck.getAllPermissionChecks());
    }
    
    public List<String> getCandidateGroups() {
        if (this.cachedCandidateGroups != null) {
            return this.cachedCandidateGroups;
        }
        if (this.authorizationUserId != null) {
            final List<Group> groups = ((Query<T, Group>)Context.getCommandContext().getReadOnlyIdentityProvider().createGroupQuery().groupMember(this.authorizationUserId)).list();
            this.cachedCandidateGroups = groups.stream().map((Function<? super Object, ?>)Group::getId).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        }
        return this.cachedCandidateGroups;
    }
    
    @Override
    public ProcessDefinitionQueryImpl startableByUser(final String userId) {
        EnsureUtil.ensureNotNull("userId", (Object)userId);
        this.authorizationUserId = userId;
        return this;
    }
}
