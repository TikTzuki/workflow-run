// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Date;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.CommentEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Comment;
import org.zik.bpm.engine.impl.interceptor.Command;

public class AddCommentCmd implements Command<Comment>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String processInstanceId;
    protected String message;
    
    public AddCommentCmd(final String taskId, final String processInstanceId, final String message) {
        this.taskId = taskId;
        this.processInstanceId = processInstanceId;
        this.message = message;
    }
    
    @Override
    public Comment execute(final CommandContext commandContext) {
        if (this.processInstanceId == null && this.taskId == null) {
            throw new ProcessEngineException("Process instance id and task id is null");
        }
        EnsureUtil.ensureNotNull("Message", (Object)this.message);
        final String userId = commandContext.getAuthenticatedUserId();
        final CommentEntity comment = new CommentEntity();
        comment.setUserId(userId);
        comment.setType("comment");
        comment.setTime(ClockUtil.getCurrentTime());
        comment.setTaskId(this.taskId);
        comment.setProcessInstanceId(this.processInstanceId);
        comment.setAction("AddComment");
        final ExecutionEntity execution = this.getExecution(commandContext);
        if (execution != null) {
            comment.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        }
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(comment);
        }
        String eventMessage = this.message.replaceAll("\\s+", " ");
        if (eventMessage.length() > 163) {
            eventMessage = eventMessage.substring(0, 160) + "...";
        }
        comment.setMessage(eventMessage);
        comment.setFullMessage(this.message);
        commandContext.getCommentManager().insert(comment);
        final TaskEntity task = this.getTask(commandContext);
        if (task != null) {
            task.triggerUpdateEvent();
        }
        return comment;
    }
    
    protected ExecutionEntity getExecution(final CommandContext commandContext) {
        if (this.taskId == null) {
            return this.getProcessInstance(commandContext);
        }
        final TaskEntity task = this.getTask(commandContext);
        if (task != null) {
            return task.getExecution();
        }
        return null;
    }
    
    protected ExecutionEntity getProcessInstance(final CommandContext commandContext) {
        if (this.processInstanceId != null) {
            return commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        }
        return null;
    }
    
    protected TaskEntity getTask(final CommandContext commandContext) {
        if (this.taskId != null) {
            return commandContext.getTaskManager().findTaskById(this.taskId);
        }
        return null;
    }
    
    protected boolean isHistoryRemovalTimeStrategyStart() {
        return "start".equals(this.getHistoryRemovalTimeStrategy());
    }
    
    protected String getHistoryRemovalTimeStrategy() {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected HistoricProcessInstanceEventEntity getHistoricRootProcessInstance(final String rootProcessInstanceId) {
        return Context.getCommandContext().getDbEntityManager().selectById(HistoricProcessInstanceEventEntity.class, rootProcessInstanceId);
    }
    
    protected void provideRemovalTime(final CommentEntity comment) {
        final String rootProcessInstanceId = comment.getRootProcessInstanceId();
        if (rootProcessInstanceId != null) {
            final HistoricProcessInstanceEventEntity historicRootProcessInstance = this.getHistoricRootProcessInstance(rootProcessInstanceId);
            if (historicRootProcessInstance != null) {
                final Date removalTime = historicRootProcessInstance.getRemovalTime();
                comment.setRemovalTime(removalTime);
            }
        }
    }
}
