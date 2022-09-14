// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.HandleTaskEscalationCmd;
import org.zik.bpm.engine.impl.cmd.HandleTaskBpmnErrorCmd;
import org.zik.bpm.engine.task.TaskReport;
import org.zik.bpm.engine.impl.cmd.GetSubTasksCmd;
import org.zik.bpm.engine.impl.cmd.SaveAttachmentCmd;
import org.zik.bpm.engine.impl.cmd.GetProcessInstanceAttachmentsCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskAttachmentsCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskAttachmentCmd;
import org.zik.bpm.engine.impl.cmd.GetAttachmentCmd;
import org.zik.bpm.engine.impl.cmd.DeleteTaskAttachmentCmd;
import org.zik.bpm.engine.impl.cmd.DeleteAttachmentCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskAttachmentContentCmd;
import org.zik.bpm.engine.impl.cmd.GetAttachmentContentCmd;
import org.zik.bpm.engine.impl.cmd.CreateAttachmentCmd;
import org.zik.bpm.engine.task.Attachment;
import java.io.InputStream;
import org.zik.bpm.engine.impl.cmd.GetProcessInstanceCommentsCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskEventsCmd;
import org.zik.bpm.engine.task.Event;
import org.zik.bpm.engine.impl.cmd.GetTaskCommentCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskCommentsCmd;
import org.zik.bpm.engine.impl.cmd.AddCommentCmd;
import org.zik.bpm.engine.task.Comment;
import org.zik.bpm.engine.impl.cmd.RemoveTaskVariablesCmd;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cmd.PatchTaskVariablesCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.cmd.SetTaskVariablesCmd;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.cmd.GetTaskVariableCmdTyped;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.cmd.GetTaskVariableCmd;
import org.zik.bpm.engine.impl.cmd.GetTaskVariablesCmd;
import org.zik.bpm.engine.task.NativeTaskQuery;
import org.zik.bpm.engine.task.TaskQuery;
import org.zik.bpm.engine.impl.cmd.SetTaskPriorityCmd;
import org.zik.bpm.engine.impl.cmd.ResolveTaskCmd;
import org.zik.bpm.engine.impl.cmd.DelegateTaskCmd;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.cmd.CompleteTaskCmd;
import java.util.Map;
import org.zik.bpm.engine.impl.cmd.ClaimTaskCmd;
import org.zik.bpm.engine.impl.cmd.GetIdentityLinksForTaskCmd;
import org.zik.bpm.engine.task.IdentityLink;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.DeleteUserIdentityLinkCmd;
import org.zik.bpm.engine.impl.cmd.DeleteGroupIdentityLinkCmd;
import org.zik.bpm.engine.impl.cmd.AddGroupIdentityLinkCmd;
import org.zik.bpm.engine.impl.cmd.AddUserIdentityLinkCmd;
import org.zik.bpm.engine.impl.cmd.SetTaskOwnerCmd;
import org.zik.bpm.engine.impl.cmd.AssignTaskCmd;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmd.DeleteTaskCmd;
import org.zik.bpm.engine.impl.cmd.SaveTaskCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CreateTaskCmd;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.TaskService;

public class TaskServiceImpl extends ServiceImpl implements TaskService
{
    @Override
    public Task newTask() {
        return this.newTask(null);
    }
    
    @Override
    public Task newTask(final String taskId) {
        return this.commandExecutor.execute((Command<Task>)new CreateTaskCmd(taskId));
    }
    
    @Override
    public void saveTask(final Task task) {
        this.commandExecutor.execute((Command<Object>)new SaveTaskCmd(task));
    }
    
    @Override
    public void deleteTask(final String taskId) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskId, null, false));
    }
    
    @Override
    public void deleteTasks(final Collection<String> taskIds) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskIds, null, false));
    }
    
    @Override
    public void deleteTask(final String taskId, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskId, null, cascade));
    }
    
    @Override
    public void deleteTasks(final Collection<String> taskIds, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskIds, null, cascade));
    }
    
    @Override
    public void deleteTask(final String taskId, final String deleteReason) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskId, deleteReason, false));
    }
    
    @Override
    public void deleteTasks(final Collection<String> taskIds, final String deleteReason) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskCmd(taskIds, deleteReason, false));
    }
    
    @Override
    public void setAssignee(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new AssignTaskCmd(taskId, userId));
    }
    
    @Override
    public void setOwner(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new SetTaskOwnerCmd(taskId, userId));
    }
    
    @Override
    public void addCandidateUser(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new AddUserIdentityLinkCmd(taskId, userId, "candidate"));
    }
    
    @Override
    public void addCandidateGroup(final String taskId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new AddGroupIdentityLinkCmd(taskId, groupId, "candidate"));
    }
    
    @Override
    public void addUserIdentityLink(final String taskId, final String userId, final String identityLinkType) {
        this.commandExecutor.execute((Command<Object>)new AddUserIdentityLinkCmd(taskId, userId, identityLinkType));
    }
    
    @Override
    public void addGroupIdentityLink(final String taskId, final String groupId, final String identityLinkType) {
        this.commandExecutor.execute((Command<Object>)new AddGroupIdentityLinkCmd(taskId, groupId, identityLinkType));
    }
    
    @Override
    public void deleteCandidateGroup(final String taskId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new DeleteGroupIdentityLinkCmd(taskId, groupId, "candidate"));
    }
    
    @Override
    public void deleteCandidateUser(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserIdentityLinkCmd(taskId, userId, "candidate"));
    }
    
    @Override
    public void deleteGroupIdentityLink(final String taskId, final String groupId, final String identityLinkType) {
        this.commandExecutor.execute((Command<Object>)new DeleteGroupIdentityLinkCmd(taskId, groupId, identityLinkType));
    }
    
    @Override
    public void deleteUserIdentityLink(final String taskId, final String userId, final String identityLinkType) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserIdentityLinkCmd(taskId, userId, identityLinkType));
    }
    
    @Override
    public List<IdentityLink> getIdentityLinksForTask(final String taskId) {
        return this.commandExecutor.execute((Command<List<IdentityLink>>)new GetIdentityLinksForTaskCmd(taskId));
    }
    
    @Override
    public void claim(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new ClaimTaskCmd(taskId, userId));
    }
    
    @Override
    public void complete(final String taskId) {
        this.complete(taskId, null);
    }
    
    @Override
    public void complete(final String taskId, final Map<String, Object> variables) {
        this.commandExecutor.execute((Command<Object>)new CompleteTaskCmd(taskId, variables, false, false));
    }
    
    @Override
    public VariableMap completeWithVariablesInReturn(final String taskId, final Map<String, Object> variables, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new CompleteTaskCmd(taskId, variables, true, deserializeValues));
    }
    
    @Override
    public void delegateTask(final String taskId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new DelegateTaskCmd(taskId, userId));
    }
    
    @Override
    public void resolveTask(final String taskId) {
        this.commandExecutor.execute((Command<Object>)new ResolveTaskCmd(taskId, null));
    }
    
    @Override
    public void resolveTask(final String taskId, final Map<String, Object> variables) {
        this.commandExecutor.execute((Command<Object>)new ResolveTaskCmd(taskId, variables));
    }
    
    @Override
    public void setPriority(final String taskId, final int priority) {
        this.commandExecutor.execute((Command<Object>)new SetTaskPriorityCmd(taskId, priority));
    }
    
    @Override
    public TaskQuery createTaskQuery() {
        return new TaskQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeTaskQuery createNativeTaskQuery() {
        return new NativeTaskQueryImpl(this.commandExecutor);
    }
    
    public VariableMap getVariables(final String taskId) {
        return this.getVariablesTyped(taskId);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String taskId) {
        return this.getVariablesTyped(taskId, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String taskId, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetTaskVariablesCmd(taskId, null, false, deserializeValues));
    }
    
    public VariableMap getVariablesLocal(final String taskId) {
        return this.getVariablesLocalTyped(taskId);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String taskId) {
        return this.getVariablesLocalTyped(taskId, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String taskId, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetTaskVariablesCmd(taskId, null, true, deserializeValues));
    }
    
    public VariableMap getVariables(final String taskId, final Collection<String> variableNames) {
        return this.getVariablesTyped(taskId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String taskId, final Collection<String> variableNames, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetTaskVariablesCmd(taskId, variableNames, false, deserializeValues));
    }
    
    public VariableMap getVariablesLocal(final String taskId, final Collection<String> variableNames) {
        return this.getVariablesLocalTyped(taskId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String taskId, final Collection<String> variableNames, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetTaskVariablesCmd(taskId, variableNames, true, deserializeValues));
    }
    
    @Override
    public Object getVariable(final String taskId, final String variableName) {
        return this.commandExecutor.execute((Command<Object>)new GetTaskVariableCmd(taskId, variableName, false));
    }
    
    @Override
    public Object getVariableLocal(final String taskId, final String variableName) {
        return this.commandExecutor.execute((Command<Object>)new GetTaskVariableCmd(taskId, variableName, true));
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String taskId, final String variableName) {
        return this.getVariableTyped(taskId, variableName, false, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String taskId, final String variableName, final boolean deserializeValue) {
        return this.getVariableTyped(taskId, variableName, false, deserializeValue);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String taskId, final String variableName) {
        return this.getVariableTyped(taskId, variableName, true, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String taskId, final String variableName, final boolean deserializeValue) {
        return this.getVariableTyped(taskId, variableName, true, deserializeValue);
    }
    
    protected <T extends TypedValue> T getVariableTyped(final String taskId, final String variableName, final boolean isLocal, final boolean deserializeValue) {
        return this.commandExecutor.execute((Command<T>)new GetTaskVariableCmdTyped(taskId, variableName, isLocal, deserializeValue));
    }
    
    @Override
    public void setVariable(final String taskId, final String variableName, final Object value) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(variableName, value);
        this.setVariables(taskId, variables, false);
    }
    
    @Override
    public void setVariableLocal(final String taskId, final String variableName, final Object value) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(variableName, value);
        this.setVariables(taskId, variables, true);
    }
    
    @Override
    public void setVariables(final String taskId, final Map<String, ?> variables) {
        this.setVariables(taskId, variables, false);
    }
    
    @Override
    public void setVariablesLocal(final String taskId, final Map<String, ?> variables) {
        this.setVariables(taskId, variables, true);
    }
    
    protected void setVariables(final String taskId, final Map<String, ?> variables, final boolean local) {
        try {
            this.commandExecutor.execute((Command<Object>)new SetTaskVariablesCmd(taskId, variables, local));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }
    
    public void updateVariablesLocal(final String taskId, final Map<String, ?> modifications, final Collection<String> deletions) {
        this.updateVariables(taskId, modifications, deletions, true);
    }
    
    public void updateVariables(final String taskId, final Map<String, ?> modifications, final Collection<String> deletions) {
        this.updateVariables(taskId, modifications, deletions, false);
    }
    
    protected void updateVariables(final String taskId, final Map<String, ?> modifications, final Collection<String> deletions, final boolean local) {
        try {
            this.commandExecutor.execute((Command<Object>)new PatchTaskVariablesCmd(taskId, modifications, deletions, local));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public void removeVariable(final String taskId, final String variableName) {
        final Collection<String> variableNames = new ArrayList<String>();
        variableNames.add(variableName);
        this.commandExecutor.execute((Command<Object>)new RemoveTaskVariablesCmd(taskId, variableNames, false));
    }
    
    @Override
    public void removeVariableLocal(final String taskId, final String variableName) {
        final Collection<String> variableNames = new ArrayList<String>(1);
        variableNames.add(variableName);
        this.commandExecutor.execute((Command<Object>)new RemoveTaskVariablesCmd(taskId, variableNames, true));
    }
    
    @Override
    public void removeVariables(final String taskId, final Collection<String> variableNames) {
        this.commandExecutor.execute((Command<Object>)new RemoveTaskVariablesCmd(taskId, variableNames, false));
    }
    
    @Override
    public void removeVariablesLocal(final String taskId, final Collection<String> variableNames) {
        this.commandExecutor.execute((Command<Object>)new RemoveTaskVariablesCmd(taskId, variableNames, true));
    }
    
    @Override
    public void addComment(final String taskId, final String processInstance, final String message) {
        this.createComment(taskId, processInstance, message);
    }
    
    @Override
    public Comment createComment(final String taskId, final String processInstance, final String message) {
        return this.commandExecutor.execute((Command<Comment>)new AddCommentCmd(taskId, processInstance, message));
    }
    
    @Override
    public List<Comment> getTaskComments(final String taskId) {
        return this.commandExecutor.execute((Command<List<Comment>>)new GetTaskCommentsCmd(taskId));
    }
    
    @Override
    public Comment getTaskComment(final String taskId, final String commentId) {
        return this.commandExecutor.execute((Command<Comment>)new GetTaskCommentCmd(taskId, commentId));
    }
    
    @Override
    public List<Event> getTaskEvents(final String taskId) {
        return this.commandExecutor.execute((Command<List<Event>>)new GetTaskEventsCmd(taskId));
    }
    
    @Override
    public List<Comment> getProcessInstanceComments(final String processInstanceId) {
        return this.commandExecutor.execute((Command<List<Comment>>)new GetProcessInstanceCommentsCmd(processInstanceId));
    }
    
    @Override
    public Attachment createAttachment(final String attachmentType, final String taskId, final String processInstanceId, final String attachmentName, final String attachmentDescription, final InputStream content) {
        return this.commandExecutor.execute((Command<Attachment>)new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content, null));
    }
    
    @Override
    public Attachment createAttachment(final String attachmentType, final String taskId, final String processInstanceId, final String attachmentName, final String attachmentDescription, final String url) {
        return this.commandExecutor.execute((Command<Attachment>)new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, null, url));
    }
    
    @Override
    public InputStream getAttachmentContent(final String attachmentId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetAttachmentContentCmd(attachmentId));
    }
    
    @Override
    public InputStream getTaskAttachmentContent(final String taskId, final String attachmentId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetTaskAttachmentContentCmd(taskId, attachmentId));
    }
    
    @Override
    public void deleteAttachment(final String attachmentId) {
        this.commandExecutor.execute((Command<Object>)new DeleteAttachmentCmd(attachmentId));
    }
    
    @Override
    public void deleteTaskAttachment(final String taskId, final String attachmentId) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskAttachmentCmd(taskId, attachmentId));
    }
    
    @Override
    public Attachment getAttachment(final String attachmentId) {
        return this.commandExecutor.execute((Command<Attachment>)new GetAttachmentCmd(attachmentId));
    }
    
    @Override
    public Attachment getTaskAttachment(final String taskId, final String attachmentId) {
        return this.commandExecutor.execute((Command<Attachment>)new GetTaskAttachmentCmd(taskId, attachmentId));
    }
    
    @Override
    public List<Attachment> getTaskAttachments(final String taskId) {
        return this.commandExecutor.execute((Command<List<Attachment>>)new GetTaskAttachmentsCmd(taskId));
    }
    
    @Override
    public List<Attachment> getProcessInstanceAttachments(final String processInstanceId) {
        return this.commandExecutor.execute((Command<List<Attachment>>)new GetProcessInstanceAttachmentsCmd(processInstanceId));
    }
    
    @Override
    public void saveAttachment(final Attachment attachment) {
        this.commandExecutor.execute((Command<Object>)new SaveAttachmentCmd(attachment));
    }
    
    @Override
    public List<Task> getSubTasks(final String parentTaskId) {
        return this.commandExecutor.execute((Command<List<Task>>)new GetSubTasksCmd(parentTaskId));
    }
    
    @Override
    public TaskReport createTaskReport() {
        return new TaskReportImpl(this.commandExecutor);
    }
    
    @Override
    public void handleBpmnError(final String taskId, final String errorCode) {
        this.commandExecutor.execute((Command<Object>)new HandleTaskBpmnErrorCmd(taskId, errorCode));
    }
    
    @Override
    public void handleBpmnError(final String taskId, final String errorCode, final String errorMessage) {
        this.commandExecutor.execute((Command<Object>)new HandleTaskBpmnErrorCmd(taskId, errorCode, errorMessage));
    }
    
    @Override
    public void handleBpmnError(final String taskId, final String errorCode, final String errorMessage, final Map<String, Object> variables) {
        this.commandExecutor.execute((Command<Object>)new HandleTaskBpmnErrorCmd(taskId, errorCode, errorMessage, variables));
    }
    
    @Override
    public void handleEscalation(final String taskId, final String escalationCode) {
        this.commandExecutor.execute((Command<Object>)new HandleTaskEscalationCmd(taskId, escalationCode));
    }
    
    @Override
    public void handleEscalation(final String taskId, final String escalationCode, final Map<String, Object> variables) {
        this.commandExecutor.execute((Command<Object>)new HandleTaskEscalationCmd(taskId, escalationCode, variables));
    }
}
