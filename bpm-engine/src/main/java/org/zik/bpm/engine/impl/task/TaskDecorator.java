// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task;

import org.zik.bpm.engine.impl.context.Context;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import org.zik.bpm.engine.impl.calendar.BusinessCalendar;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Date;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.el.ExpressionManager;

public class TaskDecorator
{
    protected TaskDefinition taskDefinition;
    protected ExpressionManager expressionManager;
    
    public TaskDecorator(final TaskDefinition taskDefinition, final ExpressionManager expressionManager) {
        this.taskDefinition = taskDefinition;
        this.expressionManager = expressionManager;
    }
    
    public void decorate(final TaskEntity task, final VariableScope variableScope) {
        task.setTaskDefinition(this.taskDefinition);
        this.initializeTaskName(task, variableScope);
        this.initializeTaskDescription(task, variableScope);
        this.initializeTaskDueDate(task, variableScope);
        this.initializeTaskFollowUpDate(task, variableScope);
        this.initializeTaskPriority(task, variableScope);
        this.initializeTaskAssignments(task, variableScope);
    }
    
    protected void initializeTaskName(final TaskEntity task, final VariableScope variableScope) {
        final Expression nameExpression = this.taskDefinition.getNameExpression();
        if (nameExpression != null) {
            final String name = (String)nameExpression.getValue(variableScope);
            task.setName(name);
        }
    }
    
    protected void initializeTaskDescription(final TaskEntity task, final VariableScope variableScope) {
        final Expression descriptionExpression = this.taskDefinition.getDescriptionExpression();
        if (descriptionExpression != null) {
            final String description = (String)descriptionExpression.getValue(variableScope);
            task.setDescription(description);
        }
    }
    
    protected void initializeTaskDueDate(final TaskEntity task, final VariableScope variableScope) {
        final Expression dueDateExpression = this.taskDefinition.getDueDateExpression();
        if (dueDateExpression != null) {
            final Object dueDate = dueDateExpression.getValue(variableScope);
            if (dueDate != null) {
                if (dueDate instanceof Date) {
                    task.setDueDate((Date)dueDate);
                }
                else {
                    if (!(dueDate instanceof String)) {
                        throw new ProcessEngineException("Due date expression does not resolve to a Date or Date string: " + dueDateExpression.getExpressionText());
                    }
                    final BusinessCalendar businessCalendar = this.getBusinessCalender();
                    task.setDueDate(businessCalendar.resolveDuedate((String)dueDate, task));
                }
            }
        }
    }
    
    protected void initializeTaskFollowUpDate(final TaskEntity task, final VariableScope variableScope) {
        final Expression followUpDateExpression = this.taskDefinition.getFollowUpDateExpression();
        if (followUpDateExpression != null) {
            final Object followUpDate = followUpDateExpression.getValue(variableScope);
            if (followUpDate != null) {
                if (followUpDate instanceof Date) {
                    task.setFollowUpDate((Date)followUpDate);
                }
                else {
                    if (!(followUpDate instanceof String)) {
                        throw new ProcessEngineException("Follow up date expression does not resolve to a Date or Date string: " + followUpDateExpression.getExpressionText());
                    }
                    final BusinessCalendar businessCalendar = this.getBusinessCalender();
                    task.setFollowUpDate(businessCalendar.resolveDuedate((String)followUpDate, task));
                }
            }
        }
    }
    
    protected void initializeTaskPriority(final TaskEntity task, final VariableScope variableScope) {
        final Expression priorityExpression = this.taskDefinition.getPriorityExpression();
        if (priorityExpression != null) {
            final Object priority = priorityExpression.getValue(variableScope);
            if (priority != null) {
                if (priority instanceof String) {
                    try {
                        task.setPriority(Integer.valueOf((String)priority));
                        return;
                    }
                    catch (NumberFormatException e) {
                        throw new ProcessEngineException("Priority does not resolve to a number: " + priority, e);
                    }
                }
                if (!(priority instanceof Number)) {
                    throw new ProcessEngineException("Priority expression does not resolve to a number: " + priorityExpression.getExpressionText());
                }
                task.setPriority(((Number)priority).intValue());
            }
        }
    }
    
    protected void initializeTaskAssignments(final TaskEntity task, final VariableScope variableScope) {
        this.initializeTaskAssignee(task, variableScope);
        this.initializeTaskCandidateUsers(task, variableScope);
        this.initializeTaskCandidateGroups(task, variableScope);
    }
    
    protected void initializeTaskAssignee(final TaskEntity task, final VariableScope variableScope) {
        final Expression assigneeExpression = this.taskDefinition.getAssigneeExpression();
        if (assigneeExpression != null) {
            task.setAssignee((String)assigneeExpression.getValue(variableScope));
        }
    }
    
    protected void initializeTaskCandidateGroups(final TaskEntity task, final VariableScope variableScope) {
        final Set<Expression> candidateGroupIdExpressions = this.taskDefinition.getCandidateGroupIdExpressions();
        for (final Expression groupIdExpr : candidateGroupIdExpressions) {
            final Object value = groupIdExpr.getValue(variableScope);
            if (value instanceof String) {
                final List<String> candiates = this.extractCandidates((String)value);
                task.addCandidateGroups(candiates);
            }
            else {
                if (!(value instanceof Collection)) {
                    throw new ProcessEngineException("Expression did not resolve to a string or collection of strings");
                }
                task.addCandidateGroups((Collection<String>)value);
            }
        }
    }
    
    protected void initializeTaskCandidateUsers(final TaskEntity task, final VariableScope variableScope) {
        final Set<Expression> candidateUserIdExpressions = this.taskDefinition.getCandidateUserIdExpressions();
        for (final Expression userIdExpr : candidateUserIdExpressions) {
            final Object value = userIdExpr.getValue(variableScope);
            if (value instanceof String) {
                final List<String> candiates = this.extractCandidates((String)value);
                task.addCandidateUsers(candiates);
            }
            else {
                if (!(value instanceof Collection)) {
                    throw new ProcessEngineException("Expression did not resolve to a string or collection of strings");
                }
                task.addCandidateUsers((Collection<String>)value);
            }
        }
    }
    
    protected List<String> extractCandidates(final String str) {
        return Arrays.asList(str.split("[\\s]*,[\\s]*"));
    }
    
    public TaskDefinition getTaskDefinition() {
        return this.taskDefinition;
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    protected BusinessCalendar getBusinessCalender() {
        return Context.getProcessEngineConfiguration().getBusinessCalendarManager().getBusinessCalendar("dueDate");
    }
}
