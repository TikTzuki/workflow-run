// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;

public class AtomicOperationInvocation
{
    private static final ContextLogger LOG;
    protected AtomicOperation operation;
    protected ExecutionEntity execution;
    protected boolean performAsync;
    protected String applicationContextName;
    protected String activityId;
    protected String activityName;
    
    public AtomicOperationInvocation(final AtomicOperation operation, final ExecutionEntity execution, final boolean performAsync) {
        this.applicationContextName = null;
        this.activityId = null;
        this.activityName = null;
        this.init(operation, execution, performAsync);
    }
    
    protected void init(final AtomicOperation operation, final ExecutionEntity execution, final boolean performAsync) {
        this.operation = operation;
        this.execution = execution;
        this.performAsync = performAsync;
    }
    
    public void execute(final BpmnStackTrace stackTrace, final ProcessDataContext processDataContext) {
        if (this.operation != PvmAtomicOperation.ACTIVITY_START_CANCEL_SCOPE && this.operation != PvmAtomicOperation.ACTIVITY_START_INTERRUPT_SCOPE && this.operation != PvmAtomicOperation.ACTIVITY_START_CONCURRENT && this.operation != PvmAtomicOperation.DELETE_CASCADE) {
            final ExecutionEntity replacedBy = this.execution.getReplacedBy();
            if (replacedBy != null) {
                this.execution = replacedBy;
            }
        }
        if (this.execution.isCanceled() && (this.operation == PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_END || this.operation == PvmAtomicOperation.ACTIVITY_NOTIFY_LISTENER_END)) {
            return;
        }
        if (this.execution.isEnded() && (this.operation == PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE || this.operation == PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE)) {
            return;
        }
        final ProcessApplicationReference currentPa = Context.getCurrentProcessApplication();
        if (currentPa != null) {
            this.applicationContextName = currentPa.getName();
        }
        this.activityId = this.execution.getActivityId();
        this.activityName = this.execution.getCurrentActivityName();
        stackTrace.add(this);
        final boolean popProcessDataContextSection = processDataContext.pushSection(this.execution);
        try {
            Context.setExecutionContext(this.execution);
            if (!this.performAsync) {
                AtomicOperationInvocation.LOG.debugExecutingAtomicOperation(this.operation, this.execution);
                (this.operation).execute(this.execution);
            }
            else {
                this.execution.scheduleAtomicOperationAsync(this);
            }
            if (popProcessDataContextSection) {
                processDataContext.popSection();
            }
        }
        finally {
            Context.removeExecutionContext();
        }
    }
    
    public AtomicOperation getOperation() {
        return this.operation;
    }
    
    public ExecutionEntity getExecution() {
        return this.execution;
    }
    
    public boolean isPerformAsync() {
        return this.performAsync;
    }
    
    public String getApplicationContextName() {
        return this.applicationContextName;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getActivityName() {
        return this.activityName;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTEXT_LOGGER;
    }
}
