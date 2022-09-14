// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.MismatchingMessageCorrelationException;
import org.zik.bpm.engine.impl.runtime.CorrelationHandler;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.runtime.CorrelationHandlerResult;
import java.util.List;
import org.zik.bpm.engine.impl.runtime.CorrelationSet;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.MessageCorrelationBuilderImpl;
import org.zik.bpm.engine.impl.runtime.MessageCorrelationResultImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CorrelateMessageCmd extends AbstractCorrelateMessageCmd implements Command<MessageCorrelationResultImpl>
{
    private static final CommandLogger LOG;
    protected boolean startMessageOnly;
    
    public CorrelateMessageCmd(final MessageCorrelationBuilderImpl messageCorrelationBuilderImpl, final boolean collectVariables, final boolean deserializeVariableValues, final boolean startMessageOnly) {
        super(messageCorrelationBuilderImpl, collectVariables, deserializeVariableValues);
        this.startMessageOnly = startMessageOnly;
    }
    
    @Override
    public MessageCorrelationResultImpl execute(final CommandContext commandContext) {
        EnsureUtil.ensureAtLeastOneNotNull("At least one of the following correlation criteria has to be present: messageName, businessKey, correlationKeys, processInstanceId", this.messageName, this.builder.getBusinessKey(), this.builder.getCorrelationProcessInstanceVariables(), this.builder.getProcessInstanceId());
        final CorrelationHandler correlationHandler = Context.getProcessEngineConfiguration().getCorrelationHandler();
        final CorrelationSet correlationSet = new CorrelationSet(this.builder);
        CorrelationHandlerResult correlationResult = null;
        if (this.startMessageOnly) {
            final List<CorrelationHandlerResult> correlationResults = commandContext.runWithoutAuthorization((Callable<List<CorrelationHandlerResult>>)new Callable<List<CorrelationHandlerResult>>() {
                @Override
                public List<CorrelationHandlerResult> call() throws Exception {
                    return correlationHandler.correlateStartMessages(commandContext, CorrelateMessageCmd.this.messageName, correlationSet);
                }
            });
            if (correlationResults.isEmpty()) {
                throw new MismatchingMessageCorrelationException(this.messageName, "No process definition matches the parameters");
            }
            if (correlationResults.size() > 1) {
                throw CorrelateMessageCmd.LOG.exceptionCorrelateMessageToSingleProcessDefinition(this.messageName, correlationResults.size(), correlationSet);
            }
            correlationResult = correlationResults.get(0);
        }
        else {
            correlationResult = commandContext.runWithoutAuthorization((Callable<CorrelationHandlerResult>)new Callable<CorrelationHandlerResult>() {
                @Override
                public CorrelationHandlerResult call() throws Exception {
                    return correlationHandler.correlateMessage(commandContext, CorrelateMessageCmd.this.messageName, correlationSet);
                }
            });
            if (correlationResult == null) {
                throw new MismatchingMessageCorrelationException(this.messageName, "No process definition or execution matches the parameters");
            }
        }
        this.checkAuthorization(correlationResult);
        return this.createMessageCorrelationResult(commandContext, correlationResult);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
