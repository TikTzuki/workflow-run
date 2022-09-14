// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnCompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.SubProcessActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.cmmn.behavior.TransferVariablesActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public abstract class AbstractAtomicOperationCaseExecutionComplete extends AbstractCmmnEventAtomicOperation
{
    protected static final CmmnOperationLogger LOG;
    
    @Override
    protected String getEventName() {
        return "complete";
    }
    
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        this.triggerBehavior(behavior, execution);
        execution.setCurrentState(CaseExecutionState.COMPLETED);
        return execution;
    }
    
    @Override
    protected void postTransitionNotification(final CmmnExecution execution) {
        if (!execution.isCaseInstanceExecution()) {
            execution.remove();
        }
        else {
            final CmmnExecution superCaseExecution = execution.getSuperCaseExecution();
            final PvmExecutionImpl superExecution = execution.getSuperExecution();
            if (superCaseExecution != null) {
                final TransferVariablesActivityBehavior behavior = (TransferVariablesActivityBehavior)ActivityBehaviorUtil.getActivityBehavior(superCaseExecution);
                behavior.transferVariables(execution, superCaseExecution);
                superCaseExecution.complete();
            }
            else if (superExecution != null) {
                final SubProcessActivityBehavior behavior2 = (SubProcessActivityBehavior)ActivityBehaviorUtil.getActivityBehavior(superExecution);
                try {
                    behavior2.passOutputVariables(superExecution, execution);
                }
                catch (RuntimeException e) {
                    AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseError(execution, e);
                    throw e;
                }
                catch (Exception e2) {
                    AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseError(execution, e2);
                    throw AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseErrorException(execution, e2);
                }
                superExecution.setSubCaseInstance(null);
                try {
                    behavior2.completed(superExecution);
                }
                catch (RuntimeException e) {
                    AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseError(execution, e);
                    throw e;
                }
                catch (Exception e2) {
                    AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseError(execution, e2);
                    throw AbstractAtomicOperationCaseExecutionComplete.LOG.completingSubCaseErrorException(execution, e2);
                }
            }
            execution.setSuperCaseExecution(null);
            execution.setSuperExecution(null);
        }
        final CmmnExecution parent = execution.getParent();
        if (parent != null) {
            final CmmnActivityBehavior behavior3 = ActivityBehaviorUtil.getActivityBehavior(parent);
            if (behavior3 instanceof CmmnCompositeActivityBehavior) {
                final CmmnCompositeActivityBehavior compositeBehavior = (CmmnCompositeActivityBehavior)behavior3;
                compositeBehavior.handleChildCompletion(parent, execution);
            }
        }
    }
    
    protected abstract void triggerBehavior(final CmmnActivityBehavior p0, final CmmnExecution p1);
    
    static {
        LOG = ProcessEngineLogger.CMMN_OPERATION_LOGGER;
    }
}
