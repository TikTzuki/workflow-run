// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.task.TaskReport;
import org.zik.bpm.engine.task.Attachment;
import java.io.InputStream;
import org.zik.bpm.engine.task.Event;
import org.zik.bpm.engine.task.Comment;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.task.NativeTaskQuery;
import org.zik.bpm.engine.task.TaskQuery;
import org.zik.bpm.engine.task.IdentityLink;
import java.util.List;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;
import java.util.Collection;
import org.zik.bpm.engine.task.Task;

public interface TaskService
{
    Task newTask();

    Task newTask(final String p0);

    void saveTask(final Task p0);

    void deleteTask(final String p0);

    void deleteTasks(final Collection<String> p0);

    void deleteTask(final String p0, final boolean p1);

    void deleteTasks(final Collection<String> p0, final boolean p1);

    void deleteTask(final String p0, final String p1);

    void deleteTasks(final Collection<String> p0, final String p1);

    void claim(final String p0, final String p1);

    void complete(final String p0);

    void delegateTask(final String p0, final String p1);

    void resolveTask(final String p0);

    void resolveTask(final String p0, final Map<String, Object> p1);

    void complete(final String p0, final Map<String, Object> p1);

    VariableMap completeWithVariablesInReturn(final String p0, final Map<String, Object> p1, final boolean p2);

    void setAssignee(final String p0, final String p1);

    void setOwner(final String p0, final String p1);

    List<IdentityLink> getIdentityLinksForTask(final String p0);

    void addCandidateUser(final String p0, final String p1);

    void addCandidateGroup(final String p0, final String p1);

    void addUserIdentityLink(final String p0, final String p1, final String p2);

    void addGroupIdentityLink(final String p0, final String p1, final String p2);

    void deleteCandidateUser(final String p0, final String p1);

    void deleteCandidateGroup(final String p0, final String p1);

    void deleteUserIdentityLink(final String p0, final String p1, final String p2);

    void deleteGroupIdentityLink(final String p0, final String p1, final String p2);

    void setPriority(final String p0, final int p1);

    TaskQuery createTaskQuery();

    NativeTaskQuery createNativeTaskQuery();

    void setVariable(final String p0, final String p1, final Object p2);

    void setVariables(final String p0, final Map<String, ?> p1);

    void setVariableLocal(final String p0, final String p1, final Object p2);

    void setVariablesLocal(final String p0, final Map<String, ?> p1);

    Object getVariable(final String p0, final String p1);

     <T extends TypedValue> T getVariableTyped(final String p0, final String p1);

     <T extends TypedValue> T getVariableTyped(final String p0, final String p1, final boolean p2);

    Object getVariableLocal(final String p0, final String p1);

     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1);

     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1, final boolean p2);

    Map<String, Object> getVariables(final String p0);

    VariableMap getVariablesTyped(final String p0);

    VariableMap getVariablesTyped(final String p0, final boolean p1);

    Map<String, Object> getVariablesLocal(final String p0);

    VariableMap getVariablesLocalTyped(final String p0);

    VariableMap getVariablesLocalTyped(final String p0, final boolean p1);

    Map<String, Object> getVariables(final String p0, final Collection<String> p1);

    VariableMap getVariablesTyped(final String p0, final Collection<String> p1, final boolean p2);

    Map<String, Object> getVariablesLocal(final String p0, final Collection<String> p1);

    VariableMap getVariablesLocalTyped(final String p0, final Collection<String> p1, final boolean p2);

    void removeVariable(final String p0, final String p1);

    void removeVariableLocal(final String p0, final String p1);

    void removeVariables(final String p0, final Collection<String> p1);

    void removeVariablesLocal(final String p0, final Collection<String> p1);

    @Deprecated
    void addComment(final String p0, final String p1, final String p2);

    Comment createComment(final String p0, final String p1, final String p2);

    List<Comment> getTaskComments(final String p0);

    Comment getTaskComment(final String p0, final String p1);

    @Deprecated
    List<Event> getTaskEvents(final String p0);

    List<Comment> getProcessInstanceComments(final String p0);

    Attachment createAttachment(final String p0, final String p1, final String p2, final String p3, final String p4, final InputStream p5);

    Attachment createAttachment(final String p0, final String p1, final String p2, final String p3, final String p4, final String p5);

    void saveAttachment(final Attachment p0);

    Attachment getAttachment(final String p0);

    Attachment getTaskAttachment(final String p0, final String p1);

    InputStream getAttachmentContent(final String p0);

    InputStream getTaskAttachmentContent(final String p0, final String p1);

    List<Attachment> getTaskAttachments(final String p0);

    List<Attachment> getProcessInstanceAttachments(final String p0);

    void deleteAttachment(final String p0);

    void deleteTaskAttachment(final String p0, final String p1);

    List<Task> getSubTasks(final String p0);

    TaskReport createTaskReport();

    void handleBpmnError(final String p0, final String p1);

    void handleBpmnError(final String p0, final String p1, final String p2);

    void handleBpmnError(final String p0, final String p1, final String p2, final Map<String, Object> p3);

    void handleEscalation(final String p0, final String p1);

    void handleEscalation(final String p0, final String p1, final Map<String, Object> p2);
}
