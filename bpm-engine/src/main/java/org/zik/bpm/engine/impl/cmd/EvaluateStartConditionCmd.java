// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import org.zik.bpm.engine.impl.runtime.ConditionHandler;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.runtime.ConditionHandlerResult;
import org.zik.bpm.engine.impl.runtime.ConditionSet;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ConditionEvaluationBuilderImpl;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class EvaluateStartConditionCmd implements Command<List<ProcessInstance>>
{
    protected ConditionEvaluationBuilderImpl builder;
    
    public EvaluateStartConditionCmd(final ConditionEvaluationBuilderImpl builder) {
        this.builder = builder;
    }
    
    @Override
    public List<ProcessInstance> execute(final CommandContext commandContext) {
        final ConditionHandler conditionHandler = commandContext.getProcessEngineConfiguration().getConditionHandler();
        final ConditionSet conditionSet = new ConditionSet(this.builder);
        final List<ConditionHandlerResult> results = conditionHandler.evaluateStartCondition(commandContext, conditionSet);
        for (final ConditionHandlerResult ConditionHandlerResult : results) {
            this.checkAuthorization(commandContext, ConditionHandlerResult);
        }
        final List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
        for (final ConditionHandlerResult ConditionHandlerResult2 : results) {
            processInstances.add(this.instantiateProcess(commandContext, ConditionHandlerResult2));
        }
        return processInstances;
    }
    
    protected void checkAuthorization(final CommandContext commandContext, final ConditionHandlerResult result) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            final ProcessDefinitionEntity definition = result.getProcessDefinition();
            checker.checkCreateProcessInstance(definition);
        }
    }
    
    protected ProcessInstance instantiateProcess(final CommandContext commandContext, final ConditionHandlerResult result) {
        final ProcessDefinitionEntity processDefinitionEntity = result.getProcessDefinition();
        final ActivityImpl startEvent = processDefinitionEntity.findActivity(result.getActivity().getActivityId());
        final ExecutionEntity processInstance = processDefinitionEntity.createProcessInstance(this.builder.getBusinessKey(), startEvent);
        processInstance.start((Map<String, Object>)this.builder.getVariables());
        return processInstance;
    }
}
