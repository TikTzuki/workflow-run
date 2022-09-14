// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.List;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionVariableSnapshotObserver;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessInstantiationBuilderImpl;
import org.zik.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.zik.bpm.engine.impl.interceptor.Command;

public class StartProcessInstanceAtActivitiesCmd implements Command<ProcessInstanceWithVariables>
{
    private static final CommandLogger LOG;
    protected ProcessInstantiationBuilderImpl instantiationBuilder;
    
    public StartProcessInstanceAtActivitiesCmd(final ProcessInstantiationBuilderImpl instantiationBuilder) {
        this.instantiationBuilder = instantiationBuilder;
    }
    
    @Override
    public ProcessInstanceWithVariables execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = new GetDeployedProcessDefinitionCmd(this.instantiationBuilder, false).execute(commandContext);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateProcessInstance(processDefinition);
        }
        final ProcessInstanceModificationBuilderImpl modificationBuilder = this.instantiationBuilder.getModificationBuilder();
        EnsureUtil.ensureNotEmpty("At least one instantiation instruction required (e.g. by invoking startBefore(..), startAfter(..) or startTransition(..))", "instructions", modificationBuilder.getModificationOperations());
        final ActivityImpl initialActivity = this.determineFirstActivity(processDefinition, modificationBuilder);
        final ExecutionEntity processInstance = processDefinition.createProcessInstance(this.instantiationBuilder.getBusinessKey(), this.instantiationBuilder.getCaseInstanceId(), initialActivity);
        if (this.instantiationBuilder.getTenantId() != null) {
            processInstance.setTenantId(this.instantiationBuilder.getTenantId());
        }
        processInstance.setSkipCustomListeners(modificationBuilder.isSkipCustomListeners());
        final VariableMap variables = modificationBuilder.getProcessVariables();
        final ExecutionVariableSnapshotObserver variablesListener = new ExecutionVariableSnapshotObserver(processInstance);
        processInstance.startWithoutExecuting((Map<String, Object>)variables);
        processInstance.setPreserveScope(true);
        final List<AbstractProcessInstanceModificationCommand> instructions = modificationBuilder.getModificationOperations();
        processInstance.setStarting(instructions.size() == 1);
        for (int i = 0; i < instructions.size(); ++i) {
            final AbstractProcessInstanceModificationCommand instruction = instructions.get(i);
            StartProcessInstanceAtActivitiesCmd.LOG.debugStartingInstruction(processInstance.getId(), i, instruction.describe());
            instruction.setProcessInstanceId(processInstance.getId());
            instruction.setSkipCustomListeners(modificationBuilder.isSkipCustomListeners());
            instruction.setSkipIoMappings(modificationBuilder.isSkipIoMappings());
            instruction.execute(commandContext);
        }
        if (!processInstance.hasChildren() && processInstance.isEnded()) {
            processInstance.propagateEnd();
        }
        commandContext.getOperationLogManager().logProcessInstanceOperation("Create", processInstance.getId(), processInstance.getProcessDefinitionId(), processInstance.getProcessDefinition().getKey(), Collections.singletonList(PropertyChange.EMPTY_CHANGE), modificationBuilder.getAnnotation());
        return new ProcessInstanceWithVariablesImpl(processInstance, variablesListener.getVariables());
    }
    
    protected ActivityImpl determineFirstActivity(final ProcessDefinitionImpl processDefinition, final ProcessInstanceModificationBuilderImpl modificationBuilder) {
        final AbstractProcessInstanceModificationCommand firstInstruction = modificationBuilder.getModificationOperations().get(0);
        if (firstInstruction instanceof AbstractInstantiationCmd) {
            final AbstractInstantiationCmd instantiationInstruction = (AbstractInstantiationCmd)firstInstruction;
            final CoreModelElement targetElement = instantiationInstruction.getTargetElement(processDefinition);
            EnsureUtil.ensureNotNull(NotValidException.class, "Element '" + instantiationInstruction.getTargetElementId() + "' does not exist in process " + processDefinition.getId(), "targetElement", targetElement);
            if (targetElement instanceof ActivityImpl) {
                return (ActivityImpl)targetElement;
            }
            if (targetElement instanceof TransitionImpl) {
                return (ActivityImpl)((TransitionImpl)targetElement).getDestination();
            }
        }
        return null;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
