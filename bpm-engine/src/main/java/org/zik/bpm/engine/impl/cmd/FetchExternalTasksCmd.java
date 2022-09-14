// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingResult;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingListener;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.externaltask.LockedExternalTaskImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.HashMap;
import org.zik.bpm.engine.impl.externaltask.TopicFetchInstruction;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.externaltask.LockedExternalTask;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class FetchExternalTasksCmd implements Command<List<LockedExternalTask>>
{
    protected static final EnginePersistenceLogger LOG;
    protected String workerId;
    protected int maxResults;
    protected boolean usePriority;
    protected Map<String, TopicFetchInstruction> fetchInstructions;
    
    public FetchExternalTasksCmd(final String workerId, final int maxResults, final Map<String, TopicFetchInstruction> instructions) {
        this(workerId, maxResults, instructions, false);
    }
    
    public FetchExternalTasksCmd(final String workerId, final int maxResults, final Map<String, TopicFetchInstruction> instructions, final boolean usePriority) {
        this.fetchInstructions = new HashMap<String, TopicFetchInstruction>();
        this.workerId = workerId;
        this.maxResults = maxResults;
        this.fetchInstructions = instructions;
        this.usePriority = usePriority;
    }
    
    @Override
    public List<LockedExternalTask> execute(final CommandContext commandContext) {
        this.validateInput();
        for (final TopicFetchInstruction instruction : this.fetchInstructions.values()) {
            instruction.ensureVariablesInitialized();
        }
        final List<ExternalTaskEntity> externalTasks = commandContext.getExternalTaskManager().selectExternalTasksForTopics(new ArrayList<TopicFetchInstruction>(this.fetchInstructions.values()), this.maxResults, this.usePriority);
        final List<LockedExternalTask> result = new ArrayList<LockedExternalTask>();
        for (final ExternalTaskEntity entity : externalTasks) {
            final TopicFetchInstruction fetchInstruction = this.fetchInstructions.get(entity.getTopicName());
            final ExecutionEntity execution = entity.getExecution(false);
            if (execution != null) {
                entity.lock(this.workerId, fetchInstruction.getLockDuration());
                final LockedExternalTaskImpl resultTask = LockedExternalTaskImpl.fromEntity(entity, fetchInstruction.getVariablesToFetch(), fetchInstruction.isLocalVariables(), fetchInstruction.isDeserializeVariables(), fetchInstruction.isIncludeExtensionProperties());
                result.add(resultTask);
            }
            else {
                FetchExternalTasksCmd.LOG.logTaskWithoutExecution(this.workerId);
            }
        }
        this.filterOnOptimisticLockingFailure(commandContext, result);
        return result;
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    protected void filterOnOptimisticLockingFailure(final CommandContext commandContext, final List<LockedExternalTask> tasks) {
        commandContext.getDbEntityManager().registerOptimisticLockingListener(new OptimisticLockingListener() {
            @Override
            public Class<? extends DbEntity> getEntityType() {
                return ExternalTaskEntity.class;
            }
            
            @Override
            public OptimisticLockingResult failedOperation(final DbOperation operation) {
                if (!(operation instanceof DbEntityOperation)) {
                    return OptimisticLockingResult.THROW;
                }
                final DbEntityOperation dbEntityOperation = (DbEntityOperation)operation;
                final DbEntity dbEntity = dbEntityOperation.getEntity();
                boolean failedOperationEntityInList = false;
                final Iterator<LockedExternalTask> it = tasks.iterator();
                while (it.hasNext()) {
                    final LockedExternalTask resultTask = it.next();
                    if (resultTask.getId().equals(dbEntity.getId())) {
                        it.remove();
                        failedOperationEntityInList = true;
                        break;
                    }
                }
                if (!failedOperationEntityInList) {
                    return OptimisticLockingResult.THROW;
                }
                return OptimisticLockingResult.IGNORE;
            }
        });
    }
    
    protected void validateInput() {
        EnsureUtil.ensureNotNull("workerId", (Object)this.workerId);
        EnsureUtil.ensureGreaterThanOrEqual("maxResults", this.maxResults, 0L);
        for (final TopicFetchInstruction instruction : this.fetchInstructions.values()) {
            EnsureUtil.ensureNotNull("topicName", (Object)instruction.getTopicName());
            EnsureUtil.ensurePositive("lockTime", instruction.getLockDuration());
        }
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
