// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ModificationBuilderImpl;

public class ProcessInstanceModificationCmd extends AbstractModificationCmd<Void>
{
    private static final CommandLogger LOG;
    protected boolean writeUserOperationLog;
    
    public ProcessInstanceModificationCmd(final ModificationBuilderImpl builder, final boolean writeUserOperationLog) {
        super(builder);
        this.writeUserOperationLog = writeUserOperationLog;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final List<AbstractProcessInstanceModificationCommand> instructions = this.builder.getInstructions();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Modification instructions cannot be empty", instructions);
        final Collection<String> processInstanceIds = this.collectProcessInstanceIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot be empty", "Process instance ids", processInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "Process instance ids", processInstanceIds);
        final ProcessDefinitionEntity processDefinition = this.getProcessDefinition(commandContext, this.builder.getProcessDefinitionId());
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process definition id cannot be null", processDefinition);
        if (this.writeUserOperationLog) {
            final String annotation = this.builder.getAnnotation();
            this.writeUserOperationLog(commandContext, processDefinition, processInstanceIds.size(), false, annotation);
        }
        final boolean skipCustomListeners = this.builder.isSkipCustomListeners();
        final boolean skipIoMappings = this.builder.isSkipIoMappings();
        for (final String processInstanceId : processInstanceIds) {
            final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(processInstanceId);
            this.ensureProcessInstanceExist(processInstanceId, processInstance);
            this.ensureSameProcessDefinition(processInstance, processDefinition.getId());
            final ProcessInstanceModificationBuilderImpl builder = this.createProcessInstanceModificationBuilder(processInstanceId, commandContext);
            builder.execute(false, skipCustomListeners, skipIoMappings);
        }
        return null;
    }
    
    protected void ensureSameProcessDefinition(final ExecutionEntity processInstance, final String processDefinitionId) {
        if (!processDefinitionId.equals(processInstance.getProcessDefinitionId())) {
            throw ProcessInstanceModificationCmd.LOG.processDefinitionOfInstanceDoesNotMatchModification(processInstance, processDefinitionId);
        }
    }
    
    protected void ensureProcessInstanceExist(final String processInstanceId, final ExecutionEntity processInstance) {
        if (processInstance == null) {
            throw ProcessInstanceModificationCmd.LOG.processInstanceDoesNotExist(processInstanceId);
        }
    }
    
    protected ProcessInstanceModificationBuilderImpl createProcessInstanceModificationBuilder(final String processInstanceId, final CommandContext commandContext) {
        final ProcessInstanceModificationBuilderImpl processInstanceModificationBuilder = new ProcessInstanceModificationBuilderImpl(commandContext, processInstanceId);
        final List<AbstractProcessInstanceModificationCommand> operations = processInstanceModificationBuilder.getModificationOperations();
        ActivityInstance activityInstanceTree = null;
        for (final AbstractProcessInstanceModificationCommand instruction : this.builder.getInstructions()) {
            instruction.setProcessInstanceId(processInstanceId);
            if (!(instruction instanceof ActivityCancellationCmd) || !((ActivityCancellationCmd)instruction).isCancelCurrentActiveActivityInstances()) {
                operations.add(instruction);
            }
            else {
                if (activityInstanceTree == null) {
                    activityInstanceTree = commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(processInstanceId));
                }
                final ActivityCancellationCmd cancellationInstruction = (ActivityCancellationCmd)instruction;
                final List<AbstractInstanceCancellationCmd> cmds = cancellationInstruction.createActivityInstanceCancellations(activityInstanceTree, commandContext);
                operations.addAll(cmds);
            }
        }
        return processInstanceModificationBuilder;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
