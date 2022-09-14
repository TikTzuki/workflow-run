// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import java.util.Map;

public class AsyncContinuationJobHandler implements JobHandler<AsyncContinuationJobHandler.AsyncContinuationConfiguration>
{
    public static final String TYPE = "async-continuation";
    private Map<String, PvmAtomicOperation> supportedOperations;

    public AsyncContinuationJobHandler() {
        (this.supportedOperations = new HashMap<String, PvmAtomicOperation>()).put(PvmAtomicOperation.TRANSITION_CREATE_SCOPE.getCanonicalName(), PvmAtomicOperation.TRANSITION_CREATE_SCOPE);
        this.supportedOperations.put(PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE.getCanonicalName(), PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE);
        this.supportedOperations.put(PvmAtomicOperation.PROCESS_START.getCanonicalName(), PvmAtomicOperation.PROCESS_START);
        this.supportedOperations.put(PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE.getCanonicalName(), PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE);
        this.supportedOperations.put(PvmAtomicOperation.ACTIVITY_END.getCanonicalName(), PvmAtomicOperation.ACTIVITY_END);
    }

    @Override
    public String getType() {
        return "async-continuation";
    }

    @Override
    public void execute(final AsyncContinuationConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        LegacyBehavior.repairMultiInstanceAsyncJob(execution);
        final PvmAtomicOperation atomicOperation = this.findMatchingAtomicOperation(configuration.getAtomicOperation());
        EnsureUtil.ensureNotNull("Cannot process job with configuration " + configuration, "atomicOperation", atomicOperation);
        final String transitionId = configuration.getTransitionId();
        if (transitionId != null) {
            final PvmActivity activity = execution.getActivity();
            final TransitionImpl transition = (TransitionImpl)activity.findOutgoingTransition(transitionId);
            execution.setTransition(transition);
        }
        Context.getCommandInvocationContext().performOperation(atomicOperation, execution);
    }

    public PvmAtomicOperation findMatchingAtomicOperation(final String operationName) {
        if (operationName == null) {
            return PvmAtomicOperation.TRANSITION_CREATE_SCOPE;
        }
        return this.supportedOperations.get(operationName);
    }

    protected boolean isSupported(final PvmAtomicOperation atomicOperation) {
        return this.supportedOperations.containsKey(atomicOperation.getCanonicalName());
    }

    @Override
    public AsyncContinuationConfiguration newConfiguration(final String canonicalString) {
        final String[] configParts = this.tokenizeJobConfiguration(canonicalString);
        final AsyncContinuationConfiguration configuration = new AsyncContinuationConfiguration();
        configuration.setAtomicOperation(configParts[0]);
        configuration.setTransitionId(configParts[1]);
        return configuration;
    }

    protected String[] tokenizeJobConfiguration(final String jobConfiguration) {
        final String[] configuration = new String[2];
        if (jobConfiguration != null) {
            final String[] configParts = jobConfiguration.split("\\$");
            if (configuration.length > 2) {
                throw new ProcessEngineException("Illegal async continuation job handler configuration: '" + jobConfiguration + "': exprecting one part or two parts seperated by '$'.");
            }
            configuration[0] = configParts[0];
            if (configParts.length == 2) {
                configuration[1] = configParts[1];
            }
        }
        return configuration;
    }

    @Override
    public void onDelete(final AsyncContinuationConfiguration configuration, final JobEntity jobEntity) {
    }

    public static class AsyncContinuationConfiguration implements JobHandlerConfiguration
    {
        protected String atomicOperation;
        protected String transitionId;

        public String getAtomicOperation() {
            return this.atomicOperation;
        }

        public void setAtomicOperation(final String atomicOperation) {
            this.atomicOperation = atomicOperation;
        }

        public String getTransitionId() {
            return this.transitionId;
        }

        public void setTransitionId(final String transitionId) {
            this.transitionId = transitionId;
        }

        @Override
        public String toCanonicalString() {
            String configuration = this.atomicOperation;
            if (this.transitionId != null) {
                configuration = configuration + "$" + this.transitionId;
            }
            return configuration;
        }
    }
}
