// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class MessageEventReceivedCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String executionId;
    protected final Map<String, Object> processVariables;
    protected final Map<String, Object> processVariablesLocal;
    protected final String messageName;
    protected boolean exclusive;
    
    public MessageEventReceivedCmd(final String messageName, final String executionId, final Map<String, Object> processVariables) {
        this(messageName, executionId, processVariables, null);
    }
    
    public MessageEventReceivedCmd(final String messageName, final String executionId, final Map<String, Object> processVariables, final Map<String, Object> processVariablesLocal) {
        this.exclusive = false;
        this.executionId = executionId;
        this.messageName = messageName;
        this.processVariables = processVariables;
        this.processVariablesLocal = processVariablesLocal;
    }
    
    public MessageEventReceivedCmd(final String messageName, final String executionId, final Map<String, Object> processVariables, final Map<String, Object> processVariablesLocal, final boolean exclusive) {
        this(messageName, executionId, processVariables, processVariablesLocal);
        this.exclusive = exclusive;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("executionId", (Object)this.executionId);
        final EventSubscriptionManager eventSubscriptionManager = commandContext.getEventSubscriptionManager();
        List<EventSubscriptionEntity> eventSubscriptions = null;
        if (this.messageName != null) {
            eventSubscriptions = eventSubscriptionManager.findEventSubscriptionsByNameAndExecution(EventType.MESSAGE.name(), this.messageName, this.executionId, this.exclusive);
        }
        else {
            eventSubscriptions = eventSubscriptionManager.findEventSubscriptionsByExecutionAndType(this.executionId, EventType.MESSAGE.name(), this.exclusive);
        }
        EnsureUtil.ensureNotEmpty("Execution with id '" + this.executionId + "' does not have a subscription to a message event with name '" + this.messageName + "'", "eventSubscriptions", eventSubscriptions);
        EnsureUtil.ensureNumberOfElements("More than one matching message subscription found for execution " + this.executionId, "eventSubscriptions", eventSubscriptions, 1);
        final EventSubscriptionEntity eventSubscriptionEntity = eventSubscriptions.get(0);
        final String processInstanceId = eventSubscriptionEntity.getProcessInstanceId();
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstanceById(processInstanceId);
        }
        eventSubscriptionEntity.eventReceived(this.processVariables, this.processVariablesLocal, null, false);
        return null;
    }
}
