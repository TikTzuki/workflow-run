// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

import org.camunda.bpm.engine.variable.type.ValueType;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.query.Query;

public interface TaskQuery extends Query<TaskQuery, Task>
{
    TaskQuery taskId(final String p0);
    
    TaskQuery taskIdIn(final String... p0);
    
    TaskQuery taskName(final String p0);
    
    TaskQuery taskNameNotEqual(final String p0);
    
    TaskQuery taskNameLike(final String p0);
    
    TaskQuery taskNameNotLike(final String p0);
    
    TaskQuery taskDescription(final String p0);
    
    TaskQuery taskDescriptionLike(final String p0);
    
    TaskQuery taskPriority(final Integer p0);
    
    TaskQuery taskMinPriority(final Integer p0);
    
    TaskQuery taskMaxPriority(final Integer p0);
    
    TaskQuery taskAssignee(final String p0);
    
    TaskQuery taskAssigneeExpression(final String p0);
    
    TaskQuery taskAssigneeLike(final String p0);
    
    TaskQuery taskAssigneeLikeExpression(final String p0);
    
    TaskQuery taskAssigneeIn(final String... p0);
    
    TaskQuery taskAssigneeNotIn(final String... p0);
    
    TaskQuery taskOwner(final String p0);
    
    TaskQuery taskOwnerExpression(final String p0);
    
    TaskQuery taskUnassigned();
    
    @Deprecated
    TaskQuery taskUnnassigned();
    
    TaskQuery taskAssigned();
    
    TaskQuery taskDelegationState(final DelegationState p0);
    
    TaskQuery taskCandidateUser(final String p0);
    
    TaskQuery taskCandidateUserExpression(final String p0);
    
    TaskQuery taskInvolvedUser(final String p0);
    
    TaskQuery taskInvolvedUserExpression(final String p0);
    
    TaskQuery withCandidateGroups();
    
    TaskQuery withoutCandidateGroups();
    
    TaskQuery withCandidateUsers();
    
    TaskQuery withoutCandidateUsers();
    
    TaskQuery taskCandidateGroup(final String p0);
    
    TaskQuery taskCandidateGroupExpression(final String p0);
    
    TaskQuery taskCandidateGroupIn(final List<String> p0);
    
    TaskQuery taskCandidateGroupInExpression(final String p0);
    
    TaskQuery includeAssignedTasks();
    
    TaskQuery processInstanceId(final String p0);
    
    TaskQuery processInstanceIdIn(final String... p0);
    
    TaskQuery processInstanceBusinessKey(final String p0);
    
    TaskQuery processInstanceBusinessKeyExpression(final String p0);
    
    TaskQuery processInstanceBusinessKeyIn(final String... p0);
    
    TaskQuery processInstanceBusinessKeyLike(final String p0);
    
    TaskQuery processInstanceBusinessKeyLikeExpression(final String p0);
    
    TaskQuery executionId(final String p0);
    
    TaskQuery activityInstanceIdIn(final String... p0);
    
    TaskQuery taskCreatedOn(final Date p0);
    
    TaskQuery taskCreatedOnExpression(final String p0);
    
    TaskQuery taskCreatedBefore(final Date p0);
    
    TaskQuery taskCreatedBeforeExpression(final String p0);
    
    TaskQuery taskCreatedAfter(final Date p0);
    
    TaskQuery taskCreatedAfterExpression(final String p0);
    
    TaskQuery excludeSubtasks();
    
    TaskQuery taskDefinitionKey(final String p0);
    
    TaskQuery taskDefinitionKeyLike(final String p0);
    
    TaskQuery taskDefinitionKeyIn(final String... p0);
    
    TaskQuery taskParentTaskId(final String p0);
    
    TaskQuery caseInstanceId(final String p0);
    
    TaskQuery caseInstanceBusinessKey(final String p0);
    
    TaskQuery caseInstanceBusinessKeyLike(final String p0);
    
    TaskQuery caseExecutionId(final String p0);
    
    TaskQuery caseDefinitionKey(final String p0);
    
    TaskQuery caseDefinitionId(final String p0);
    
    TaskQuery caseDefinitionName(final String p0);
    
    TaskQuery caseDefinitionNameLike(final String p0);
    
    TaskQuery matchVariableNamesIgnoreCase();
    
    TaskQuery matchVariableValuesIgnoreCase();
    
    TaskQuery taskVariableValueEquals(final String p0, final Object p1);
    
    TaskQuery taskVariableValueNotEquals(final String p0, final Object p1);
    
    TaskQuery taskVariableValueLike(final String p0, final String p1);
    
    TaskQuery taskVariableValueGreaterThan(final String p0, final Object p1);
    
    TaskQuery taskVariableValueGreaterThanOrEquals(final String p0, final Object p1);
    
    TaskQuery taskVariableValueLessThan(final String p0, final Object p1);
    
    TaskQuery taskVariableValueLessThanOrEquals(final String p0, final Object p1);
    
    TaskQuery processVariableValueEquals(final String p0, final Object p1);
    
    TaskQuery processVariableValueNotEquals(final String p0, final Object p1);
    
    TaskQuery processVariableValueLike(final String p0, final String p1);
    
    TaskQuery processVariableValueNotLike(final String p0, final String p1);
    
    TaskQuery processVariableValueGreaterThan(final String p0, final Object p1);
    
    TaskQuery processVariableValueGreaterThanOrEquals(final String p0, final Object p1);
    
    TaskQuery processVariableValueLessThan(final String p0, final Object p1);
    
    TaskQuery processVariableValueLessThanOrEquals(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueEquals(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueNotEquals(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueLike(final String p0, final String p1);
    
    TaskQuery caseInstanceVariableValueNotLike(final String p0, final String p1);
    
    TaskQuery caseInstanceVariableValueGreaterThan(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueGreaterThanOrEquals(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueLessThan(final String p0, final Object p1);
    
    TaskQuery caseInstanceVariableValueLessThanOrEquals(final String p0, final Object p1);
    
    TaskQuery processDefinitionKey(final String p0);
    
    TaskQuery processDefinitionKeyIn(final String... p0);
    
    TaskQuery processDefinitionId(final String p0);
    
    TaskQuery processDefinitionName(final String p0);
    
    TaskQuery processDefinitionNameLike(final String p0);
    
    TaskQuery dueDate(final Date p0);
    
    TaskQuery dueDateExpression(final String p0);
    
    TaskQuery dueBefore(final Date p0);
    
    TaskQuery dueBeforeExpression(final String p0);
    
    TaskQuery dueAfter(final Date p0);
    
    TaskQuery dueAfterExpression(final String p0);
    
    TaskQuery followUpDate(final Date p0);
    
    TaskQuery followUpDateExpression(final String p0);
    
    TaskQuery followUpBefore(final Date p0);
    
    TaskQuery followUpBeforeExpression(final String p0);
    
    TaskQuery followUpBeforeOrNotExistent(final Date p0);
    
    TaskQuery followUpBeforeOrNotExistentExpression(final String p0);
    
    TaskQuery followUpAfter(final Date p0);
    
    TaskQuery followUpAfterExpression(final String p0);
    
    TaskQuery suspended();
    
    TaskQuery active();
    
    TaskQuery initializeFormKeys();
    
    TaskQuery tenantIdIn(final String... p0);
    
    TaskQuery withoutTenantId();
    
    TaskQuery withoutDueDate();
    
    TaskQuery orderByTaskId();
    
    TaskQuery orderByTaskName();
    
    TaskQuery orderByTaskNameCaseInsensitive();
    
    TaskQuery orderByTaskDescription();
    
    TaskQuery orderByTaskPriority();
    
    TaskQuery orderByTaskAssignee();
    
    TaskQuery orderByTaskCreateTime();
    
    TaskQuery orderByProcessInstanceId();
    
    TaskQuery orderByCaseInstanceId();
    
    TaskQuery orderByExecutionId();
    
    TaskQuery orderByCaseExecutionId();
    
    TaskQuery orderByDueDate();
    
    TaskQuery orderByFollowUpDate();
    
    TaskQuery orderByProcessVariable(final String p0, final ValueType p1);
    
    TaskQuery orderByExecutionVariable(final String p0, final ValueType p1);
    
    TaskQuery orderByTaskVariable(final String p0, final ValueType p1);
    
    TaskQuery orderByCaseExecutionVariable(final String p0, final ValueType p1);
    
    TaskQuery orderByCaseInstanceVariable(final String p0, final ValueType p1);
    
    TaskQuery orderByTenantId();
    
    TaskQuery or();
    
    TaskQuery endOr();
}
