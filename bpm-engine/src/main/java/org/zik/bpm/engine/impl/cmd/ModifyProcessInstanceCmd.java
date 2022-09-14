// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.ModificationUtil;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import org.zik.bpm.engine.runtime.ActivityInstance;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ModifyProcessInstanceCmd implements Command<Void>
{
    private static final CommandLogger LOG;
    protected ProcessInstanceModificationBuilderImpl builder;
    protected boolean writeOperationLog;
    
    public ModifyProcessInstanceCmd(final ProcessInstanceModificationBuilderImpl processInstanceModificationBuilder) {
        this(processInstanceModificationBuilder, true);
    }
    
    public ModifyProcessInstanceCmd(final ProcessInstanceModificationBuilderImpl processInstanceModificationBuilder, final boolean writeOperationLog) {
        this.builder = processInstanceModificationBuilder;
        this.writeOperationLog = writeOperationLog;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final String processInstanceId = this.builder.getProcessInstanceId();
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        ExecutionEntity processInstance = executionManager.findExecutionById(processInstanceId);
        this.ensureProcessInstanceExist(processInstanceId, processInstance);
        this.checkUpdateProcessInstance(processInstance, commandContext);
        processInstance.setPreserveScope(true);
        final List<AbstractProcessInstanceModificationCommand> instructions = this.builder.getModificationOperations();
        this.checkCancellation(commandContext);
        for (int i = 0; i < instructions.size(); ++i) {
            final AbstractProcessInstanceModificationCommand instruction = instructions.get(i);
            ModifyProcessInstanceCmd.LOG.debugModificationInstruction(processInstanceId, i + 1, instruction.describe());
            instruction.setSkipCustomListeners(this.builder.isSkipCustomListeners());
            instruction.setSkipIoMappings(this.builder.isSkipIoMappings());
            instruction.setExternallyTerminated(this.builder.isExternallyTerminated());
            instruction.execute(commandContext);
        }
        processInstance = executionManager.findExecutionById(processInstanceId);
        if (!processInstance.hasChildren() && !processInstance.isCanceled() && !processInstance.isRemoved()) {
            if (processInstance.getActivity() == null) {
                this.checkDeleteProcessInstance(processInstance, commandContext);
                this.deletePropagate(processInstance, this.builder.getModificationReason(), this.builder.isSkipCustomListeners(), this.builder.isSkipIoMappings(), this.builder.isExternallyTerminated());
            }
            else if (processInstance.isEnded()) {
                processInstance.propagateEnd();
            }
        }
        if (this.writeOperationLog) {
            commandContext.getOperationLogManager().logProcessInstanceOperation(this.getLogEntryOperation(), processInstanceId, null, null, Collections.singletonList(PropertyChange.EMPTY_CHANGE), this.builder.getAnnotation());
        }
        return null;
    }
    
    private void checkCancellation(final CommandContext commandContext) {
        for (final AbstractProcessInstanceModificationCommand instruction : this.builder.getModificationOperations()) {
            if (instruction instanceof ActivityCancellationCmd && ((ActivityCancellationCmd)instruction).cancelCurrentActiveActivityInstances) {
                final ActivityInstance activityInstanceTree = commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(((ActivityCancellationCmd)instruction).processInstanceId));
                ((ActivityCancellationCmd)instruction).setActivityInstanceTreeToCancel(activityInstanceTree);
            }
        }
    }
    
    protected void ensureProcessInstanceExist(final String processInstanceId, final ExecutionEntity processInstance) {
        if (processInstance == null) {
            throw ModifyProcessInstanceCmd.LOG.processInstanceDoesNotExist(processInstanceId);
        }
    }
    
    protected String getLogEntryOperation() {
        return "ModifyProcessInstance";
    }
    
    protected void checkUpdateProcessInstance(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstance(execution);
        }
    }
    
    protected void checkDeleteProcessInstance(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteProcessInstance(execution);
        }
    }
    
    protected void deletePropagate(final ExecutionEntity processInstance, final String deleteReason, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean externallyTerminated) {
        ExecutionEntity topmostDeletableExecution = processInstance;
        for (ExecutionEntity parentScopeExecution = (ExecutionEntity)topmostDeletableExecution.getParentScopeExecution(true); parentScopeExecution != null && parentScopeExecution.getNonEventScopeExecutions().size() <= 1; parentScopeExecution = (ExecutionEntity)topmostDeletableExecution.getParentScopeExecution(true)) {
            topmostDeletableExecution = parentScopeExecution;
        }
        topmostDeletableExecution.deleteCascade(deleteReason, skipCustomListeners, skipIoMappings, externallyTerminated, false);
        ModificationUtil.handleChildRemovalInScope(topmostDeletableExecution);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
