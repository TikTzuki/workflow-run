// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;

public class HistoricProcessInstanceQueryImpl extends AbstractVariableQueryImpl<HistoricProcessInstanceQuery, HistoricProcessInstance> implements HistoricProcessInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionName;
    protected String processDefinitionNameLike;
    protected String businessKey;
    protected String[] businessKeyIn;
    protected String businessKeyLike;
    protected boolean finished;
    protected boolean unfinished;
    protected boolean withIncidents;
    protected boolean withRootIncidents;
    protected String incidentType;
    protected String incidentStatus;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected String startedBy;
    protected boolean isRootProcessInstances;
    protected String superProcessInstanceId;
    protected String subProcessInstanceId;
    protected String superCaseInstanceId;
    protected String subCaseInstanceId;
    protected List<String> processKeyNotIn;
    protected Date startedBefore;
    protected Date startedAfter;
    protected Date finishedBefore;
    protected Date finishedAfter;
    protected Date executedActivityAfter;
    protected Date executedActivityBefore;
    protected Date executedJobAfter;
    protected Date executedJobBefore;
    protected String processDefinitionKey;
    protected String[] processDefinitionKeys;
    protected Set<String> processInstanceIds;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String[] executedActivityIds;
    protected String[] activeActivityIds;
    protected String state;
    protected String caseInstanceId;
    protected List<HistoricProcessInstanceQueryImpl> queries;
    protected boolean isOrQueryActive;
    protected Map<String, Set<QueryVariableValue>> queryVariableNameToValuesMap;
    protected Date startDateBy;
    protected Date startDateOn;
    protected Date finishDateBy;
    protected Date finishDateOn;
    protected Date startDateOnBegin;
    protected Date startDateOnEnd;
    protected Date finishDateOnBegin;
    protected Date finishDateOnEnd;
    
    public HistoricProcessInstanceQueryImpl() {
        this.finished = false;
        this.unfinished = false;
        this.withIncidents = false;
        this.withRootIncidents = false;
        this.queries = new ArrayList<HistoricProcessInstanceQueryImpl>(Collections.singletonList(this));
        this.isOrQueryActive = false;
        this.queryVariableNameToValuesMap = new HashMap<String, Set<QueryVariableValue>>();
    }
    
    public HistoricProcessInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.finished = false;
        this.unfinished = false;
        this.withIncidents = false;
        this.withRootIncidents = false;
        this.queries = new ArrayList<HistoricProcessInstanceQueryImpl>(Collections.singletonList(this));
        this.isOrQueryActive = false;
        this.queryVariableNameToValuesMap = new HashMap<String, Set<QueryVariableValue>>();
    }
    
    @Override
    public HistoricProcessInstanceQueryImpl processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processInstanceIds(final Set<String> processInstanceIds) {
        EnsureUtil.ensureNotEmpty("Set of process instance ids", processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQueryImpl processDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull("processDefinitionKeys", (Object[])processDefinitionKeys);
        this.processDefinitionKeys = processDefinitionKeys;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processDefinitionNameLike(final String nameLike) {
        this.processDefinitionNameLike = nameLike;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processInstanceBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processInstanceBusinessKeyIn(final String... businessKeyIn) {
        this.businessKeyIn = businessKeyIn;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processInstanceBusinessKeyLike(final String businessKeyLike) {
        this.businessKeyLike = businessKeyLike;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery finished() {
        this.finished = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery unfinished() {
        this.unfinished = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery withIncidents() {
        this.withIncidents = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery withRootIncidents() {
        this.withRootIncidents = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery incidentType(final String incidentType) {
        EnsureUtil.ensureNotNull("incident type", (Object)incidentType);
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery incidentStatus(final String status) {
        this.incidentStatus = status;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery incidentMessage(final String incidentMessage) {
        EnsureUtil.ensureNotNull("incidentMessage", (Object)incidentMessage);
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery incidentMessageLike(final String incidentMessageLike) {
        EnsureUtil.ensureNotNull("incidentMessageLike", (Object)incidentMessageLike);
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery startedBy(final String userId) {
        this.startedBy = userId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery processDefinitionKeyNotIn(final List<String> processDefinitionKeys) {
        EnsureUtil.ensureNotContainsNull("processDefinitionKeys", processDefinitionKeys);
        EnsureUtil.ensureNotContainsEmptyString("processDefinitionKeys", processDefinitionKeys);
        this.processKeyNotIn = processDefinitionKeys;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery startedAfter(final Date date) {
        this.startedAfter = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery startedBefore(final Date date) {
        this.startedBefore = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery finishedAfter(final Date date) {
        this.finishedAfter = date;
        this.finished = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery finishedBefore(final Date date) {
        this.finishedBefore = date;
        this.finished = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery rootProcessInstances() {
        if (this.superProcessInstanceId != null) {
            throw new BadUserRequestException("Invalid query usage: cannot set both rootProcessInstances and superProcessInstanceId");
        }
        if (this.superCaseInstanceId != null) {
            throw new BadUserRequestException("Invalid query usage: cannot set both rootProcessInstances and superCaseInstanceId");
        }
        this.isRootProcessInstances = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery superProcessInstanceId(final String superProcessInstanceId) {
        if (this.isRootProcessInstances) {
            throw new BadUserRequestException("Invalid query usage: cannot set both rootProcessInstances and superProcessInstanceId");
        }
        this.superProcessInstanceId = superProcessInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery subProcessInstanceId(final String subProcessInstanceId) {
        this.subProcessInstanceId = subProcessInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery superCaseInstanceId(final String superCaseInstanceId) {
        if (this.isRootProcessInstances) {
            throw new BadUserRequestException("Invalid query usage: cannot set both rootProcessInstances and superCaseInstanceId");
        }
        this.superCaseInstanceId = superCaseInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery subCaseInstanceId(final String subCaseInstanceId) {
        this.subCaseInstanceId = subCaseInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery caseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || (this.finished && this.unfinished) || CompareUtil.areNotInAscendingOrder(this.startedAfter, this.startedBefore) || CompareUtil.areNotInAscendingOrder(this.finishedAfter, this.finishedBefore) || CompareUtil.elementIsContainedInList(this.processDefinitionKey, this.processKeyNotIn) || CompareUtil.elementIsNotContainedInList(this.processInstanceId, this.processInstanceIds);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessInstanceBusinessKey() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceBusinessKey() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessInstanceDuration() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceDuration() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.DURATION);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessInstanceStartTime() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceStartTime() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.START_TIME);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessInstanceEndTime() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceEndTime() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.END_TIME);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessDefinitionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionId() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessDefinitionKey() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionKey() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessDefinitionName() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionName() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_NAME);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessDefinitionVersion() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionVersion() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_VERSION);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByProcessInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceId() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
    }
    
    @Override
    public HistoricProcessInstanceQuery orderByTenantId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTenantId() within 'or' query");
        }
        return ((AbstractQuery<HistoricProcessInstanceQuery, U>)this).orderBy(HistoricProcessInstanceQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricProcessInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public List<String> executeIdsList(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstanceIds(this);
    }
    
    @Override
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricProcessInstanceManager().findDeploymentIdMappingsByQueryCriteria(this);
    }
    
    @Override
    public List<QueryVariableValue> getQueryVariableValues() {
        return this.queryVariableNameToValuesMap.values().stream().flatMap((Function<? super Set<QueryVariableValue>, ? extends Stream<?>>)Collection::stream).collect((Collector<? super Object, ?, List<QueryVariableValue>>)Collectors.toList());
    }
    
    public Map<String, Set<QueryVariableValue>> getQueryVariableNameToValuesMap() {
        return this.queryVariableNameToValuesMap;
    }
    
    @Override
    protected void ensureVariablesInitialized() {
        super.ensureVariablesInitialized();
        if (!this.queries.isEmpty()) {
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
            final String dbType = processEngineConfiguration.getDatabaseType();
            for (final HistoricProcessInstanceQueryImpl orQuery : this.queries) {
                for (final QueryVariableValue var : orQuery.getQueryVariableValues()) {
                    var.initialize(variableSerializers, dbType);
                }
            }
        }
    }
    
    @Override
    protected void addVariable(final String name, final Object value, final QueryOperator operator, final boolean processInstanceScope) {
        final QueryVariableValue queryVariableValue = this.createQueryVariableValue(name, value, operator, processInstanceScope);
        final Set<QueryVariableValue> queryVariableValues = this.queryVariableNameToValuesMap.get(name);
        if (queryVariableValues == null) {
            this.queryVariableNameToValuesMap.put(name, new HashSet<QueryVariableValue>(Collections.singletonList(queryVariableValue)));
        }
        else {
            queryVariableValues.add(queryVariableValue);
        }
    }
    
    public List<HistoricProcessInstanceQueryImpl> getQueries() {
        return this.queries;
    }
    
    public void addOrQuery(final HistoricProcessInstanceQueryImpl orQuery) {
        orQuery.isOrQueryActive = true;
        this.queries.add(orQuery);
    }
    
    public void setOrQueryActive() {
        this.isOrQueryActive = true;
    }
    
    public boolean isOrQueryActive() {
        return this.isOrQueryActive;
    }
    
    public String[] getActiveActivityIds() {
        return this.activeActivityIds;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String[] getBusinessKeyIn() {
        return this.businessKeyIn;
    }
    
    public String getBusinessKeyLike() {
        return this.businessKeyLike;
    }
    
    public String[] getExecutedActivityIds() {
        return this.executedActivityIds;
    }
    
    public Date getExecutedActivityAfter() {
        return this.executedActivityAfter;
    }
    
    public Date getExecutedActivityBefore() {
        return this.executedActivityBefore;
    }
    
    public Date getExecutedJobAfter() {
        return this.executedJobAfter;
    }
    
    public Date getExecutedJobBefore() {
        return this.executedJobBefore;
    }
    
    public boolean isOpen() {
        return this.unfinished;
    }
    
    public boolean isUnfinished() {
        return this.unfinished;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
    
    public String getProcessDefinitionIdLike() {
        return this.processDefinitionKey + ":%:%";
    }
    
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public String getProcessDefinitionNameLike() {
        return this.processDefinitionNameLike;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public Set<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public String getStartedBy() {
        return this.startedBy;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public void setSuperProcessInstanceId(final String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }
    
    public List<String> getProcessKeyNotIn() {
        return this.processKeyNotIn;
    }
    
    public Date getStartedAfter() {
        return this.startedAfter;
    }
    
    public Date getStartedBefore() {
        return this.startedBefore;
    }
    
    public Date getFinishedAfter() {
        return this.finishedAfter;
    }
    
    public Date getFinishedBefore() {
        return this.finishedBefore;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
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
    
    public String getIncidentStatus() {
        return this.incidentStatus;
    }
    
    public String getState() {
        return this.state;
    }
    
    public Date getFinishDateBy() {
        return this.finishDateBy;
    }
    
    public Date getStartDateBy() {
        return this.startDateBy;
    }
    
    public Date getStartDateOn() {
        return this.startDateOn;
    }
    
    public Date getStartDateOnBegin() {
        return this.startDateOnBegin;
    }
    
    public Date getStartDateOnEnd() {
        return this.startDateOnEnd;
    }
    
    public Date getFinishDateOn() {
        return this.finishDateOn;
    }
    
    public Date getFinishDateOnBegin() {
        return this.finishDateOnBegin;
    }
    
    public Date getFinishDateOnEnd() {
        return this.finishDateOnEnd;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean getIsTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isWithIncidents() {
        return this.withIncidents;
    }
    
    public boolean isWithRootIncidents() {
        return this.withRootIncidents;
    }
    
    @Deprecated
    @Override
    public HistoricProcessInstanceQuery startDateBy(final Date date) {
        this.startDateBy = this.calculateMidnight(date);
        return this;
    }
    
    @Deprecated
    @Override
    public HistoricProcessInstanceQuery startDateOn(final Date date) {
        this.startDateOn = date;
        this.startDateOnBegin = this.calculateMidnight(date);
        this.startDateOnEnd = this.calculateBeforeMidnight(date);
        return this;
    }
    
    @Deprecated
    @Override
    public HistoricProcessInstanceQuery finishDateBy(final Date date) {
        this.finishDateBy = this.calculateBeforeMidnight(date);
        return this;
    }
    
    @Deprecated
    @Override
    public HistoricProcessInstanceQuery finishDateOn(final Date date) {
        this.finishDateOn = date;
        this.finishDateOnBegin = this.calculateMidnight(date);
        this.finishDateOnEnd = this.calculateBeforeMidnight(date);
        return this;
    }
    
    @Deprecated
    private Date calculateBeforeMidnight(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, 1);
        cal.add(13, -1);
        return cal.getTime();
    }
    
    @Deprecated
    private Date calculateMidnight(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(14, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(10, 0);
        return cal.getTime();
    }
    
    public boolean isRootProcessInstances() {
        return this.isRootProcessInstances;
    }
    
    public String getSubProcessInstanceId() {
        return this.subProcessInstanceId;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public String getSubCaseInstanceId() {
        return this.subCaseInstanceId;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    @Override
    public HistoricProcessInstanceQuery executedActivityAfter(final Date date) {
        this.executedActivityAfter = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery executedActivityBefore(final Date date) {
        this.executedActivityBefore = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery executedJobAfter(final Date date) {
        this.executedJobAfter = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery executedJobBefore(final Date date) {
        this.executedJobBefore = date;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery executedActivityIdIn(final String... ids) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "activity ids", (Object[])ids);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "activity ids", Arrays.asList(ids));
        this.executedActivityIds = ids;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery activeActivityIdIn(final String... ids) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "activity ids", (Object[])ids);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "activity ids", Arrays.asList(ids));
        this.activeActivityIds = ids;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery active() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "Already querying for historic process instance with another state", this.state, this.state);
        this.state = "ACTIVE";
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery suspended() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "Already querying for historic process instance with another state", this.state, this.state);
        this.state = "SUSPENDED";
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery completed() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "Already querying for historic process instance with another state", this.state, this.state);
        this.state = "COMPLETED";
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery externallyTerminated() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "Already querying for historic process instance with another state", this.state, this.state);
        this.state = "EXTERNALLY_TERMINATED";
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery internallyTerminated() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "Already querying for historic process instance with another state", this.state, this.state);
        this.state = "INTERNALLY_TERMINATED";
        return this;
    }
    
    @Override
    public HistoricProcessInstanceQuery or() {
        if (this != this.queries.get(0)) {
            throw new ProcessEngineException("Invalid query usage: cannot set or() within 'or' query");
        }
        final HistoricProcessInstanceQueryImpl orQuery = new HistoricProcessInstanceQueryImpl();
        orQuery.isOrQueryActive = true;
        orQuery.queries = this.queries;
        this.queries.add(orQuery);
        return orQuery;
    }
    
    @Override
    public HistoricProcessInstanceQuery endOr() {
        if (!this.queries.isEmpty() && this != this.queries.get(this.queries.size() - 1)) {
            throw new ProcessEngineException("Invalid query usage: cannot set endOr() before or()");
        }
        return this.queries.get(0);
    }
}
