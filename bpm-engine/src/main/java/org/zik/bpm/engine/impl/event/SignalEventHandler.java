// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class SignalEventHandler extends EventHandlerImpl
{
    private static final CommandLogger LOG;
    
    public SignalEventHandler() {
        super(EventType.SIGNAL);
    }
    
    protected void handleStartEvent(final EventSubscriptionEntity eventSubscription, final Map<String, Object> payload, final String businessKey, final CommandContext commandContext) {
        final String processDefinitionId = eventSubscription.getConfiguration();
        EnsureUtil.ensureNotNull("Configuration of signal start event subscription '" + eventSubscription.getId() + "' contains no process definition id.", (Object)processDefinitionId);
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
        if (processDefinition == null || processDefinition.isSuspended()) {
            SignalEventHandler.LOG.debugIgnoringEventSubscription(eventSubscription, processDefinitionId);
        }
        else {
            final ActivityImpl signalStartEvent = processDefinition.findActivity(eventSubscription.getActivityId());
            final PvmProcessInstance processInstance = processDefinition.createProcessInstance(businessKey, signalStartEvent);
            processInstance.start(payload);
        }
    }
    
    @Override
    public void handleEvent(final EventSubscriptionEntity eventSubscription, final Object payload, final Object payloadLocal, final String businessKey, final CommandContext commandContext) {
        if (eventSubscription.getExecutionId() != null) {
            this.handleIntermediateEvent(eventSubscription, payload, payloadLocal, commandContext);
        }
        else {
            this.handleStartEvent(eventSubscription, (Map<String, Object>)payload, businessKey, commandContext);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
