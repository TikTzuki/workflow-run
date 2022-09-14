// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.core.model.CallableElement;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.AuthorizationException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.impl.util.CallableElementUtil;
import org.zik.bpm.engine.impl.repository.CalledProcessDefinitionImpl;
import java.util.HashMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Queue;
import org.zik.bpm.engine.impl.bpmn.behavior.CallActivityBehavior;
import java.util.LinkedList;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.CalledProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetStaticCalledProcessDefinitionCmd implements Command<Collection<CalledProcessDefinition>>
{
    protected String processDefinitionId;
    
    public GetStaticCalledProcessDefinitionCmd(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    protected List<ActivityImpl> findCallActivitiesInProcess(final ProcessDefinitionEntity processDefinition) {
        final List<ActivityImpl> callActivities = new ArrayList<ActivityImpl>();
        final Queue<ActivityImpl> toCheck = new LinkedList<ActivityImpl>(processDefinition.getActivities());
        while (!toCheck.isEmpty()) {
            final ActivityImpl candidate = toCheck.poll();
            if (!candidate.getActivities().isEmpty()) {
                toCheck.addAll((Collection<?>)candidate.getActivities());
            }
            if (candidate.getActivityBehavior() instanceof CallActivityBehavior) {
                callActivities.add(candidate);
            }
        }
        return callActivities;
    }
    
    @Override
    public Collection<CalledProcessDefinition> execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = new GetDeployedProcessDefinitionCmd(this.processDefinitionId, true).execute(commandContext);
        final List<ActivityImpl> callActivities = this.findCallActivitiesInProcess(processDefinition);
        final Map<String, CalledProcessDefinitionImpl> calledProcessDefinitionsById = new HashMap<String, CalledProcessDefinitionImpl>();
        for (final ActivityImpl activity : callActivities) {
            final CallActivityBehavior behavior = (CallActivityBehavior)activity.getActivityBehavior();
            final CallableElement callableElement = behavior.getCallableElement();
            final String activityId = activity.getActivityId();
            final String tenantId = processDefinition.getTenantId();
            final ProcessDefinition calledProcess = CallableElementUtil.getStaticallyBoundProcessDefinition(this.processDefinitionId, activityId, callableElement, tenantId);
            if (calledProcess != null) {
                if (!calledProcessDefinitionsById.containsKey(calledProcess.getId())) {
                    try {
                        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                            checker.checkReadProcessDefinition(calledProcess);
                        }
                        final CalledProcessDefinitionImpl result = new CalledProcessDefinitionImpl(calledProcess, this.processDefinitionId);
                        result.addCallingCallActivity(activityId);
                        calledProcessDefinitionsById.put(calledProcess.getId(), result);
                    }
                    catch (AuthorizationException e) {
                        ProcessEngineLogger.CMD_LOGGER.debugNotAllowedToResolveCalledProcess(calledProcess.getId(), this.processDefinitionId, activityId, e);
                    }
                }
                else {
                    calledProcessDefinitionsById.get(calledProcess.getId()).addCallingCallActivity(activityId);
                }
            }
        }
        return new ArrayList<CalledProcessDefinition>(calledProcessDefinitionsById.values());
    }
}
