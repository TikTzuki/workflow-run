// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.HashMap;
import java.util.HashSet;
import org.zik.bpm.engine.delegate.TaskListener;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.impl.form.handler.TaskFormHandler;
import java.util.Set;
import org.zik.bpm.engine.delegate.Expression;

public class TaskDefinition
{
    protected String key;
    protected Expression nameExpression;
    protected Expression descriptionExpression;
    protected Expression assigneeExpression;
    protected Set<Expression> candidateUserIdExpressions;
    protected Set<Expression> candidateGroupIdExpressions;
    protected Expression dueDateExpression;
    protected Expression followUpDateExpression;
    protected Expression priorityExpression;
    protected TaskFormHandler taskFormHandler;
    protected FormDefinition formDefinition;
    protected Map<String, List<TaskListener>> taskListeners;
    protected Map<String, List<TaskListener>> builtinTaskListeners;
    protected Map<String, TaskListener> timeoutTaskListeners;
    
    public TaskDefinition(final TaskFormHandler taskFormHandler) {
        this.candidateUserIdExpressions = new HashSet<Expression>();
        this.candidateGroupIdExpressions = new HashSet<Expression>();
        this.formDefinition = new FormDefinition();
        this.taskListeners = new HashMap<String, List<TaskListener>>();
        this.builtinTaskListeners = new HashMap<String, List<TaskListener>>();
        this.timeoutTaskListeners = new HashMap<String, TaskListener>();
        this.taskFormHandler = taskFormHandler;
    }
    
    public Expression getNameExpression() {
        return this.nameExpression;
    }
    
    public void setNameExpression(final Expression nameExpression) {
        this.nameExpression = nameExpression;
    }
    
    public Expression getDescriptionExpression() {
        return this.descriptionExpression;
    }
    
    public void setDescriptionExpression(final Expression descriptionExpression) {
        this.descriptionExpression = descriptionExpression;
    }
    
    public Expression getAssigneeExpression() {
        return this.assigneeExpression;
    }
    
    public void setAssigneeExpression(final Expression assigneeExpression) {
        this.assigneeExpression = assigneeExpression;
    }
    
    public Set<Expression> getCandidateUserIdExpressions() {
        return this.candidateUserIdExpressions;
    }
    
    public void addCandidateUserIdExpression(final Expression userId) {
        this.candidateUserIdExpressions.add(userId);
    }
    
    public Set<Expression> getCandidateGroupIdExpressions() {
        return this.candidateGroupIdExpressions;
    }
    
    public void addCandidateGroupIdExpression(final Expression groupId) {
        this.candidateGroupIdExpressions.add(groupId);
    }
    
    public Expression getPriorityExpression() {
        return this.priorityExpression;
    }
    
    public void setPriorityExpression(final Expression priorityExpression) {
        this.priorityExpression = priorityExpression;
    }
    
    public TaskFormHandler getTaskFormHandler() {
        return this.taskFormHandler;
    }
    
    public void setTaskFormHandler(final TaskFormHandler taskFormHandler) {
        this.taskFormHandler = taskFormHandler;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public Expression getDueDateExpression() {
        return this.dueDateExpression;
    }
    
    public void setDueDateExpression(final Expression dueDateExpression) {
        this.dueDateExpression = dueDateExpression;
    }
    
    public Expression getFollowUpDateExpression() {
        return this.followUpDateExpression;
    }
    
    public void setFollowUpDateExpression(final Expression followUpDateExpression) {
        this.followUpDateExpression = followUpDateExpression;
    }
    
    public Map<String, List<TaskListener>> getTaskListeners() {
        return this.taskListeners;
    }
    
    public Map<String, List<TaskListener>> getBuiltinTaskListeners() {
        return this.builtinTaskListeners;
    }
    
    public void setTaskListeners(final Map<String, List<TaskListener>> taskListeners) {
        this.taskListeners = taskListeners;
    }
    
    public List<TaskListener> getTaskListeners(final String eventName) {
        return this.taskListeners.get(eventName);
    }
    
    public List<TaskListener> getBuiltinTaskListeners(final String eventName) {
        return this.builtinTaskListeners.get(eventName);
    }
    
    public TaskListener getTimeoutTaskListener(final String timeoutId) {
        return this.timeoutTaskListeners.get(timeoutId);
    }
    
    public void addTaskListener(final String eventName, final TaskListener taskListener) {
        CollectionUtil.addToMapOfLists(this.taskListeners, eventName, taskListener);
    }
    
    public void addBuiltInTaskListener(final String eventName, final TaskListener taskListener) {
        List<TaskListener> listeners = this.taskListeners.get(eventName);
        if (listeners == null) {
            listeners = new ArrayList<TaskListener>();
            this.taskListeners.put(eventName, listeners);
        }
        listeners.add(0, taskListener);
        CollectionUtil.addToMapOfLists(this.builtinTaskListeners, eventName, taskListener);
    }
    
    public void addTimeoutTaskListener(final String timeoutId, final TaskListener taskListener) {
        this.timeoutTaskListeners.put(timeoutId, taskListener);
    }
    
    public FormDefinition getFormDefinition() {
        return this.formDefinition;
    }
    
    public void setFormDefinition(final FormDefinition formDefinition) {
        this.formDefinition = formDefinition;
    }
    
    public Expression getFormKey() {
        return this.formDefinition.getFormKey();
    }
    
    public void setFormKey(final Expression formKey) {
        this.formDefinition.setFormKey(formKey);
    }
    
    public Expression getCamundaFormDefinitionKey() {
        return this.formDefinition.getCamundaFormDefinitionKey();
    }
    
    public String getCamundaFormDefinitionBinding() {
        return this.formDefinition.getCamundaFormDefinitionBinding();
    }
    
    public Expression getCamundaFormDefinitionVersion() {
        return this.formDefinition.getCamundaFormDefinitionVersion();
    }
}
