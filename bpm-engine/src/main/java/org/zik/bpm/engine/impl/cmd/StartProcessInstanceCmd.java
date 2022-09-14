// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionVariableSnapshotObserver;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessInstantiationBuilderImpl;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.zik.bpm.engine.impl.interceptor.Command;

public class StartProcessInstanceCmd implements Command<ProcessInstanceWithVariables>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final ProcessInstantiationBuilderImpl instantiationBuilder;
    
    public StartProcessInstanceCmd(final ProcessInstantiationBuilderImpl instantiationBuilder) {
        this.instantiationBuilder = instantiationBuilder;
    }
    
    @Override
    public ProcessInstanceWithVariables execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = new GetDeployedProcessDefinitionCmd(this.instantiationBuilder, false).execute(commandContext);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateProcessInstance(processDefinition);
        }
        final ExecutionEntity processInstance = processDefinition.createProcessInstance(this.instantiationBuilder.getBusinessKey(), this.instantiationBuilder.getCaseInstanceId());
        if (this.instantiationBuilder.getTenantId() != null) {
            processInstance.setTenantId(this.instantiationBuilder.getTenantId());
        }
        final ExecutionVariableSnapshotObserver variablesListener = new ExecutionVariableSnapshotObserver(processInstance);
        processInstance.start(this.instantiationBuilder.getVariables());
        commandContext.getOperationLogManager().logProcessInstanceOperation("Create", processInstance.getId(), processInstance.getProcessDefinitionId(), processInstance.getProcessDefinition().getKey(), Collections.singletonList(PropertyChange.EMPTY_CHANGE));
        return new ProcessInstanceWithVariablesImpl(processInstance, variablesListener.getVariables());
    }
}
