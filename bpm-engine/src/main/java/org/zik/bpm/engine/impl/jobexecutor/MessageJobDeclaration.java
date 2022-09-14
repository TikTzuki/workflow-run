// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.interceptor.AtomicOperationInvocation;

public class MessageJobDeclaration extends JobDeclaration<AtomicOperationInvocation, MessageEntity>
{
    public static final String ASYNC_BEFORE = "async-before";
    public static final String ASYNC_AFTER = "async-after";
    private static final long serialVersionUID = 1L;
    protected String[] operationIdentifier;
    
    public MessageJobDeclaration(final String[] operationsIdentifier) {
        super("async-continuation");
        this.operationIdentifier = operationsIdentifier;
    }
    
    @Override
    protected MessageEntity newJobInstance(final AtomicOperationInvocation context) {
        final MessageEntity message = new MessageEntity();
        message.setExecution(context.getExecution());
        return message;
    }
    
    public boolean isApplicableForOperation(final AtomicOperation operation) {
        for (final String identifier : this.operationIdentifier) {
            if (operation.getCanonicalName().equals(identifier)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final AtomicOperationInvocation context) {
        return context.getExecution();
    }
    
    @Override
    protected JobHandlerConfiguration resolveJobHandlerConfiguration(final AtomicOperationInvocation context) {
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration configuration = new AsyncContinuationJobHandler.AsyncContinuationConfiguration();
        configuration.setAtomicOperation(context.getOperation().getCanonicalName());
        final ExecutionEntity execution = context.getExecution();
        final PvmActivity activity = execution.getActivity();
        if (activity != null && activity.isAsyncAfter() && execution.getTransition() != null) {
            configuration.setTransitionId(execution.getTransition().getId());
        }
        return configuration;
    }
}
