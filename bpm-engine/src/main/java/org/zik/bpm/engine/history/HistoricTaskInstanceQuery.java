// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricTaskInstanceQuery extends Query<HistoricTaskInstanceQuery, HistoricTaskInstance>
{
    HistoricTaskInstanceQuery taskId(final String p0);
    
    HistoricTaskInstanceQuery processInstanceId(final String p0);
    
    HistoricTaskInstanceQuery processInstanceBusinessKey(final String p0);
    
    HistoricTaskInstanceQuery processInstanceBusinessKeyIn(final String... p0);
    
    HistoricTaskInstanceQuery processInstanceBusinessKeyLike(final String p0);
    
    HistoricTaskInstanceQuery executionId(final String p0);
    
    HistoricTaskInstanceQuery activityInstanceIdIn(final String... p0);
    
    HistoricTaskInstanceQuery processDefinitionId(final String p0);
    
    HistoricTaskInstanceQuery processDefinitionKey(final String p0);
    
    HistoricTaskInstanceQuery processDefinitionName(final String p0);
    
    HistoricTaskInstanceQuery caseDefinitionId(final String p0);
    
    HistoricTaskInstanceQuery caseDefinitionKey(final String p0);
    
    HistoricTaskInstanceQuery caseDefinitionName(final String p0);
    
    HistoricTaskInstanceQuery caseInstanceId(final String p0);
    
    HistoricTaskInstanceQuery caseExecutionId(final String p0);
    
    HistoricTaskInstanceQuery taskName(final String p0);
    
    HistoricTaskInstanceQuery taskNameLike(final String p0);
    
    HistoricTaskInstanceQuery taskDescription(final String p0);
    
    HistoricTaskInstanceQuery taskDescriptionLike(final String p0);
    
    HistoricTaskInstanceQuery taskDefinitionKey(final String p0);
    
    HistoricTaskInstanceQuery taskDefinitionKeyIn(final String... p0);
    
    HistoricTaskInstanceQuery taskDeleteReason(final String p0);
    
    HistoricTaskInstanceQuery taskDeleteReasonLike(final String p0);
    
    HistoricTaskInstanceQuery taskAssigned();
    
    HistoricTaskInstanceQuery taskUnassigned();
    
    HistoricTaskInstanceQuery taskAssignee(final String p0);
    
    HistoricTaskInstanceQuery taskAssigneeLike(final String p0);
    
    HistoricTaskInstanceQuery taskOwner(final String p0);
    
    HistoricTaskInstanceQuery taskOwnerLike(final String p0);
    
    HistoricTaskInstanceQuery taskPriority(final Integer p0);
    
    HistoricTaskInstanceQuery finished();
    
    HistoricTaskInstanceQuery unfinished();
    
    HistoricTaskInstanceQuery processFinished();
    
    HistoricTaskInstanceQuery processUnfinished();
    
    HistoricTaskInstanceQuery taskInvolvedUser(final String p0);
    
    HistoricTaskInstanceQuery taskInvolvedGroup(final String p0);
    
    HistoricTaskInstanceQuery taskHadCandidateUser(final String p0);
    
    HistoricTaskInstanceQuery taskHadCandidateGroup(final String p0);
    
    HistoricTaskInstanceQuery withCandidateGroups();
    
    HistoricTaskInstanceQuery withoutCandidateGroups();
    
    HistoricTaskInstanceQuery matchVariableNamesIgnoreCase();
    
    HistoricTaskInstanceQuery matchVariableValuesIgnoreCase();
    
    HistoricTaskInstanceQuery taskVariableValueEquals(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery taskParentTaskId(final String p0);
    
    HistoricTaskInstanceQuery processVariableValueEquals(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueNotEquals(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueLike(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueNotLike(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueGreaterThan(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueGreaterThanOrEquals(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueLessThan(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery processVariableValueLessThanOrEquals(final String p0, final Object p1);
    
    HistoricTaskInstanceQuery taskDueDate(final Date p0);
    
    HistoricTaskInstanceQuery taskDueBefore(final Date p0);
    
    HistoricTaskInstanceQuery taskDueAfter(final Date p0);
    
    HistoricTaskInstanceQuery withoutTaskDueDate();
    
    HistoricTaskInstanceQuery taskFollowUpDate(final Date p0);
    
    HistoricTaskInstanceQuery taskFollowUpBefore(final Date p0);
    
    HistoricTaskInstanceQuery taskFollowUpAfter(final Date p0);
    
    HistoricTaskInstanceQuery tenantIdIn(final String... p0);
    
    HistoricTaskInstanceQuery withoutTenantId();
    
    HistoricTaskInstanceQuery finishedAfter(final Date p0);
    
    HistoricTaskInstanceQuery finishedBefore(final Date p0);
    
    HistoricTaskInstanceQuery startedAfter(final Date p0);
    
    HistoricTaskInstanceQuery startedBefore(final Date p0);
    
    HistoricTaskInstanceQuery orderByTenantId();
    
    HistoricTaskInstanceQuery orderByTaskId();
    
    HistoricTaskInstanceQuery orderByHistoricActivityInstanceId();
    
    HistoricTaskInstanceQuery orderByProcessDefinitionId();
    
    HistoricTaskInstanceQuery orderByProcessInstanceId();
    
    HistoricTaskInstanceQuery orderByExecutionId();
    
    HistoricTaskInstanceQuery orderByHistoricTaskInstanceDuration();
    
    HistoricTaskInstanceQuery orderByHistoricTaskInstanceEndTime();
    
    HistoricTaskInstanceQuery orderByHistoricActivityInstanceStartTime();
    
    HistoricTaskInstanceQuery orderByTaskName();
    
    HistoricTaskInstanceQuery orderByTaskDescription();
    
    HistoricTaskInstanceQuery orderByTaskAssignee();
    
    HistoricTaskInstanceQuery orderByTaskOwner();
    
    HistoricTaskInstanceQuery orderByTaskDueDate();
    
    HistoricTaskInstanceQuery orderByTaskFollowUpDate();
    
    HistoricTaskInstanceQuery orderByDeleteReason();
    
    HistoricTaskInstanceQuery orderByTaskDefinitionKey();
    
    HistoricTaskInstanceQuery orderByTaskPriority();
    
    HistoricTaskInstanceQuery orderByCaseDefinitionId();
    
    HistoricTaskInstanceQuery orderByCaseInstanceId();
    
    HistoricTaskInstanceQuery orderByCaseExecutionId();
    
    HistoricTaskInstanceQuery or();
    
    HistoricTaskInstanceQuery endOr();
}
