// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.deletion;

import org.zik.bpm.engine.impl.batch.DeploymentMapping;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.DeleteProcessInstancesCmd;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class DeleteProcessInstancesJobHandler extends AbstractBatchJobHandler<DeleteProcessInstanceBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "instance-deletion";
    }
    
    @Override
    protected DeleteProcessInstanceBatchConfigurationJsonConverter getJsonConverterInstance() {
        return DeleteProcessInstanceBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return DeleteProcessInstancesJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected DeleteProcessInstanceBatchConfiguration createJobConfiguration(final DeleteProcessInstanceBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new DeleteProcessInstanceBatchConfiguration(processIdsForJob, null, configuration.getDeleteReason(), configuration.isSkipCustomListeners(), configuration.isSkipSubprocesses(), configuration.isFailIfNotExists());
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final DeleteProcessInstanceBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        commandContext.executeWithOperationLogPrevented((Command<Object>)new DeleteProcessInstancesCmd(batchConfiguration.getIds(), batchConfiguration.getDeleteReason(), batchConfiguration.isSkipCustomListeners(), true, batchConfiguration.isSkipSubprocesses(), batchConfiguration.isFailIfNotExists()));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    @Override
    protected void createJobEntities(final BatchEntity batch, final DeleteProcessInstanceBatchConfiguration configuration, final String deploymentId, final List<String> processIds, final int invocationsPerBatchJob) {
        if (deploymentId == null && (configuration.getIdMappings() == null || configuration.getIdMappings().isEmpty())) {
            final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
            final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(configuration.getIds()));
            elementConfiguration.addDeploymentMappings(query.listDeploymentIdMappings(), configuration.getIds());
            elementConfiguration.getMappings().forEach(mapping -> super.createJobEntities(batch, configuration, mapping.getDeploymentId(), mapping.getIds(processIds), invocationsPerBatchJob));
        }
        else {
            super.createJobEntities(batch, configuration, deploymentId, processIds, invocationsPerBatchJob);
        }
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("instance-deletion");
    }
}
