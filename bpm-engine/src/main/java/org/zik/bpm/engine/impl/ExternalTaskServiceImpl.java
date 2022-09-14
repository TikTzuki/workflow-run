// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.ExtendLockOnExternalTaskCmd;
import org.zik.bpm.engine.impl.cmd.UpdateExternalTaskRetriesBuilderImpl;
import org.zik.bpm.engine.externaltask.UpdateExternalTaskRetriesSelectBuilder;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.cmd.GetExternalTaskErrorDetailsCmd;
import org.zik.bpm.engine.impl.cmd.GetTopicNamesCmd;
import java.util.List;
import org.zik.bpm.engine.externaltask.ExternalTaskQuery;
import org.zik.bpm.engine.impl.cmd.SetExternalTaskPriorityCmd;
import org.zik.bpm.engine.impl.cmd.SetExternalTaskRetriesCmd;
import org.zik.bpm.engine.impl.cmd.UnlockExternalTaskCmd;
import org.zik.bpm.engine.impl.cmd.HandleExternalTaskBpmnErrorCmd;
import org.zik.bpm.engine.impl.cmd.HandleExternalTaskFailureCmd;
import org.zik.bpm.engine.impl.cmd.CompleteExternalTaskCmd;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.LockExternalTaskCmd;
import org.zik.bpm.engine.impl.externaltask.ExternalTaskQueryTopicBuilderImpl;
import org.zik.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.zik.bpm.engine.ExternalTaskService;

public class ExternalTaskServiceImpl extends ServiceImpl implements ExternalTaskService
{
    @Override
    public ExternalTaskQueryBuilder fetchAndLock(final int maxTasks, final String workerId) {
        return this.fetchAndLock(maxTasks, workerId, false);
    }
    
    @Override
    public ExternalTaskQueryBuilder fetchAndLock(final int maxTasks, final String workerId, final boolean usePriority) {
        return new ExternalTaskQueryTopicBuilderImpl(this.commandExecutor, workerId, maxTasks, usePriority);
    }
    
    @Override
    public void lock(final String externalTaskId, final String workerId, final long lockDuration) {
        this.commandExecutor.execute((Command<Object>)new LockExternalTaskCmd(externalTaskId, workerId, lockDuration));
    }
    
    @Override
    public void complete(final String externalTaskId, final String workerId) {
        this.complete(externalTaskId, workerId, null, null);
    }
    
    @Override
    public void complete(final String externalTaskId, final String workerId, final Map<String, Object> variables) {
        this.complete(externalTaskId, workerId, variables, null);
    }
    
    @Override
    public void complete(final String externalTaskId, final String workerId, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        this.commandExecutor.execute((Command<Object>)new CompleteExternalTaskCmd(externalTaskId, workerId, variables, localVariables));
    }
    
    @Override
    public void handleFailure(final String externalTaskId, final String workerId, final String errorMessage, final int retries, final long retryDuration) {
        this.handleFailure(externalTaskId, workerId, errorMessage, null, retries, retryDuration);
    }
    
    @Override
    public void handleFailure(final String externalTaskId, final String workerId, final String errorMessage, final String errorDetails, final int retries, final long retryDuration) {
        this.handleFailure(externalTaskId, workerId, errorMessage, errorDetails, retries, retryDuration, null, null);
    }
    
    @Override
    public void handleFailure(final String externalTaskId, final String workerId, final String errorMessage, final String errorDetails, final int retries, final long retryDuration, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        this.commandExecutor.execute((Command<Object>)new HandleExternalTaskFailureCmd(externalTaskId, workerId, errorMessage, errorDetails, retries, retryDuration, variables, localVariables));
    }
    
    @Override
    public void handleBpmnError(final String externalTaskId, final String workerId, final String errorCode) {
        this.handleBpmnError(externalTaskId, workerId, errorCode, null, null);
    }
    
    @Override
    public void handleBpmnError(final String externalTaskId, final String workerId, final String errorCode, final String errorMessage) {
        this.handleBpmnError(externalTaskId, workerId, errorCode, errorMessage, null);
    }
    
    @Override
    public void handleBpmnError(final String externalTaskId, final String workerId, final String errorCode, final String errorMessage, final Map<String, Object> variables) {
        this.commandExecutor.execute((Command<Object>)new HandleExternalTaskBpmnErrorCmd(externalTaskId, workerId, errorCode, errorMessage, variables));
    }
    
    @Override
    public void unlock(final String externalTaskId) {
        this.commandExecutor.execute((Command<Object>)new UnlockExternalTaskCmd(externalTaskId));
    }
    
    public void setRetries(final String externalTaskId, final int retries, final boolean writeUserOperationLog) {
        this.commandExecutor.execute((Command<Object>)new SetExternalTaskRetriesCmd(externalTaskId, retries, writeUserOperationLog));
    }
    
    @Override
    public void setPriority(final String externalTaskId, final long priority) {
        this.commandExecutor.execute((Command<Object>)new SetExternalTaskPriorityCmd(externalTaskId, priority));
    }
    
    @Override
    public ExternalTaskQuery createExternalTaskQuery() {
        return new ExternalTaskQueryImpl(this.commandExecutor);
    }
    
    @Override
    public List<String> getTopicNames() {
        return this.commandExecutor.execute((Command<List<String>>)new GetTopicNamesCmd(false, false, false));
    }
    
    @Override
    public List<String> getTopicNames(final boolean withLockedTasks, final boolean withUnlockedTasks, final boolean withRetriesLeft) {
        return this.commandExecutor.execute((Command<List<String>>)new GetTopicNamesCmd(withLockedTasks, withUnlockedTasks, withRetriesLeft));
    }
    
    @Override
    public String getExternalTaskErrorDetails(final String externalTaskId) {
        return this.commandExecutor.execute((Command<String>)new GetExternalTaskErrorDetailsCmd(externalTaskId));
    }
    
    @Override
    public void setRetries(final String externalTaskId, final int retries) {
        this.setRetries(externalTaskId, retries, true);
    }
    
    @Override
    public void setRetries(final List<String> externalTaskIds, final int retries) {
        this.updateRetries().externalTaskIds(externalTaskIds).set(retries);
    }
    
    @Override
    public Batch setRetriesAsync(final List<String> externalTaskIds, final ExternalTaskQuery externalTaskQuery, final int retries) {
        return this.updateRetries().externalTaskIds(externalTaskIds).externalTaskQuery(externalTaskQuery).setAsync(retries);
    }
    
    @Override
    public UpdateExternalTaskRetriesSelectBuilder updateRetries() {
        return new UpdateExternalTaskRetriesBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public void extendLock(final String externalTaskId, final String workerId, final long lockDuration) {
        this.commandExecutor.execute((Command<Object>)new ExtendLockOnExternalTaskCmd(externalTaskId, workerId, lockDuration));
    }
}
