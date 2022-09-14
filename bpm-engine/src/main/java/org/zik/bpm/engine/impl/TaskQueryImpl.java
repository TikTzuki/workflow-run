// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.HashMap;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import java.util.Collections;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.HashSet;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.task.DelegationState;
import java.util.Set;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.task.TaskQuery;

public class TaskQueryImpl extends AbstractQuery<TaskQuery, Task> implements TaskQuery
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String[] taskIdIn;
    protected String name;
    protected String nameNotEqual;
    protected String nameLike;
    protected String nameNotLike;
    protected String description;
    protected String descriptionLike;
    protected Integer priority;
    protected Integer minPriority;
    protected Integer maxPriority;
    protected String assignee;
    protected String assigneeLike;
    protected Set<String> assigneeIn;
    protected Set<String> assigneeNotIn;
    protected String involvedUser;
    protected String owner;
    protected Boolean unassigned;
    protected Boolean assigned;
    protected boolean noDelegationState;
    protected DelegationState delegationState;
    protected String candidateUser;
    protected String candidateGroup;
    protected List<String> candidateGroups;
    protected Boolean withCandidateGroups;
    protected Boolean withoutCandidateGroups;
    protected Boolean withCandidateUsers;
    protected Boolean withoutCandidateUsers;
    protected Boolean includeAssignedTasks;
    protected String processInstanceId;
    protected String[] processInstanceIdIn;
    protected String executionId;
    protected String[] activityInstanceIdIn;
    protected Date createTime;
    protected Date createTimeBefore;
    protected Date createTimeAfter;
    protected String key;
    protected String keyLike;
    protected String[] taskDefinitionKeys;
    protected String processDefinitionKey;
    protected String[] processDefinitionKeys;
    protected String processDefinitionId;
    protected String processDefinitionName;
    protected String processDefinitionNameLike;
    protected String processInstanceBusinessKey;
    protected String[] processInstanceBusinessKeys;
    protected String processInstanceBusinessKeyLike;
    protected List<TaskQueryVariableValue> variables;
    protected Date dueDate;
    protected Date dueBefore;
    protected Date dueAfter;
    protected Date followUpDate;
    protected Date followUpBefore;
    protected boolean followUpNullAccepted;
    protected Date followUpAfter;
    protected boolean excludeSubtasks;
    protected SuspensionState suspensionState;
    protected boolean initializeFormKeys;
    protected boolean taskNameCaseInsensitive;
    protected Boolean variableNamesIgnoreCase;
    protected Boolean variableValuesIgnoreCase;
    protected String parentTaskId;
    protected boolean isWithoutTenantId;
    protected boolean isWithoutDueDate;
    protected String[] tenantIds;
    protected String caseDefinitionKey;
    protected String caseDefinitionId;
    protected String caseDefinitionName;
    protected String caseDefinitionNameLike;
    protected String caseInstanceId;
    protected String caseInstanceBusinessKey;
    protected String caseInstanceBusinessKeyLike;
    protected String caseExecutionId;
    protected List<String> cachedCandidateGroups;
    protected Map<String, List<String>> cachedUserGroups;
    protected List<TaskQueryImpl> queries;
    protected boolean isOrQueryActive;
    
    public TaskQueryImpl() {
        this.noDelegationState = false;
        this.variables = new ArrayList<TaskQueryVariableValue>();
        this.followUpNullAccepted = false;
        this.excludeSubtasks = false;
        this.initializeFormKeys = false;
        this.taskNameCaseInsensitive = false;
        this.isWithoutTenantId = false;
        this.isWithoutDueDate = false;
        this.queries = new ArrayList<TaskQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    public TaskQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.noDelegationState = false;
        this.variables = new ArrayList<TaskQueryVariableValue>();
        this.followUpNullAccepted = false;
        this.excludeSubtasks = false;
        this.initializeFormKeys = false;
        this.taskNameCaseInsensitive = false;
        this.isWithoutTenantId = false;
        this.isWithoutDueDate = false;
        this.queries = new ArrayList<TaskQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    @Override
    public TaskQueryImpl taskId(final String taskId) {
        EnsureUtil.ensureNotNull("Task id", (Object)taskId);
        this.taskId = taskId;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskIdIn(final String... taskIds) {
        EnsureUtil.ensureNotNull("taskIds", (Object[])taskIds);
        this.taskIdIn = taskIds;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskName(final String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("Task nameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskDescription(final String description) {
        EnsureUtil.ensureNotNull("Description", (Object)description);
        this.description = description;
        return this;
    }
    
    @Override
    public TaskQuery taskDescriptionLike(final String descriptionLike) {
        EnsureUtil.ensureNotNull("Task descriptionLike", (Object)descriptionLike);
        this.descriptionLike = descriptionLike;
        return this;
    }
    
    @Override
    public TaskQuery taskPriority(final Integer priority) {
        EnsureUtil.ensureNotNull("Priority", priority);
        this.priority = priority;
        return this;
    }
    
    @Override
    public TaskQuery taskMinPriority(final Integer minPriority) {
        EnsureUtil.ensureNotNull("Min Priority", minPriority);
        this.minPriority = minPriority;
        return this;
    }
    
    @Override
    public TaskQuery taskMaxPriority(final Integer maxPriority) {
        EnsureUtil.ensureNotNull("Max Priority", maxPriority);
        this.maxPriority = maxPriority;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskAssignee(final String assignee) {
        EnsureUtil.ensureNotNull("Assignee", (Object)assignee);
        this.assignee = assignee;
        this.expressions.remove("taskAssignee");
        return this;
    }
    
    @Override
    public TaskQuery taskAssigneeExpression(final String assigneeExpression) {
        EnsureUtil.ensureNotNull("Assignee expression", (Object)assigneeExpression);
        this.expressions.put("taskAssignee", assigneeExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskAssigneeLike(final String assignee) {
        EnsureUtil.ensureNotNull("Assignee", (Object)assignee);
        this.assigneeLike = assignee;
        this.expressions.remove("taskAssigneeLike");
        return this;
    }
    
    @Override
    public TaskQuery taskAssigneeLikeExpression(final String assigneeLikeExpression) {
        EnsureUtil.ensureNotNull("Assignee like expression", (Object)assigneeLikeExpression);
        this.expressions.put("taskAssigneeLike", assigneeLikeExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskAssigneeIn(final String... assignees) {
        EnsureUtil.ensureNotNull("Assignees", (Object[])assignees);
        final Set<String> assigneeIn = new HashSet<String>(assignees.length);
        assigneeIn.addAll(Arrays.asList(assignees));
        this.assigneeIn = assigneeIn;
        this.expressions.remove("taskAssigneeIn");
        return this;
    }
    
    @Override
    public TaskQuery taskAssigneeNotIn(final String... assignees) {
        EnsureUtil.ensureNotNull("Assignees", (Object[])assignees);
        final Set<String> assigneeNotIn = new HashSet<String>(assignees.length);
        assigneeNotIn.addAll(Arrays.asList(assignees));
        this.assigneeNotIn = assigneeNotIn;
        this.expressions.remove("taskAssigneeNotIn");
        return this;
    }
    
    @Override
    public TaskQueryImpl taskOwner(final String owner) {
        EnsureUtil.ensureNotNull("Owner", (Object)owner);
        this.owner = owner;
        this.expressions.remove("taskOwner");
        return this;
    }
    
    @Override
    public TaskQuery taskOwnerExpression(final String ownerExpression) {
        EnsureUtil.ensureNotNull("Owner expression", (Object)ownerExpression);
        this.expressions.put("taskOwner", ownerExpression);
        return this;
    }
    
    @Deprecated
    @Override
    public TaskQuery taskUnnassigned() {
        return this.taskUnassigned();
    }
    
    @Override
    public TaskQuery taskUnassigned() {
        this.unassigned = true;
        return this;
    }
    
    @Override
    public TaskQuery taskAssigned() {
        this.assigned = true;
        return this;
    }
    
    @Override
    public TaskQuery taskDelegationState(final DelegationState delegationState) {
        if (delegationState == null) {
            this.noDelegationState = true;
        }
        else {
            this.delegationState = delegationState;
        }
        return this;
    }
    
    @Override
    public TaskQueryImpl taskCandidateUser(final String candidateUser) {
        EnsureUtil.ensureNotNull("Candidate user", (Object)candidateUser);
        if (!this.isOrQueryActive) {
            if (this.candidateGroup != null || this.expressions.containsKey("taskCandidateGroup")) {
                throw new ProcessEngineException("Invalid query usage: cannot set both candidateUser and candidateGroup");
            }
            if (this.candidateGroups != null || this.expressions.containsKey("taskCandidateGroupIn")) {
                throw new ProcessEngineException("Invalid query usage: cannot set both candidateUser and candidateGroupIn");
            }
        }
        this.candidateUser = candidateUser;
        this.expressions.remove("taskCandidateUser");
        return this;
    }
    
    @Override
    public TaskQuery taskCandidateUserExpression(final String candidateUserExpression) {
        EnsureUtil.ensureNotNull("Candidate user expression", (Object)candidateUserExpression);
        if (this.candidateGroup != null || this.expressions.containsKey("taskCandidateGroup")) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateUser and candidateGroup");
        }
        if (this.candidateGroups != null || this.expressions.containsKey("taskCandidateGroupIn")) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateUser and candidateGroupIn");
        }
        this.expressions.put("taskCandidateUser", candidateUserExpression);
        return this;
    }
    
    @Override
    public TaskQueryImpl taskInvolvedUser(final String involvedUser) {
        EnsureUtil.ensureNotNull("Involved user", (Object)involvedUser);
        this.involvedUser = involvedUser;
        this.expressions.remove("taskInvolvedUser");
        return this;
    }
    
    @Override
    public TaskQuery taskInvolvedUserExpression(final String involvedUserExpression) {
        EnsureUtil.ensureNotNull("Involved user expression", (Object)involvedUserExpression);
        this.expressions.put("taskInvolvedUser", involvedUserExpression);
        return this;
    }
    
    @Override
    public TaskQuery withCandidateGroups() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withCandidateGroups() within 'or' query");
        }
        this.withCandidateGroups = true;
        return this;
    }
    
    @Override
    public TaskQuery withoutCandidateGroups() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withoutCandidateGroups() within 'or' query");
        }
        this.withoutCandidateGroups = true;
        return this;
    }
    
    @Override
    public TaskQuery withCandidateUsers() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withCandidateUsers() within 'or' query");
        }
        this.withCandidateUsers = true;
        return this;
    }
    
    @Override
    public TaskQuery withoutCandidateUsers() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set withoutCandidateUsers() within 'or' query");
        }
        this.withoutCandidateUsers = true;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskCandidateGroup(final String candidateGroup) {
        EnsureUtil.ensureNotNull("Candidate group", (Object)candidateGroup);
        if (!this.isOrQueryActive && (this.candidateUser != null || this.expressions.containsKey("taskCandidateUser"))) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateGroup and candidateUser");
        }
        this.candidateGroup = candidateGroup;
        this.expressions.remove("taskCandidateGroup");
        return this;
    }
    
    @Override
    public TaskQuery taskCandidateGroupExpression(final String candidateGroupExpression) {
        EnsureUtil.ensureNotNull("Candidate group expression", (Object)candidateGroupExpression);
        if (!this.isOrQueryActive && (this.candidateUser != null || this.expressions.containsKey("taskCandidateUser"))) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateGroup and candidateUser");
        }
        this.expressions.put("taskCandidateGroup", candidateGroupExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskCandidateGroupIn(final List<String> candidateGroups) {
        EnsureUtil.ensureNotEmpty("Candidate group list", candidateGroups);
        if (!this.isOrQueryActive && (this.candidateUser != null || this.expressions.containsKey("taskCandidateUser"))) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateGroupIn and candidateUser");
        }
        this.candidateGroups = candidateGroups;
        this.expressions.remove("taskCandidateGroupIn");
        return this;
    }
    
    @Override
    public TaskQuery taskCandidateGroupInExpression(final String candidateGroupsExpression) {
        EnsureUtil.ensureNotEmpty("Candidate group list expression", candidateGroupsExpression);
        if (!this.isOrQueryActive && (this.candidateUser != null || this.expressions.containsKey("taskCandidateUser"))) {
            throw new ProcessEngineException("Invalid query usage: cannot set both candidateGroupIn and candidateUser");
        }
        this.expressions.put("taskCandidateGroupIn", candidateGroupsExpression);
        return this;
    }
    
    @Override
    public TaskQuery includeAssignedTasks() {
        if (this.candidateUser == null && this.candidateGroup == null && this.candidateGroups == null && !this.isWithCandidateGroups() && !this.isWithoutCandidateGroups() && !this.isWithCandidateUsers() && !this.isWithoutCandidateUsers() && !this.expressions.containsKey("taskCandidateUser") && !this.expressions.containsKey("taskCandidateGroup") && !this.expressions.containsKey("taskCandidateGroupIn")) {
            throw new ProcessEngineException("Invalid query usage: candidateUser, candidateGroup, candidateGroupIn, withCandidateGroups, withoutCandidateGroups, withCandidateUsers, withoutCandidateUsers has to be called before 'includeAssignedTasks'.");
        }
        this.includeAssignedTasks = true;
        return this;
    }
    
    public TaskQuery includeAssignedTasksInternal() {
        this.includeAssignedTasks = true;
        return this;
    }
    
    @Override
    public TaskQueryImpl processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public TaskQuery processInstanceIdIn(final String... processInstanceIds) {
        this.processInstanceIdIn = processInstanceIds;
        return this;
    }
    
    @Override
    public TaskQueryImpl processInstanceBusinessKey(final String processInstanceBusinessKey) {
        this.processInstanceBusinessKey = processInstanceBusinessKey;
        this.expressions.remove("processInstanceBusinessKey");
        return this;
    }
    
    @Override
    public TaskQuery processInstanceBusinessKeyExpression(final String processInstanceBusinessKeyExpression) {
        EnsureUtil.ensureNotNull("processInstanceBusinessKey expression", (Object)processInstanceBusinessKeyExpression);
        this.expressions.put("processInstanceBusinessKey", processInstanceBusinessKeyExpression);
        return this;
    }
    
    @Override
    public TaskQuery processInstanceBusinessKeyIn(final String... processInstanceBusinessKeys) {
        this.processInstanceBusinessKeys = processInstanceBusinessKeys;
        return this;
    }
    
    @Override
    public TaskQuery processInstanceBusinessKeyLike(final String processInstanceBusinessKey) {
        this.processInstanceBusinessKeyLike = processInstanceBusinessKey;
        this.expressions.remove("processInstanceBusinessKeyLike");
        return this;
    }
    
    @Override
    public TaskQuery processInstanceBusinessKeyLikeExpression(final String processInstanceBusinessKeyLikeExpression) {
        EnsureUtil.ensureNotNull("processInstanceBusinessKeyLike expression", (Object)processInstanceBusinessKeyLikeExpression);
        this.expressions.put("processInstanceBusinessKeyLike", processInstanceBusinessKeyLikeExpression);
        return this;
    }
    
    @Override
    public TaskQueryImpl executionId(final String executionId) {
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public TaskQuery activityInstanceIdIn(final String... activityInstanceIds) {
        this.activityInstanceIdIn = activityInstanceIds;
        return this;
    }
    
    @Override
    public TaskQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutTenantId)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both tenantIdIn and withoutTenantId filters.");
        }
        this.tenantIds = tenantIds;
        return this;
    }
    
    @Override
    public TaskQuery withoutTenantId() {
        if (!this.isOrQueryActive && this.tenantIds != null && this.tenantIds.length > 0) {
            throw new ProcessEngineException("Invalid query usage: cannot set both tenantIdIn and withoutTenantId filters.");
        }
        this.isWithoutTenantId = true;
        return this;
    }
    
    @Override
    public TaskQueryImpl taskCreatedOn(final Date createTime) {
        this.createTime = createTime;
        this.expressions.remove("taskCreatedOn");
        return this;
    }
    
    @Override
    public TaskQuery taskCreatedOnExpression(final String createTimeExpression) {
        this.expressions.put("taskCreatedOn", createTimeExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskCreatedBefore(final Date before) {
        this.createTimeBefore = before;
        this.expressions.remove("taskCreatedBefore");
        return this;
    }
    
    @Override
    public TaskQuery taskCreatedBeforeExpression(final String beforeExpression) {
        this.expressions.put("taskCreatedBefore", beforeExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskCreatedAfter(final Date after) {
        this.createTimeAfter = after;
        this.expressions.remove("taskCreatedAfter");
        return this;
    }
    
    @Override
    public TaskQuery taskCreatedAfterExpression(final String afterExpression) {
        this.expressions.put("taskCreatedAfter", afterExpression);
        return this;
    }
    
    @Override
    public TaskQuery taskDefinitionKey(final String key) {
        this.key = key;
        return this;
    }
    
    @Override
    public TaskQuery taskDefinitionKeyLike(final String keyLike) {
        this.keyLike = keyLike;
        return this;
    }
    
    @Override
    public TaskQuery taskDefinitionKeyIn(final String... taskDefinitionKeys) {
        this.taskDefinitionKeys = taskDefinitionKeys;
        return this;
    }
    
    @Override
    public TaskQuery taskParentTaskId(final String taskParentTaskId) {
        this.parentTaskId = taskParentTaskId;
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull("caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceBusinessKey(final String caseInstanceBusinessKey) {
        EnsureUtil.ensureNotNull("caseInstanceBusinessKey", (Object)caseInstanceBusinessKey);
        this.caseInstanceBusinessKey = caseInstanceBusinessKey;
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceBusinessKeyLike(final String caseInstanceBusinessKeyLike) {
        EnsureUtil.ensureNotNull("caseInstanceBusinessKeyLike", (Object)caseInstanceBusinessKeyLike);
        this.caseInstanceBusinessKeyLike = caseInstanceBusinessKeyLike;
        return this;
    }
    
    @Override
    public TaskQuery caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)caseExecutionId);
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    @Override
    public TaskQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull("caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public TaskQuery caseDefinitionKey(final String caseDefinitionKey) {
        EnsureUtil.ensureNotNull("caseDefinitionKey", (Object)caseDefinitionKey);
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public TaskQuery caseDefinitionName(final String caseDefinitionName) {
        EnsureUtil.ensureNotNull("caseDefinitionName", (Object)caseDefinitionName);
        this.caseDefinitionName = caseDefinitionName;
        return this;
    }
    
    @Override
    public TaskQuery caseDefinitionNameLike(final String caseDefinitionNameLike) {
        EnsureUtil.ensureNotNull("caseDefinitionNameLike", (Object)caseDefinitionNameLike);
        this.caseDefinitionNameLike = caseDefinitionNameLike;
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueNotEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueLike(final String variableName, final String variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LIKE, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueGreaterThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueGreaterThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN_OR_EQUAL, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueLessThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN, true, false);
        return this;
    }
    
    @Override
    public TaskQuery taskVariableValueLessThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN_OR_EQUAL, true, false);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueNotEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueLike(final String variableName, final String variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LIKE, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueNotLike(final String variableName, final String variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_LIKE, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueGreaterThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueGreaterThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN_OR_EQUAL, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueLessThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN, false, true);
        return this;
    }
    
    @Override
    public TaskQuery processVariableValueLessThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN_OR_EQUAL, false, true);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.EQUALS, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueNotEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueLike(final String variableName, final String variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LIKE, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueNotLike(final String variableName, final String variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.NOT_LIKE, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueGreaterThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueGreaterThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.GREATER_THAN_OR_EQUAL, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueLessThan(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN, false, false);
        return this;
    }
    
    @Override
    public TaskQuery caseInstanceVariableValueLessThanOrEquals(final String variableName, final Object variableValue) {
        this.addVariable(variableName, variableValue, QueryOperator.LESS_THAN_OR_EQUAL, false, false);
        return this;
    }
    
    @Override
    public TaskQuery processDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public TaskQuery processDefinitionKeyIn(final String... processDefinitionKeys) {
        this.processDefinitionKeys = processDefinitionKeys;
        return this;
    }
    
    @Override
    public TaskQuery processDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public TaskQuery processDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
        return this;
    }
    
    @Override
    public TaskQuery processDefinitionNameLike(final String processDefinitionName) {
        this.processDefinitionNameLike = processDefinitionName;
        return this;
    }
    
    @Override
    public TaskQuery dueDate(final Date dueDate) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueDate and withoutDueDate filters.");
        }
        this.dueDate = dueDate;
        this.expressions.remove("dueDate");
        return this;
    }
    
    @Override
    public TaskQuery dueDateExpression(final String dueDateExpression) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueDateExpression and withoutDueDate filters.");
        }
        this.expressions.put("dueDate", dueDateExpression);
        return this;
    }
    
    @Override
    public TaskQuery dueBefore(final Date dueBefore) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueBefore and withoutDueDate filters.");
        }
        this.dueBefore = dueBefore;
        this.expressions.remove("dueBefore");
        return this;
    }
    
    @Override
    public TaskQuery dueBeforeExpression(final String dueDate) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueBeforeExpression and withoutDueDate filters.");
        }
        this.expressions.put("dueBefore", dueDate);
        return this;
    }
    
    @Override
    public TaskQuery dueAfter(final Date dueAfter) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueAfter and withoutDueDate filters.");
        }
        this.dueAfter = dueAfter;
        this.expressions.remove("dueAfter");
        return this;
    }
    
    @Override
    public TaskQuery dueAfterExpression(final String dueDateExpression) {
        if (!this.isOrQueryActive && Boolean.TRUE.equals(this.isWithoutDueDate)) {
            throw new ProcessEngineException("Invalid query usage: cannot set both dueAfterExpression and withoutDueDate filters.");
        }
        this.expressions.put("dueAfter", dueDateExpression);
        return this;
    }
    
    @Override
    public TaskQuery withoutDueDate() {
        if (!this.isOrQueryActive && (this.dueAfter != null || this.dueBefore != null || this.dueDate != null || this.expressions.containsKey("dueDate") || this.expressions.containsKey("dueBefore") || this.expressions.containsKey("dueAfter"))) {
            throw new ProcessEngineException("Invalid query usage: cannot set both due date (equal to, before, or after) and withoutDueDate filters.");
        }
        this.isWithoutDueDate = true;
        return this;
    }
    
    @Override
    public TaskQuery followUpDate(final Date followUpDate) {
        this.followUpDate = followUpDate;
        this.expressions.remove("followUpDate");
        return this;
    }
    
    @Override
    public TaskQuery followUpDateExpression(final String followUpDateExpression) {
        this.expressions.put("followUpDate", followUpDateExpression);
        return this;
    }
    
    @Override
    public TaskQuery followUpBefore(final Date followUpBefore) {
        this.followUpBefore = followUpBefore;
        this.followUpNullAccepted = false;
        this.expressions.remove("followUpBefore");
        return this;
    }
    
    @Override
    public TaskQuery followUpBeforeExpression(final String followUpBeforeExpression) {
        this.followUpNullAccepted = false;
        this.expressions.put("followUpBefore", followUpBeforeExpression);
        return this;
    }
    
    @Override
    public TaskQuery followUpBeforeOrNotExistent(final Date followUpDate) {
        this.followUpBefore = followUpDate;
        this.followUpNullAccepted = true;
        this.expressions.remove("followUpBeforeOrNotExistent");
        return this;
    }
    
    @Override
    public TaskQuery followUpBeforeOrNotExistentExpression(final String followUpDateExpression) {
        this.expressions.put("followUpBeforeOrNotExistent", followUpDateExpression);
        this.followUpNullAccepted = true;
        return this;
    }
    
    public void setFollowUpNullAccepted(final boolean followUpNullAccepted) {
        this.followUpNullAccepted = followUpNullAccepted;
    }
    
    @Override
    public TaskQuery followUpAfter(final Date followUpAfter) {
        this.followUpAfter = followUpAfter;
        this.expressions.remove("followUpAfter");
        return this;
    }
    
    @Override
    public TaskQuery followUpAfterExpression(final String followUpAfterExpression) {
        this.expressions.put("followUpAfter", followUpAfterExpression);
        return this;
    }
    
    @Override
    public TaskQuery excludeSubtasks() {
        this.excludeSubtasks = true;
        return this;
    }
    
    @Override
    public TaskQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public TaskQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public TaskQuery initializeFormKeys() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set initializeFormKeys() within 'or' query");
        }
        this.initializeFormKeys = true;
        return this;
    }
    
    public TaskQuery taskNameCaseInsensitive() {
        this.taskNameCaseInsensitive = true;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.minPriority, this.priority, this.maxPriority) || CompareUtil.areNotInAscendingOrder(this.dueAfter, this.dueDate, this.dueBefore) || CompareUtil.areNotInAscendingOrder(this.followUpAfter, this.followUpDate, this.followUpBefore) || CompareUtil.areNotInAscendingOrder(this.createTimeAfter, this.createTime, this.createTimeBefore) || CompareUtil.elementIsNotContainedInArray(this.key, this.taskDefinitionKeys) || CompareUtil.elementIsNotContainedInArray(this.processDefinitionKey, this.processDefinitionKeys) || CompareUtil.elementIsNotContainedInArray(this.processInstanceBusinessKey, this.processInstanceBusinessKeys);
    }
    
    public List<String> getCandidateGroups() {
        if (this.cachedCandidateGroups != null) {
            return this.cachedCandidateGroups;
        }
        if (this.candidateGroup != null && this.candidateGroups != null) {
            this.cachedCandidateGroups = new ArrayList<String>(this.candidateGroups);
            if (!this.isOrQueryActive) {
                this.cachedCandidateGroups.retainAll(Collections.singletonList(this.candidateGroup));
            }
            else if (!this.candidateGroups.contains(this.candidateGroup)) {
                this.cachedCandidateGroups.add(this.candidateGroup);
            }
        }
        else if (this.candidateGroup != null) {
            this.cachedCandidateGroups = Collections.singletonList(this.candidateGroup);
        }
        else if (this.candidateGroups != null) {
            this.cachedCandidateGroups = this.candidateGroups;
        }
        if (this.candidateUser != null) {
            final List<String> groupsForCandidateUser = this.getGroupsForCandidateUser(this.candidateUser);
            if (this.cachedCandidateGroups == null) {
                this.cachedCandidateGroups = groupsForCandidateUser;
            }
            else {
                for (final String group : groupsForCandidateUser) {
                    if (!this.cachedCandidateGroups.contains(group)) {
                        this.cachedCandidateGroups.add(group);
                    }
                }
            }
        }
        return this.cachedCandidateGroups;
    }
    
    public Boolean isWithCandidateGroups() {
        if (this.withCandidateGroups == null) {
            return false;
        }
        return this.withCandidateGroups;
    }
    
    public Boolean isWithCandidateUsers() {
        if (this.withCandidateUsers == null) {
            return false;
        }
        return this.withCandidateUsers;
    }
    
    public Boolean isWithCandidateGroupsInternal() {
        return this.withCandidateGroups;
    }
    
    public Boolean isWithoutCandidateGroups() {
        if (this.withoutCandidateGroups == null) {
            return false;
        }
        return this.withoutCandidateGroups;
    }
    
    public Boolean isWithoutCandidateUsers() {
        if (this.withoutCandidateUsers == null) {
            return false;
        }
        return this.withoutCandidateUsers;
    }
    
    public Boolean isWithoutCandidateGroupsInternal() {
        return this.withoutCandidateGroups;
    }
    
    public List<String> getCandidateGroupsInternal() {
        return this.candidateGroups;
    }
    
    protected List<String> getGroupsForCandidateUser(final String candidateUser) {
        final Map<String, List<String>> cachedUserGroups = this.getCachedUserGroups();
        if (cachedUserGroups.containsKey(candidateUser)) {
            return cachedUserGroups.get(candidateUser);
        }
        final List<Group> groups = ((Query<T, Group>)Context.getCommandContext().getReadOnlyIdentityProvider().createGroupQuery().groupMember(candidateUser)).list();
        final List<String> groupIds = new ArrayList<String>();
        for (final Group group : groups) {
            groupIds.add(group.getId());
        }
        cachedUserGroups.put(candidateUser, groupIds);
        return groupIds;
    }
    
    protected Map<String, List<String>> getCachedUserGroups() {
        if (this.queries.get(0).cachedUserGroups == null) {
            this.queries.get(0).cachedUserGroups = new HashMap<String, List<String>>();
        }
        return this.queries.get(0).cachedUserGroups;
    }
    
    protected void ensureOrExpressionsEvaluated() {
        for (int i = 1; i < this.queries.size(); ++i) {
            this.queries.get(i).validate();
            this.queries.get(i).evaluateExpressions();
        }
    }
    
    protected void ensureVariablesInitialized() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
        final String dbType = processEngineConfiguration.getDatabaseType();
        for (final QueryVariableValue var : this.variables) {
            var.initialize(variableSerializers, dbType);
        }
        if (!this.queries.isEmpty()) {
            for (final TaskQueryImpl orQuery : this.queries) {
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
        this.addVariable(new TaskQueryVariableValue(name, value, operator, isTaskVariable, isProcessInstanceVariable, Boolean.TRUE.equals(this.variableNamesIgnoreCase), shouldMatchVariableValuesIgnoreCase));
    }
    
    protected void addVariable(final TaskQueryVariableValue taskQueryVariableValue) {
        this.variables.add(taskQueryVariableValue);
    }
    
    private boolean isBoolean(final Object value) {
        return value != null && (Boolean.class.isAssignableFrom(value.getClass()) || Boolean.TYPE.isAssignableFrom(value.getClass()));
    }
    
    @Override
    public TaskQuery orderByTaskId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.TASK_ID);
    }
    
    @Override
    public TaskQuery orderByTaskName() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskName() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.NAME);
    }
    
    @Override
    public TaskQuery orderByTaskNameCaseInsensitive() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskNameCaseInsensitive() within 'or' query");
        }
        this.taskNameCaseInsensitive();
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.NAME_CASE_INSENSITIVE);
    }
    
    @Override
    public TaskQuery orderByTaskDescription() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskDescription() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.DESCRIPTION);
    }
    
    @Override
    public TaskQuery orderByTaskPriority() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskPriority() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.PRIORITY);
    }
    
    @Override
    public TaskQuery orderByProcessInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.PROCESS_INSTANCE_ID);
    }
    
    @Override
    public TaskQuery orderByCaseInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseInstanceId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.CASE_INSTANCE_ID);
    }
    
    @Override
    public TaskQuery orderByExecutionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByExecutionId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.EXECUTION_ID);
    }
    
    @Override
    public TaskQuery orderByTenantId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTenantId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.TENANT_ID);
    }
    
    @Override
    public TaskQuery orderByCaseExecutionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseExecutionId() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.CASE_EXECUTION_ID);
    }
    
    @Override
    public TaskQuery orderByTaskAssignee() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskAssignee() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.ASSIGNEE);
    }
    
    @Override
    public TaskQuery orderByTaskCreateTime() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskCreateTime() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.CREATE_TIME);
    }
    
    @Override
    public TaskQuery orderByDueDate() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByDueDate() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.DUE_DATE);
    }
    
    @Override
    public TaskQuery orderByFollowUpDate() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByFollowUpDate() within 'or' query");
        }
        return ((AbstractQuery<TaskQuery, U>)this).orderBy(TaskQueryProperty.FOLLOW_UP_DATE);
    }
    
    @Override
    public TaskQuery orderByProcessVariable(final String variableName, final ValueType valueType) {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessVariable() within 'or' query");
        }
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("valueType", valueType);
        this.orderBy(VariableOrderProperty.forProcessInstanceVariable(variableName, valueType));
        return this;
    }
    
    @Override
    public TaskQuery orderByExecutionVariable(final String variableName, final ValueType valueType) {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByExecutionVariable() within 'or' query");
        }
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("valueType", valueType);
        this.orderBy(VariableOrderProperty.forExecutionVariable(variableName, valueType));
        return this;
    }
    
    @Override
    public TaskQuery orderByTaskVariable(final String variableName, final ValueType valueType) {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTaskVariable() within 'or' query");
        }
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("valueType", valueType);
        this.orderBy(VariableOrderProperty.forTaskVariable(variableName, valueType));
        return this;
    }
    
    @Override
    public TaskQuery orderByCaseExecutionVariable(final String variableName, final ValueType valueType) {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseExecutionVariable() within 'or' query");
        }
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("valueType", valueType);
        this.orderBy(VariableOrderProperty.forCaseExecutionVariable(variableName, valueType));
        return this;
    }
    
    @Override
    public TaskQuery orderByCaseInstanceVariable(final String variableName, final ValueType valueType) {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByCaseInstanceVariable() within 'or' query");
        }
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("valueType", valueType);
        this.orderBy(VariableOrderProperty.forCaseInstanceVariable(variableName, valueType));
        return this;
    }
    
    @Override
    public List<Task> executeList(final CommandContext commandContext, final Page page) {
        this.ensureOrExpressionsEvaluated();
        this.ensureVariablesInitialized();
        this.checkQueryOk();
        this.resetCachedCandidateGroups();
        if (this.getCandidateGroup() != null && this.getCandidateGroupsInternal() != null && this.getCandidateGroups().isEmpty()) {
            return Collections.emptyList();
        }
        this.decideAuthorizationJoinType(commandContext);
        final List<Task> taskList = commandContext.getTaskManager().findTasksByQueryCriteria(this);
        if (this.initializeFormKeys) {
            for (final Task task : taskList) {
                ((TaskEntity)task).initializeFormKey();
            }
        }
        return taskList;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.ensureOrExpressionsEvaluated();
        this.ensureVariablesInitialized();
        this.checkQueryOk();
        this.resetCachedCandidateGroups();
        if (this.getCandidateGroup() != null && this.getCandidateGroupsInternal() != null && this.getCandidateGroups().isEmpty()) {
            return 0L;
        }
        this.decideAuthorizationJoinType(commandContext);
        return commandContext.getTaskManager().findTaskCountByQueryCriteria(this);
    }
    
    protected void decideAuthorizationJoinType(final CommandContext commandContext) {
        final boolean cmmnEnabled = commandContext.getProcessEngineConfiguration().isCmmnEnabled();
        this.authCheck.setUseLeftJoin(cmmnEnabled);
    }
    
    protected void resetCachedCandidateGroups() {
        this.cachedCandidateGroups = null;
        for (int i = 1; i < this.queries.size(); ++i) {
            this.queries.get(i).cachedCandidateGroups = null;
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameNotEqual() {
        return this.nameNotEqual;
    }
    
    public String getNameLike() {
        return this.nameLike;
    }
    
    public String getNameNotLike() {
        return this.nameNotLike;
    }
    
    public String getAssignee() {
        return this.assignee;
    }
    
    public String getAssigneeLike() {
        return this.assigneeLike;
    }
    
    public Set<String> getAssigneeIn() {
        return this.assigneeIn;
    }
    
    public Set<String> getAssigneeNotIn() {
        return this.assigneeNotIn;
    }
    
    public String getInvolvedUser() {
        return this.involvedUser;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public Boolean isAssigned() {
        if (this.assigned == null) {
            return false;
        }
        return this.assigned;
    }
    
    public Boolean isAssignedInternal() {
        return this.assigned;
    }
    
    public boolean isUnassigned() {
        return this.unassigned != null && this.unassigned;
    }
    
    public Boolean isUnassignedInternal() {
        return this.unassigned;
    }
    
    public DelegationState getDelegationState() {
        return this.delegationState;
    }
    
    public boolean isNoDelegationState() {
        return this.noDelegationState;
    }
    
    public String getDelegationStateString() {
        return (this.delegationState != null) ? this.delegationState.toString() : null;
    }
    
    public String getCandidateUser() {
        return this.candidateUser;
    }
    
    public String getCandidateGroup() {
        return this.candidateGroup;
    }
    
    public boolean isIncludeAssignedTasks() {
        return this.includeAssignedTasks != null && this.includeAssignedTasks;
    }
    
    public Boolean isIncludeAssignedTasksInternal() {
        return this.includeAssignedTasks;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String[] getProcessInstanceIdIn() {
        return this.processInstanceIdIn;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String[] getActivityInstanceIdIn() {
        return this.activityInstanceIdIn;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String[] getTaskIdIn() {
        return this.taskIdIn;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getDescriptionLike() {
        return this.descriptionLike;
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    
    public Integer getMinPriority() {
        return this.minPriority;
    }
    
    public Integer getMaxPriority() {
        return this.maxPriority;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public Date getCreateTimeBefore() {
        return this.createTimeBefore;
    }
    
    public Date getCreateTimeAfter() {
        return this.createTimeAfter;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String[] getKeys() {
        return this.taskDefinitionKeys;
    }
    
    public String getKeyLike() {
        return this.keyLike;
    }
    
    public String getParentTaskId() {
        return this.parentTaskId;
    }
    
    public List<TaskQueryVariableValue> getVariables() {
        return this.variables;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public String getProcessDefinitionNameLike() {
        return this.processDefinitionNameLike;
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
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public Date getDueBefore() {
        return this.dueBefore;
    }
    
    public Date getDueAfter() {
        return this.dueAfter;
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
    
    public boolean isExcludeSubtasks() {
        return this.excludeSubtasks;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getCaseInstanceBusinessKey() {
        return this.caseInstanceBusinessKey;
    }
    
    public String getCaseInstanceBusinessKeyLike() {
        return this.caseInstanceBusinessKeyLike;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
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
    
    public String getCaseDefinitionNameLike() {
        return this.caseDefinitionNameLike;
    }
    
    public boolean isInitializeFormKeys() {
        return this.initializeFormKeys;
    }
    
    public boolean isTaskNameCaseInsensitive() {
        return this.taskNameCaseInsensitive;
    }
    
    public boolean isWithoutTenantId() {
        return this.isWithoutTenantId;
    }
    
    public boolean isWithoutDueDate() {
        return this.isWithoutDueDate;
    }
    
    public String[] getTaskDefinitionKeys() {
        return this.taskDefinitionKeys;
    }
    
    public boolean getIsTenantIdSet() {
        return this.isWithoutTenantId;
    }
    
    public Boolean isVariableNamesIgnoreCase() {
        return this.variableNamesIgnoreCase;
    }
    
    public Boolean isVariableValuesIgnoreCase() {
        return this.variableValuesIgnoreCase;
    }
    
    public List<TaskQueryImpl> getQueries() {
        return this.queries;
    }
    
    public boolean isOrQueryActive() {
        return this.isOrQueryActive;
    }
    
    public void addOrQuery(final TaskQueryImpl orQuery) {
        orQuery.isOrQueryActive = true;
        this.queries.add(orQuery);
    }
    
    public void setOrQueryActive() {
        this.isOrQueryActive = true;
    }
    
    @Override
    public TaskQuery extend(final TaskQuery extending) {
        final TaskQueryImpl extendingQuery = (TaskQueryImpl)extending;
        final TaskQueryImpl extendedQuery = new TaskQueryImpl();
        extendedQuery.validators = new HashSet<Validator<AbstractQuery<?, ?>>>(this.validators);
        if (extendingQuery.getName() != null) {
            extendedQuery.taskName(extendingQuery.getName());
        }
        else if (this.getName() != null) {
            extendedQuery.taskName(this.getName());
        }
        if (extendingQuery.getNameLike() != null) {
            extendedQuery.taskNameLike(extendingQuery.getNameLike());
        }
        else if (this.getNameLike() != null) {
            extendedQuery.taskNameLike(this.getNameLike());
        }
        if (extendingQuery.getNameNotEqual() != null) {
            extendedQuery.taskNameNotEqual(extendingQuery.getNameNotEqual());
        }
        else if (this.getNameNotEqual() != null) {
            extendedQuery.taskNameNotEqual(this.getNameNotEqual());
        }
        if (extendingQuery.getNameNotLike() != null) {
            extendedQuery.taskNameNotLike(extendingQuery.getNameNotLike());
        }
        else if (this.getNameNotLike() != null) {
            extendedQuery.taskNameNotLike(this.getNameNotLike());
        }
        if (extendingQuery.getAssignee() != null) {
            extendedQuery.taskAssignee(extendingQuery.getAssignee());
        }
        else if (this.getAssignee() != null) {
            extendedQuery.taskAssignee(this.getAssignee());
        }
        if (extendingQuery.getAssigneeLike() != null) {
            extendedQuery.taskAssigneeLike(extendingQuery.getAssigneeLike());
        }
        else if (this.getAssigneeLike() != null) {
            extendedQuery.taskAssigneeLike(this.getAssigneeLike());
        }
        if (extendingQuery.getAssigneeIn() != null) {
            extendedQuery.taskAssigneeIn((String[])extendingQuery.getAssigneeIn().toArray(new String[extendingQuery.getAssigneeIn().size()]));
        }
        else if (this.getAssigneeIn() != null) {
            extendedQuery.taskAssigneeIn((String[])this.getAssigneeIn().toArray(new String[this.getAssigneeIn().size()]));
        }
        if (extendingQuery.getAssigneeNotIn() != null) {
            extendedQuery.taskAssigneeNotIn((String[])extendingQuery.getAssigneeNotIn().toArray(new String[extendingQuery.getAssigneeNotIn().size()]));
        }
        else if (this.getAssigneeNotIn() != null) {
            extendedQuery.taskAssigneeNotIn((String[])this.getAssigneeNotIn().toArray(new String[this.getAssigneeNotIn().size()]));
        }
        if (extendingQuery.getInvolvedUser() != null) {
            extendedQuery.taskInvolvedUser(extendingQuery.getInvolvedUser());
        }
        else if (this.getInvolvedUser() != null) {
            extendedQuery.taskInvolvedUser(this.getInvolvedUser());
        }
        if (extendingQuery.getOwner() != null) {
            extendedQuery.taskOwner(extendingQuery.getOwner());
        }
        else if (this.getOwner() != null) {
            extendedQuery.taskOwner(this.getOwner());
        }
        if (extendingQuery.isAssigned() || this.isAssigned()) {
            extendedQuery.taskAssigned();
        }
        if (extendingQuery.isUnassigned() || this.isUnassigned()) {
            extendedQuery.taskUnassigned();
        }
        if (extendingQuery.getDelegationState() != null) {
            extendedQuery.taskDelegationState(extendingQuery.getDelegationState());
        }
        else if (this.getDelegationState() != null) {
            extendedQuery.taskDelegationState(this.getDelegationState());
        }
        if (extendingQuery.getCandidateUser() != null) {
            extendedQuery.taskCandidateUser(extendingQuery.getCandidateUser());
        }
        else if (this.getCandidateUser() != null) {
            extendedQuery.taskCandidateUser(this.getCandidateUser());
        }
        if (extendingQuery.getCandidateGroup() != null) {
            extendedQuery.taskCandidateGroup(extendingQuery.getCandidateGroup());
        }
        else if (this.getCandidateGroup() != null) {
            extendedQuery.taskCandidateGroup(this.getCandidateGroup());
        }
        if (extendingQuery.isWithCandidateGroups() || this.isWithCandidateGroups()) {
            extendedQuery.withCandidateGroups();
        }
        if (extendingQuery.isWithCandidateUsers() || this.isWithCandidateUsers()) {
            extendedQuery.withCandidateUsers();
        }
        if (extendingQuery.isWithoutCandidateGroups() || this.isWithoutCandidateGroups()) {
            extendedQuery.withoutCandidateGroups();
        }
        if (extendingQuery.isWithoutCandidateUsers() || this.isWithoutCandidateUsers()) {
            extendedQuery.withoutCandidateUsers();
        }
        if (extendingQuery.getCandidateGroupsInternal() != null) {
            extendedQuery.taskCandidateGroupIn(extendingQuery.getCandidateGroupsInternal());
        }
        else if (this.getCandidateGroupsInternal() != null) {
            extendedQuery.taskCandidateGroupIn(this.getCandidateGroupsInternal());
        }
        if (extendingQuery.getProcessInstanceId() != null) {
            extendedQuery.processInstanceId(extendingQuery.getProcessInstanceId());
        }
        else if (this.getProcessInstanceId() != null) {
            extendedQuery.processInstanceId(this.getProcessInstanceId());
        }
        if (extendingQuery.getProcessInstanceIdIn() != null) {
            extendedQuery.processInstanceIdIn(extendingQuery.getProcessInstanceIdIn());
        }
        else if (this.processInstanceIdIn(new String[0]) != null) {
            extendedQuery.processInstanceIdIn(this.getProcessInstanceIdIn());
        }
        if (extendingQuery.getExecutionId() != null) {
            extendedQuery.executionId(extendingQuery.getExecutionId());
        }
        else if (this.getExecutionId() != null) {
            extendedQuery.executionId(this.getExecutionId());
        }
        if (extendingQuery.getActivityInstanceIdIn() != null) {
            extendedQuery.activityInstanceIdIn(extendingQuery.getActivityInstanceIdIn());
        }
        else if (this.getActivityInstanceIdIn() != null) {
            extendedQuery.activityInstanceIdIn(this.getActivityInstanceIdIn());
        }
        if (extendingQuery.getTaskId() != null) {
            extendedQuery.taskId(extendingQuery.getTaskId());
        }
        else if (this.getTaskId() != null) {
            extendedQuery.taskId(this.getTaskId());
        }
        if (extendingQuery.getTaskIdIn() != null) {
            extendedQuery.taskIdIn(extendingQuery.getTaskIdIn());
        }
        else if (this.getTaskIdIn() != null) {
            extendedQuery.taskIdIn(this.getTaskIdIn());
        }
        if (extendingQuery.getDescription() != null) {
            extendedQuery.taskDescription(extendingQuery.getDescription());
        }
        else if (this.getDescription() != null) {
            extendedQuery.taskDescription(this.getDescription());
        }
        if (extendingQuery.getDescriptionLike() != null) {
            extendedQuery.taskDescriptionLike(extendingQuery.getDescriptionLike());
        }
        else if (this.getDescriptionLike() != null) {
            extendedQuery.taskDescriptionLike(this.getDescriptionLike());
        }
        if (extendingQuery.getPriority() != null) {
            extendedQuery.taskPriority(extendingQuery.getPriority());
        }
        else if (this.getPriority() != null) {
            extendedQuery.taskPriority(this.getPriority());
        }
        if (extendingQuery.getMinPriority() != null) {
            extendedQuery.taskMinPriority(extendingQuery.getMinPriority());
        }
        else if (this.getMinPriority() != null) {
            extendedQuery.taskMinPriority(this.getMinPriority());
        }
        if (extendingQuery.getMaxPriority() != null) {
            extendedQuery.taskMaxPriority(extendingQuery.getMaxPriority());
        }
        else if (this.getMaxPriority() != null) {
            extendedQuery.taskMaxPriority(this.getMaxPriority());
        }
        if (extendingQuery.getCreateTime() != null) {
            extendedQuery.taskCreatedOn(extendingQuery.getCreateTime());
        }
        else if (this.getCreateTime() != null) {
            extendedQuery.taskCreatedOn(this.getCreateTime());
        }
        if (extendingQuery.getCreateTimeBefore() != null) {
            extendedQuery.taskCreatedBefore(extendingQuery.getCreateTimeBefore());
        }
        else if (this.getCreateTimeBefore() != null) {
            extendedQuery.taskCreatedBefore(this.getCreateTimeBefore());
        }
        if (extendingQuery.getCreateTimeAfter() != null) {
            extendedQuery.taskCreatedAfter(extendingQuery.getCreateTimeAfter());
        }
        else if (this.getCreateTimeAfter() != null) {
            extendedQuery.taskCreatedAfter(this.getCreateTimeAfter());
        }
        if (extendingQuery.getKey() != null) {
            extendedQuery.taskDefinitionKey(extendingQuery.getKey());
        }
        else if (this.getKey() != null) {
            extendedQuery.taskDefinitionKey(this.getKey());
        }
        if (extendingQuery.getKeyLike() != null) {
            extendedQuery.taskDefinitionKeyLike(extendingQuery.getKeyLike());
        }
        else if (this.getKeyLike() != null) {
            extendedQuery.taskDefinitionKeyLike(this.getKeyLike());
        }
        if (extendingQuery.getKeys() != null) {
            extendedQuery.taskDefinitionKeyIn(extendingQuery.getKeys());
        }
        else if (this.getKeys() != null) {
            extendedQuery.taskDefinitionKeyIn(this.getKeys());
        }
        if (extendingQuery.getParentTaskId() != null) {
            extendedQuery.taskParentTaskId(extendingQuery.getParentTaskId());
        }
        else if (this.getParentTaskId() != null) {
            extendedQuery.taskParentTaskId(this.getParentTaskId());
        }
        if (extendingQuery.getProcessDefinitionKey() != null) {
            extendedQuery.processDefinitionKey(extendingQuery.getProcessDefinitionKey());
        }
        else if (this.getProcessDefinitionKey() != null) {
            extendedQuery.processDefinitionKey(this.getProcessDefinitionKey());
        }
        if (extendingQuery.getProcessDefinitionKeys() != null) {
            extendedQuery.processDefinitionKeyIn(extendingQuery.getProcessDefinitionKeys());
        }
        else if (this.getProcessDefinitionKeys() != null) {
            extendedQuery.processDefinitionKeyIn(this.getProcessDefinitionKeys());
        }
        if (extendingQuery.getProcessDefinitionId() != null) {
            extendedQuery.processDefinitionId(extendingQuery.getProcessDefinitionId());
        }
        else if (this.getProcessDefinitionId() != null) {
            extendedQuery.processDefinitionId(this.getProcessDefinitionId());
        }
        if (extendingQuery.getProcessDefinitionName() != null) {
            extendedQuery.processDefinitionName(extendingQuery.getProcessDefinitionName());
        }
        else if (this.getProcessDefinitionName() != null) {
            extendedQuery.processDefinitionName(this.getProcessDefinitionName());
        }
        if (extendingQuery.getProcessDefinitionNameLike() != null) {
            extendedQuery.processDefinitionNameLike(extendingQuery.getProcessDefinitionNameLike());
        }
        else if (this.getProcessDefinitionNameLike() != null) {
            extendedQuery.processDefinitionNameLike(this.getProcessDefinitionNameLike());
        }
        if (extendingQuery.getProcessInstanceBusinessKey() != null) {
            extendedQuery.processInstanceBusinessKey(extendingQuery.getProcessInstanceBusinessKey());
        }
        else if (this.getProcessInstanceBusinessKey() != null) {
            extendedQuery.processInstanceBusinessKey(this.getProcessInstanceBusinessKey());
        }
        if (extendingQuery.getProcessInstanceBusinessKeyLike() != null) {
            extendedQuery.processInstanceBusinessKeyLike(extendingQuery.getProcessInstanceBusinessKeyLike());
        }
        else if (this.getProcessInstanceBusinessKeyLike() != null) {
            extendedQuery.processInstanceBusinessKeyLike(this.getProcessInstanceBusinessKeyLike());
        }
        if (extendingQuery.getDueDate() != null) {
            extendedQuery.dueDate(extendingQuery.getDueDate());
        }
        else if (this.getDueDate() != null) {
            extendedQuery.dueDate(this.getDueDate());
        }
        if (extendingQuery.getDueBefore() != null) {
            extendedQuery.dueBefore(extendingQuery.getDueBefore());
        }
        else if (this.getDueBefore() != null) {
            extendedQuery.dueBefore(this.getDueBefore());
        }
        if (extendingQuery.getDueAfter() != null) {
            extendedQuery.dueAfter(extendingQuery.getDueAfter());
        }
        else if (this.getDueAfter() != null) {
            extendedQuery.dueAfter(this.getDueAfter());
        }
        if (extendingQuery.isWithoutDueDate() || this.isWithoutDueDate()) {
            extendedQuery.withoutDueDate();
        }
        if (extendingQuery.getFollowUpDate() != null) {
            extendedQuery.followUpDate(extendingQuery.getFollowUpDate());
        }
        else if (this.getFollowUpDate() != null) {
            extendedQuery.followUpDate(this.getFollowUpDate());
        }
        if (extendingQuery.getFollowUpBefore() != null) {
            extendedQuery.followUpBefore(extendingQuery.getFollowUpBefore());
        }
        else if (this.getFollowUpBefore() != null) {
            extendedQuery.followUpBefore(this.getFollowUpBefore());
        }
        if (extendingQuery.getFollowUpAfter() != null) {
            extendedQuery.followUpAfter(extendingQuery.getFollowUpAfter());
        }
        else if (this.getFollowUpAfter() != null) {
            extendedQuery.followUpAfter(this.getFollowUpAfter());
        }
        if (extendingQuery.isFollowUpNullAccepted() || this.isFollowUpNullAccepted()) {
            extendedQuery.setFollowUpNullAccepted(true);
        }
        if (extendingQuery.isExcludeSubtasks() || this.isExcludeSubtasks()) {
            extendedQuery.excludeSubtasks();
        }
        if (extendingQuery.getSuspensionState() != null) {
            if (extendingQuery.getSuspensionState().equals(SuspensionState.ACTIVE)) {
                extendedQuery.active();
            }
            else if (extendingQuery.getSuspensionState().equals(SuspensionState.SUSPENDED)) {
                extendedQuery.suspended();
            }
        }
        else if (this.getSuspensionState() != null) {
            if (this.getSuspensionState().equals(SuspensionState.ACTIVE)) {
                extendedQuery.active();
            }
            else if (this.getSuspensionState().equals(SuspensionState.SUSPENDED)) {
                extendedQuery.suspended();
            }
        }
        if (extendingQuery.getCaseInstanceId() != null) {
            extendedQuery.caseInstanceId(extendingQuery.getCaseInstanceId());
        }
        else if (this.getCaseInstanceId() != null) {
            extendedQuery.caseInstanceId(this.getCaseInstanceId());
        }
        if (extendingQuery.getCaseInstanceBusinessKey() != null) {
            extendedQuery.caseInstanceBusinessKey(extendingQuery.getCaseInstanceBusinessKey());
        }
        else if (this.getCaseInstanceBusinessKey() != null) {
            extendedQuery.caseInstanceBusinessKey(this.getCaseInstanceBusinessKey());
        }
        if (extendingQuery.getCaseInstanceBusinessKeyLike() != null) {
            extendedQuery.caseInstanceBusinessKeyLike(extendingQuery.getCaseInstanceBusinessKeyLike());
        }
        else if (this.getCaseInstanceBusinessKeyLike() != null) {
            extendedQuery.caseInstanceBusinessKeyLike(this.getCaseInstanceBusinessKeyLike());
        }
        if (extendingQuery.getCaseExecutionId() != null) {
            extendedQuery.caseExecutionId(extendingQuery.getCaseExecutionId());
        }
        else if (this.getCaseExecutionId() != null) {
            extendedQuery.caseExecutionId(this.getCaseExecutionId());
        }
        if (extendingQuery.getCaseDefinitionId() != null) {
            extendedQuery.caseDefinitionId(extendingQuery.getCaseDefinitionId());
        }
        else if (this.getCaseDefinitionId() != null) {
            extendedQuery.caseDefinitionId(this.getCaseDefinitionId());
        }
        if (extendingQuery.getCaseDefinitionKey() != null) {
            extendedQuery.caseDefinitionKey(extendingQuery.getCaseDefinitionKey());
        }
        else if (this.getCaseDefinitionKey() != null) {
            extendedQuery.caseDefinitionKey(this.getCaseDefinitionKey());
        }
        if (extendingQuery.getCaseDefinitionName() != null) {
            extendedQuery.caseDefinitionName(extendingQuery.getCaseDefinitionName());
        }
        else if (this.getCaseDefinitionName() != null) {
            extendedQuery.caseDefinitionName(this.getCaseDefinitionName());
        }
        if (extendingQuery.getCaseDefinitionNameLike() != null) {
            extendedQuery.caseDefinitionNameLike(extendingQuery.getCaseDefinitionNameLike());
        }
        else if (this.getCaseDefinitionNameLike() != null) {
            extendedQuery.caseDefinitionNameLike(this.getCaseDefinitionNameLike());
        }
        if (extendingQuery.isInitializeFormKeys() || this.isInitializeFormKeys()) {
            extendedQuery.initializeFormKeys();
        }
        if (extendingQuery.isTaskNameCaseInsensitive() || this.isTaskNameCaseInsensitive()) {
            extendedQuery.taskNameCaseInsensitive();
        }
        if (extendingQuery.getTenantIds() != null) {
            extendedQuery.tenantIdIn(extendingQuery.getTenantIds());
        }
        else if (this.getTenantIds() != null) {
            extendedQuery.tenantIdIn(this.getTenantIds());
        }
        if (extendingQuery.isWithoutTenantId() || this.isWithoutTenantId()) {
            extendedQuery.withoutTenantId();
        }
        this.mergeVariables(extendedQuery, extendingQuery);
        this.mergeExpressions(extendedQuery, extendingQuery);
        if (extendingQuery.isIncludeAssignedTasks() || this.isIncludeAssignedTasks()) {
            extendedQuery.includeAssignedTasks();
        }
        this.mergeOrdering(extendedQuery, extendingQuery);
        extendedQuery.queries = new ArrayList<TaskQueryImpl>(Arrays.asList(extendedQuery));
        if (this.queries.size() > 1) {
            this.queries.remove(0);
            extendedQuery.queries.addAll(this.queries);
        }
        if (extendingQuery.queries.size() > 1) {
            extendingQuery.queries.remove(0);
            extendedQuery.queries.addAll(extendingQuery.queries);
        }
        return extendedQuery;
    }
    
    protected void mergeVariables(final TaskQueryImpl extendedQuery, final TaskQueryImpl extendingQuery) {
        final List<TaskQueryVariableValue> extendingVariables = extendingQuery.getVariables();
        final Set<TaskQueryVariableValueComparable> extendingVariablesComparable = new HashSet<TaskQueryVariableValueComparable>();
        for (final TaskQueryVariableValue extendingVariable : extendingVariables) {
            extendedQuery.addVariable(extendingVariable);
            extendingVariablesComparable.add(new TaskQueryVariableValueComparable(extendingVariable));
        }
        for (final TaskQueryVariableValue originalVariable : this.getVariables()) {
            if (!extendingVariablesComparable.contains(new TaskQueryVariableValueComparable(originalVariable))) {
                extendedQuery.addVariable(originalVariable);
            }
        }
    }
    
    public boolean isFollowUpNullAccepted() {
        return this.followUpNullAccepted;
    }
    
    @Override
    public TaskQuery taskNameNotEqual(final String name) {
        this.nameNotEqual = name;
        return this;
    }
    
    @Override
    public TaskQuery taskNameNotLike(final String nameNotLike) {
        EnsureUtil.ensureNotNull("Task nameNotLike", (Object)nameNotLike);
        this.nameNotLike = nameNotLike;
        return this;
    }
    
    public boolean isQueryForProcessTasksOnly() {
        final ProcessEngineConfigurationImpl engineConfiguration = Context.getProcessEngineConfiguration();
        return !engineConfiguration.isCmmnEnabled() && !engineConfiguration.isStandaloneTasksEnabled();
    }
    
    @Override
    public TaskQuery or() {
        if (this != this.queries.get(0)) {
            throw new ProcessEngineException("Invalid query usage: cannot set or() within 'or' query");
        }
        final TaskQueryImpl orQuery = new TaskQueryImpl();
        orQuery.isOrQueryActive = true;
        orQuery.queries = this.queries;
        this.queries.add(orQuery);
        return orQuery;
    }
    
    @Override
    public TaskQuery endOr() {
        if (!this.queries.isEmpty() && this != this.queries.get(this.queries.size() - 1)) {
            throw new ProcessEngineException("Invalid query usage: cannot set endOr() before or()");
        }
        return this.queries.get(0);
    }
    
    @Override
    public TaskQuery matchVariableNamesIgnoreCase() {
        this.variableNamesIgnoreCase = true;
        for (final TaskQueryVariableValue variable : this.variables) {
            variable.setVariableNameIgnoreCase(true);
        }
        return this;
    }
    
    @Override
    public TaskQuery matchVariableValuesIgnoreCase() {
        this.variableValuesIgnoreCase = true;
        for (final TaskQueryVariableValue variable : this.variables) {
            variable.setVariableValueIgnoreCase(true);
        }
        return this;
    }
    
    protected class TaskQueryVariableValueComparable
    {
        protected TaskQueryVariableValue variableValue;
        
        public TaskQueryVariableValueComparable(final TaskQueryVariableValue variableValue) {
            this.variableValue = variableValue;
        }
        
        public TaskQueryVariableValue getVariableValue() {
            return this.variableValue;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final TaskQueryVariableValue other = ((TaskQueryVariableValueComparable)o).getVariableValue();
            return this.variableValue.getName().equals(other.getName()) && this.variableValue.isProcessInstanceVariable() == other.isProcessInstanceVariable() && this.variableValue.isLocal() == other.isLocal();
        }
        
        @Override
        public int hashCode() {
            int result = (this.variableValue.getName() != null) ? this.variableValue.getName().hashCode() : 0;
            result = 31 * result + (this.variableValue.isProcessInstanceVariable() ? 1 : 0);
            result = 31 * result + (this.variableValue.isLocal() ? 1 : 0);
            return result;
        }
    }
}
