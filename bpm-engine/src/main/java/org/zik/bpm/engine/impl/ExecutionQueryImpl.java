// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.List;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.runtime.ExecutionQuery;

public class ExecutionQueryImpl extends AbstractVariableQueryImpl<ExecutionQuery, Execution> implements ExecutionQuery
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String businessKey;
    protected String activityId;
    protected String executionId;
    protected String processInstanceId;
    protected List<EventSubscriptionQueryValue> eventSubscriptions;
    protected SuspensionState suspensionState;
    protected String incidentType;
    protected String incidentId;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    
    public ExecutionQueryImpl() {
        this.isTenantIdSet = false;
    }
    
    public ExecutionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
    }
    
    @Override
    public ExecutionQueryImpl processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("Process definition id", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public ExecutionQueryImpl processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("Process definition key", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public ExecutionQueryImpl processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("Process instance id", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public ExecutionQuery processInstanceBusinessKey(final String businessKey) {
        EnsureUtil.ensureNotNull("Business key", (Object)businessKey);
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public ExecutionQueryImpl executionId(final String executionId) {
        EnsureUtil.ensureNotNull("Execution id", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public ExecutionQueryImpl activityId(final String activityId) {
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public ExecutionQuery signalEventSubscription(final String signalName) {
        return this.eventSubscription(EventType.SIGNAL, signalName);
    }
    
    @Override
    public ExecutionQuery signalEventSubscriptionName(final String signalName) {
        return this.eventSubscription(EventType.SIGNAL, signalName);
    }
    
    @Override
    public ExecutionQuery messageEventSubscriptionName(final String messageName) {
        return this.eventSubscription(EventType.MESSAGE, messageName);
    }
    
    @Override
    public ExecutionQuery messageEventSubscription() {
        return this.eventSubscription(EventType.MESSAGE, null);
    }
    
    public ExecutionQuery eventSubscription(final EventType eventType, final String eventName) {
        EnsureUtil.ensureNotNull("event type", eventType);
        if (!EventType.MESSAGE.equals(eventType)) {
            EnsureUtil.ensureNotNull("event name", (Object)eventName);
        }
        if (this.eventSubscriptions == null) {
            this.eventSubscriptions = new ArrayList<EventSubscriptionQueryValue>();
        }
        this.eventSubscriptions.add(new EventSubscriptionQueryValue(eventName, eventType.name()));
        return this;
    }
    
    @Override
    public ExecutionQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public ExecutionQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public ExecutionQuery processVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, false);
        return this;
    }
    
    @Override
    public ExecutionQuery processVariableValueNotEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false);
        return this;
    }
    
    @Override
    public ExecutionQuery incidentType(final String incidentType) {
        EnsureUtil.ensureNotNull("incident type", (Object)incidentType);
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public ExecutionQuery incidentId(final String incidentId) {
        EnsureUtil.ensureNotNull("incident id", (Object)incidentId);
        this.incidentId = incidentId;
        return this;
    }
    
    @Override
    public ExecutionQuery incidentMessage(final String incidentMessage) {
        EnsureUtil.ensureNotNull("incident message", (Object)incidentMessage);
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public ExecutionQuery incidentMessageLike(final String incidentMessageLike) {
        EnsureUtil.ensureNotNull("incident messageLike", (Object)incidentMessageLike);
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    public ExecutionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public ExecutionQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public ExecutionQueryImpl orderByProcessInstanceId() {
        this.orderBy(ExecutionQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public ExecutionQueryImpl orderByProcessDefinitionId() {
        this.orderBy(new QueryOrderingProperty("process-definition", ExecutionQueryProperty.PROCESS_DEFINITION_ID));
        return this;
    }
    
    @Override
    public ExecutionQueryImpl orderByProcessDefinitionKey() {
        this.orderBy(new QueryOrderingProperty("process-definition", ExecutionQueryProperty.PROCESS_DEFINITION_KEY));
        return this;
    }
    
    @Override
    public ExecutionQuery orderByTenantId() {
        this.orderBy(ExecutionQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getExecutionManager().findExecutionCountByQueryCriteria(this);
    }
    
    @Override
    public List<Execution> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return (List<Execution>)commandContext.getExecutionManager().findExecutionsByQueryCriteria(this, page);
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessInstanceIds() {
        return null;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }
    
    public List<EventSubscriptionQueryValue> getEventSubscriptions() {
        return this.eventSubscriptions;
    }
    
    public void setEventSubscriptions(final List<EventSubscriptionQueryValue> eventSubscriptions) {
        this.eventSubscriptions = eventSubscriptions;
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
}
