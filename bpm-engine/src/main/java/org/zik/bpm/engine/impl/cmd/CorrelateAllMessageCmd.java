// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.runtime.CorrelationHandler;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.runtime.CorrelationHandlerResult;
import org.zik.bpm.engine.impl.runtime.CorrelationSet;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.MessageCorrelationBuilderImpl;
import org.zik.bpm.engine.impl.runtime.MessageCorrelationResultImpl;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CorrelateAllMessageCmd extends AbstractCorrelateMessageCmd implements Command<List<MessageCorrelationResultImpl>>
{
    public CorrelateAllMessageCmd(final MessageCorrelationBuilderImpl messageCorrelationBuilderImpl, final boolean collectVariables, final boolean deserializeVariableValues) {
        super(messageCorrelationBuilderImpl, collectVariables, deserializeVariableValues);
    }
    
    @Override
    public List<MessageCorrelationResultImpl> execute(final CommandContext commandContext) {
        EnsureUtil.ensureAtLeastOneNotNull("At least one of the following correlation criteria has to be present: messageName, businessKey, correlationKeys, processInstanceId", this.messageName, this.builder.getBusinessKey(), this.builder.getCorrelationProcessInstanceVariables(), this.builder.getProcessInstanceId());
        final CorrelationHandler correlationHandler = Context.getProcessEngineConfiguration().getCorrelationHandler();
        final CorrelationSet correlationSet = new CorrelationSet(this.builder);
        final List<CorrelationHandlerResult> correlationResults = commandContext.runWithoutAuthorization((Callable<List<CorrelationHandlerResult>>)new Callable<List<CorrelationHandlerResult>>() {
            @Override
            public List<CorrelationHandlerResult> call() throws Exception {
                return correlationHandler.correlateMessages(commandContext, CorrelateAllMessageCmd.this.messageName, correlationSet);
            }
        });
        for (final CorrelationHandlerResult correlationResult : correlationResults) {
            this.checkAuthorization(correlationResult);
        }
        final List<MessageCorrelationResultImpl> results = new ArrayList<MessageCorrelationResultImpl>();
        for (final CorrelationHandlerResult correlationResult2 : correlationResults) {
            results.add(this.createMessageCorrelationResult(commandContext, correlationResult2));
        }
        return results;
    }
}
