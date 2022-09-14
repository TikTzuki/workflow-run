// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.Iterator;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import org.zik.bpm.engine.history.HistoricTaskInstanceQuery;

public class HistoricTaskInstanceQueryImpl extends AbstractQuery<HistoricTaskInstanceQuery, HistoricTaskInstance> implements HistoricTaskInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionName;
    protected String processInstanceId;
    protected String processInstanceBusinessKey;
    protected String[] processInstanceBusinessKeys;
    protected String processInstanceBusinessKeyLike;
    protected String executionId;
    protected String[] activityInstanceIds;
    protected String taskId;
    protected String taskName;
    protected String taskNameLike;
    protected String taskParentTaskId;
    protected String taskDescription;
    protected String taskDescriptionLike;
    protected String taskDeleteReason;
    protected String taskDeleteReasonLike;
    protected String taskOwner;
    protected String taskOwnerLike;
    protected Boolean assigned;
    protected Boolean unassigned;
    protected String taskAssignee;
    protected String taskAssigneeLike;
    protected String[] taskDefinitionKeys;
    protected String taskInvolvedUser;
    protected String taskInvolvedGroup;
    protected String taskHadCandidateUser;
    protected String taskHadCandidateGroup;
    protected Boolean withCandidateGroups;
    protected Boolean withoutCandidateGroups;
    protected Integer taskPriority;
    protected boolean finished;
    protected boolean unfinished;
    protected boolean processFinished;
    protected boolean processUnfinished;
    protected List<TaskQueryVariableValue> variables;
    protected Boolean variableNamesIgnoreCase;
    protected Boolean variableValuesIgnoreCase;
    protected Date dueDate;
    protected Date dueAfter;
    protected Date dueBefore;
    protected boolean isWithoutTaskDueDate;
    protected Date followUpDate;
    protected Date followUpBefore;
    protected Date followUpAfter;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String caseDefinitionId;
    protected String caseDefinitionKey;
    protected String caseDefinitionName;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected Date finishedAfter;
    protected Date finishedBefore;
    protected Date startedAfter;
    protected Date startedBefore;
    protected List<HistoricTaskInstanceQueryImpl> queries;
    protected boolean isOrQueryActive;
    
    public HistoricTaskInstanceQueryImpl() {
        this.variables = new ArrayList<TaskQueryVariableValue>();
        this.queries = new ArrayList<HistoricTaskInstanceQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    public HistoricTaskInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.variables = new ArrayList<TaskQueryVariableValue>();
        this.queries = new ArrayList<HistoricTaskInstanceQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.ensureVariablesInitialized();
        this.checkQueryOk();
        return commandContext.getHistoricTaskInstanceManager().findHistoricTaskInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricTaskInstance> executeList(final CommandContext commandContext, final Page page) {
        this.ensureVariablesInitialized();
        this.checkQueryOk();
        return commandContext.getHistoricTaskInstanceManager().findHistoricTaskInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processInstanceBusinessKey(final String processInstanceBusinessKey) {
        this.processInstanceBusinessKey = processInstanceBusinessKey;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processInstanceBusinessKeyIn(final String... processInstanceBusinessKeys) {
        EnsureUtil.ensureNotNull("processInstanceBusinessKeys", (Object[])processInstanceBusinessKeys);
        this.processInstanceBusinessKeys = processInstanceBusinessKeys;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processInstanceBusinessKeyLike(final String processInstanceBusinessKey) {
        this.processInstanceBusinessKeyLike = processInstanceBusinessKey;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl executionId(final String executionId) {
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery activityInstanceIdIn(final String... activityInstanceIds) {
        EnsureUtil.ensureNotNull("activityInstanceIds", (Object[])activityInstanceIds);
        this.activityInstanceIds = activityInstanceIds;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl processDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskId(final String taskId) {
        this.taskId = taskId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskName(final String taskName) {
        this.taskName = taskName;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskNameLike(final String taskNameLike) {
        this.taskNameLike = taskNameLike;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskParentTaskId(final String parentTaskId) {
        this.taskParentTaskId = parentTaskId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskDescription(final String taskDescription) {
        this.taskDescription = taskDescription;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskDescriptionLike(final String taskDescriptionLike) {
        this.taskDescriptionLike = taskDescriptionLike;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskDeleteReason(final String taskDeleteReason) {
        this.taskDeleteReason = taskDeleteReason;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskDeleteReasonLike(final String taskDeleteReasonLike) {
        this.taskDeleteReasonLike = taskDeleteReasonLike;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskAssigned() {
        this.assigned = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskUnassigned() {
        this.unassigned = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskAssignee(final String taskAssignee) {
        this.taskAssignee = taskAssignee;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskAssigneeLike(final String taskAssigneeLike) {
        this.taskAssigneeLike = taskAssigneeLike;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskOwner(final String taskOwner) {
        this.taskOwner = taskOwner;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskOwnerLike(final String taskOwnerLike) {
        this.taskOwnerLike = taskOwnerLike;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery caseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery caseDefinitionKey(final String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery caseDefinitionName(final String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery caseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery caseExecutionId(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl finished() {
        this.finished = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl unfinished() {
        this.unfinished = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery matchVariableNamesIgnoreCase() {
        this.variableNamesIgnoreCase = true;
        for (final QueryVariableValue variable : this.variables) {
            variable.setVariableNameIgnoreCase(true);
        }
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery matchVariableValuesIgnoreCase() {
        this.variableValuesIgnoreCase = true;
        for (final QueryVariableValue variable : this.variables) {
            variable.setVariableValueIgnoreCase(true);
        }
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl taskVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, true, false);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueNotEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueLike(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LIKE, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueNotLike(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_LIKE, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueGreaterThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueGreaterThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN_OR_EQUAL, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueLessThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processVariableValueLessThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN_OR_EQUAL, false, true);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskDefinitionKey(final String taskDefinitionKey) {
        return this.taskDefinitionKeyIn(taskDefinitionKey);
    }
    
    @Override
    public HistoricTaskInstanceQuery taskDefinitionKeyIn(final String... taskDefinitionKeys) {
        EnsureUtil.ensureNotNull(NotValidException.class, "taskDefinitionKeys", (Object[])taskDefinitionKeys);
        this.taskDefinitionKeys = taskDefinitionKeys;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskPriority(final Integer taskPriority) {
        this.taskPriority = taskPriority;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processFinished() {
        this.processFinished = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskInvolvedUser(final String userId) {
        this.taskInvolvedUser = userId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskInvolvedGroup(final String groupId) {
        this.taskInvolvedGroup = groupId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskHadCandidateUser(final String userId) {
        this.taskHadCandidateUser = userId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskHadCandidateGroup(final String groupId) {
        this.taskHadCandidateGroup = groupId;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery withCandidateGroups() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withCandidateGroups() within 'or' query");
        }
        this.withCandidateGroups = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery withoutCandidateGroups() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withoutCandidateGroups() within 'or' query");
        }
        this.withoutCandidateGroups = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery processUnfinished() {
        this.processUnfinished = true;
        return this;
    }
    
    protected void ensureVariablesInitialized() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
        final String dbType = processEngineConfiguration.getDatabaseType();
        for (final QueryVariableValue var : this.variables) {
            var.initialize(variableSerializers, dbType);
        }
        if (!this.queries.isEmpty()) {
            for (final HistoricTaskInstanceQueryImpl orQuery : this.queries) {
                for (final QueryVariableValue var2 : orQuery.variables) {
                    var2.initialize(variableSerializers, dbType);
                }
            }
        }
    }
    
    public void addVariable(final String name, final Object value, final QueryOperator operator, final boolean isTaskVariable, final boolean isProcessInstanceVariable) {
        EnsureUtil.ensureNotNull("name", (Object)name);
        if (value == null || this.isBoolean(value)) {
            switch (operator) {
                case GREATER_THAN: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'greater than' condition");
                }
                case LESS_THAN: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'less than' condition");
                }
                case GREATER_THAN_OR_EQUAL: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'greater than or equal' condition");
                }
                case LESS_THAN_OR_EQUAL: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'less than or equal' condition");
                }
                case LIKE: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'like' condition");
                }
                case NOT_LIKE: {
                    throw new ProcessEngineException("Booleans and null cannot be used in 'not like' condition");
                }
            }
        }
        final boolean shouldMatchVariableValuesIgnoreCase = Boolean.TRUE.equals(this.variableValuesIgnoreCase) && value != null && String.class.isAssignableFrom(value.getClass());
        final boolean shouldMatchVariableNamesIgnoreCase = Boolean.TRUE.equals(this.variableNamesIgnoreCase);
        this.addVariable(new TaskQueryVariableValue(name, value, operator, isTaskVariable, isProcessInstanceVariable, shouldMatchVariableNamesIgnoreCase, shouldMatchVariableValuesIgnoreCase));
    }
    
    protected void addVariable(final TaskQueryVariableValue taskQueryVariableValue) {
        this.variables.add(taskQueryVariableValue);
    }
    
    private boolean isBoolean(final Object value) {
        return value != null && (Boolean.class.isAssignableFrom(value.getClass()) || Boolean.TYPE.isAssignableFrom(value.getClass()));
    }
    
    @Override
    public HistoricTaskInstanceQuery taskDueDate(final Date dueDate) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutTaskDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both taskDueDate and withoutTaskDueDate filters.");
        }
        this.dueDate = dueDate;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskDueAfter(final Date dueAfter) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutTaskDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both taskDueAfter and withoutTaskDueDate filters.");
        }
        this.dueAfter = dueAfter;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskDueBefore(final Date dueBefore) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutTaskDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both taskDueBefore and withoutTaskDueDate filters.");
        }
        this.dueBefore = dueBefore;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery withoutTaskDueDate() {
        if (!this.isOrQueryActive && (this.dueAfter != null || this.dueBefore != null || this.dueDate != null)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both task due date (equal to, before, or after) and withoutTaskDueDate filters.");
        }
        this.isWithoutTaskDueDate = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskFollowUpDate(final Date followUpDate) {
        this.followUpDate = followUpDate;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskFollowUpBefore(final Date followUpBefore) {
        this.followUpBefore = followUpBefore;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery taskFollowUpAfter(final Date followUpAfter) {
        this.followUpAfter = followUpAfter;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery finishedAfter(final Date date) {
        this.finishedAfter = date;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery finishedBefore(final Date date) {
        this.finishedBefore = date;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery startedAfter(final Date date) {
        this.startedAfter = date;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery startedBefore(final Date date) {
        this.startedBefore = date;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || (this.finished && this.unfinished) || (this.processFinished && this.processUnfinished) || CompareUtil.areNotInAscendingOrder(this.startedAfter, this.startedBefore) || CompareUtil.areNotInAscendingOrder(this.finishedAfter, this.finishedBefore) || CompareUtil.areNotInAscendingOrder(this.dueAfter, this.dueDate, this.dueBefore) || CompareUtil.areNotInAscendingOrder(this.followUpAfter, this.followUpDate, this.followUpBefore) || CompareUtil.elementIsNotContainedInArray(this.processInstanceBusinessKey, this.processInstanceBusinessKeys);
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByTaskId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByHistoricActivityInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByHistoricActivityInstanceId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByProcessDefinitionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByProcessInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByExecutionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByExecutionId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByHistoricTaskInstanceDuration() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByHistoricTaskInstanceDuration() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.DURATION);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByHistoricTaskInstanceEndTime() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByHistoricTaskInstanceEndTime() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.END);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByHistoricActivityInstanceStartTime() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByHistoricActivityInstanceStartTime() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.START);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByTaskName() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskName() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_NAME);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByTaskDescription() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskDescription() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_DESCRIPTION);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskAssignee() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskAssignee() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_ASSIGNEE);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskOwner() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskOwner() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_OWNER);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskDueDate() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskDueDate() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_DUE_DATE);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskFollowUpDate() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskFollowUpDate() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_FOLLOW_UP_DATE);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQueryImpl orderByDeleteReason() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByDeleteReason() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.DELETE_REASON);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskDefinitionKey() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskDefinitionKey() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTaskPriority() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskPriority() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.TASK_PRIORITY);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByCaseDefinitionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseDefinitionId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.CASE_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByCaseInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseInstanceId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.CASE_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByCaseExecutionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseExecutionId() within 'or' query");
        }
        this.orderBy(HistoricTaskInstanceQueryProperty.CASE_EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricTaskInstanceQuery orderByTenantId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTenantId() within 'or' query");
        }
        return ((AbstractQuery<HistoricTaskInstanceQuery, U>)this).orderBy(HistoricTaskInstanceQueryProperty.TENANT_ID);
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessInstanceBusinessKey() {
        return this.processInstanceBusinessKey;
    }
    
    public String[] getProcessInstanceBusinessKeys() {
        return this.processInstanceBusinessKeys;
    }
    
    public String getProcessInstanceBusinessKeyLike() {
        return this.processInstanceBusinessKeyLike;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String[] getActivityInstanceIds() {
        return this.activityInstanceIds;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public Boolean isAssigned() {
        return this.assigned;
    }
    
    public Boolean isUnassigned() {
        return this.unassigned;
    }
    
    public Boolean isWithCandidateGroups() {
        return this.withCandidateGroups;
    }
    
    public Boolean isWithoutCandidateGroups() {
        return this.withoutCandidateGroups;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isProcessFinished() {
        return this.processFinished;
    }
    
    public boolean isUnfinished() {
        return this.unfinished;
    }
    
    public boolean isProcessUnfinished() {
        return this.processUnfinished;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public Date getDueBefore() {
        return this.dueBefore;
    }
    
    public Date getDueAfter() {
        return this.dueAfter;
    }
    
    public boolean isWithoutTaskDueDate() {
        return this.isWithoutTaskDueDate;
    }
    
    public Date getFollowUpDate() {
        return this.followUpDate;
    }
    
    public Date getFollowUpBefore() {
        return this.followUpBefore;
    }
    
    public Date getFollowUpAfter() {
        return this.followUpAfter;
    }
    
    public String getTaskName() {
        return this.taskName;
    }
    
    public String getTaskNameLike() {
        return this.taskNameLike;
    }
    
    public String getTaskDescription() {
        return this.taskDescription;
    }
    
    public String getTaskDescriptionLike() {
        return this.taskDescriptionLike;
    }
    
    public String getTaskDeleteReason() {
        return this.taskDeleteReason;
    }
    
    public String getTaskDeleteReasonLike() {
        return this.taskDeleteReasonLike;
    }
    
    public String getTaskAssignee() {
        return this.taskAssignee;
    }
    
    public String getTaskAssigneeLike() {
        return this.taskAssigneeLike;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String getTaskInvolvedGroup() {
        return this.taskInvolvedGroup;
    }
    
    public String getTaskInvolvedUser() {
        return this.taskInvolvedUser;
    }
    
    public String getTaskHadCandidateGroup() {
        return this.taskHadCandidateGroup;
    }
    
    public String getTaskHadCandidateUser() {
        return this.taskHadCandidateUser;
    }
    
    public String[] getTaskDefinitionKeys() {
        return this.taskDefinitionKeys;
    }
    
    public List<TaskQueryVariableValue> getVariables() {
        return this.variables;
    }
    
    public Boolean getVariableNamesIgnoreCase() {
        return this.variableNamesIgnoreCase;
    }
    
    public Boolean getVariableValuesIgnoreCase() {
        return this.variableValuesIgnoreCase;
    }
    
    public String getTaskOwnerLike() {
        return this.taskOwnerLike;
    }
    
    public String getTaskOwner() {
        return this.taskOwner;
    }
    
    public Integer getTaskPriority() {
        return this.taskPriority;
    }
    
    public String getTaskParentTaskId() {
        return this.taskParentTaskId;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getCaseDefinitionName() {
        return this.caseDefinitionName;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public Date getFinishedAfter() {
        return this.finishedAfter;
    }
    
    public Date getFinishedBefore() {
        return this.finishedBefore;
    }
    
    public Date getStartedAfter() {
        return this.startedAfter;
    }
    
    public Date getStartedBefore() {
        return this.startedBefore;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public List<HistoricTaskInstanceQueryImpl> getQueries() {
        return this.queries;
    }
    
    public boolean isOrQueryActive() {
        return this.isOrQueryActive;
    }
    
    public void addOrQuery(final HistoricTaskInstanceQueryImpl orQuery) {
        orQuery.isOrQueryActive = true;
        this.queries.add(orQuery);
    }
    
    public void setOrQueryActive() {
        this.isOrQueryActive = true;
    }
    
    @Override
    public HistoricTaskInstanceQuery or() {
        if (this != this.queries.get(0)) {
            throw new ProcessEngineException("Invalid query usage: cannot set or() within 'or' query");
        }
        final HistoricTaskInstanceQueryImpl orQuery = new HistoricTaskInstanceQueryImpl();
        orQuery.isOrQueryActive = true;
        orQuery.queries = this.queries;
        this.queries.add(orQuery);
        return orQuery;
    }
    
    @Override
    public HistoricTaskInstanceQuery endOr() {
        if (!this.queries.isEmpty() && this != this.queries.get(this.queries.size() - 1)) {
            throw new ProcessEngineException("Invalid query usage: cannot set endOr() before or()");
        }
        return this.queries.get(0);
    }
}
