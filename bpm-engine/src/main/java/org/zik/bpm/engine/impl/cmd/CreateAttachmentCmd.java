// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Date;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.util.IoUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.io.InputStream;
import org.zik.bpm.engine.task.Attachment;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateAttachmentCmd implements Command<Attachment>
{
    protected String taskId;
    protected String attachmentType;
    protected String processInstanceId;
    protected String attachmentName;
    protected String attachmentDescription;
    protected InputStream content;
    protected String url;
    private TaskEntity task;
    protected ExecutionEntity processInstance;
    
    public CreateAttachmentCmd(final String attachmentType, final String taskId, final String processInstanceId, final String attachmentName, final String attachmentDescription, final InputStream content, final String url) {
        this.attachmentType = attachmentType;
        this.taskId = taskId;
        this.processInstanceId = processInstanceId;
        this.attachmentName = attachmentName;
        this.attachmentDescription = attachmentDescription;
        this.content = content;
        this.url = url;
    }
    
    @Override
    public Attachment execute(final CommandContext commandContext) {
        if (this.taskId != null) {
            this.task = commandContext.getTaskManager().findTaskById(this.taskId);
        }
        else {
            EnsureUtil.ensureNotNull("taskId or processInstanceId has to be provided", (Object)this.processInstanceId);
            final List<ExecutionEntity> executionsByProcessInstanceId = commandContext.getExecutionManager().findExecutionsByProcessInstanceId(this.processInstanceId);
            this.processInstance = executionsByProcessInstanceId.get(0);
        }
        final AttachmentEntity attachment = new AttachmentEntity();
        attachment.setName(this.attachmentName);
        attachment.setDescription(this.attachmentDescription);
        attachment.setType(this.attachmentType);
        attachment.setTaskId(this.taskId);
        attachment.setProcessInstanceId(this.processInstanceId);
        attachment.setUrl(this.url);
        attachment.setCreateTime(ClockUtil.getCurrentTime());
        if (this.task != null) {
            final ExecutionEntity execution = this.task.getExecution();
            if (execution != null) {
                attachment.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            }
        }
        else if (this.processInstance != null) {
            attachment.setRootProcessInstanceId(this.processInstance.getRootProcessInstanceId());
        }
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(attachment);
        }
        final DbEntityManager dbEntityManger = commandContext.getDbEntityManager();
        dbEntityManger.insert(attachment);
        if (this.content != null) {
            final byte[] bytes = IoUtil.readInputStream(this.content, this.attachmentName);
            final ByteArrayEntity byteArray = new ByteArrayEntity(bytes, ResourceTypes.HISTORY);
            byteArray.setRootProcessInstanceId(attachment.getRootProcessInstanceId());
            byteArray.setRemovalTime(attachment.getRemovalTime());
            commandContext.getByteArrayManager().insertByteArray(byteArray);
            attachment.setContentId(byteArray.getId());
        }
        final PropertyChange propertyChange = new PropertyChange("name", null, this.attachmentName);
        if (this.task != null) {
            commandContext.getOperationLogManager().logAttachmentOperation("AddAttachment", this.task, propertyChange);
            this.task.triggerUpdateEvent();
        }
        else if (this.processInstance != null) {
            commandContext.getOperationLogManager().logAttachmentOperation("AddAttachment", this.processInstance, propertyChange);
        }
        return attachment;
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
    
    protected void provideRemovalTime(final AttachmentEntity attachment) {
        final String rootProcessInstanceId = attachment.getRootProcessInstanceId();
        if (rootProcessInstanceId != null) {
            final HistoricProcessInstanceEventEntity historicRootProcessInstance = this.getHistoricRootProcessInstance(rootProcessInstanceId);
            if (historicRootProcessInstance != null) {
                final Date removalTime = historicRootProcessInstance.getRemovalTime();
                attachment.setRemovalTime(removalTime);
            }
        }
    }
}
