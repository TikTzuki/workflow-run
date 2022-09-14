// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.TransferVariablesActivityBehavior;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.SubProcessActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public class PvmAtomicOperationProcessEnd extends PvmAtomicOperationActivityInstanceEnd
{
    private static final PvmLogger LOG;
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getProcessDefinition();
    }
    
    @Override
    protected String getEventName() {
        return "end";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        execution.leaveActivityInstance();
        final PvmExecutionImpl superExecution = execution.getSuperExecution();
        final CmmnActivityExecution superCaseExecution = execution.getSuperCaseExecution();
        SubProcessActivityBehavior subProcessActivityBehavior = null;
        TransferVariablesActivityBehavior transferVariablesBehavior = null;
        if (superExecution != null) {
            final PvmActivity activity = superExecution.getActivity();
            subProcessActivityBehavior = (SubProcessActivityBehavior)activity.getActivityBehavior();
            try {
                subProcessActivityBehavior.passOutputVariables(superExecution, execution);
            }
            catch (RuntimeException e) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e);
                throw e;
            }
            catch (Exception e2) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e2);
                throw new ProcessEngineException("Error while completing sub process of execution " + execution, e2);
            }
        }
        else if (superCaseExecution != null) {
            final CmmnActivity activity2 = superCaseExecution.getActivity();
            transferVariablesBehavior = (TransferVariablesActivityBehavior)activity2.getActivityBehavior();
            try {
                transferVariablesBehavior.transferVariables(execution, superCaseExecution);
            }
            catch (RuntimeException e) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e);
                throw e;
            }
            catch (Exception e2) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e2);
                throw new ProcessEngineException("Error while completing sub process of execution " + execution, e2);
            }
        }
        execution.destroy();
        execution.remove();
        if (superExecution != null) {
            superExecution.setSubProcessInstance(null);
            try {
                subProcessActivityBehavior.completed(superExecution);
                return;
            }
            catch (RuntimeException e3) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e3);
                throw e3;
            }
            catch (Exception e4) {
                PvmAtomicOperationProcessEnd.LOG.exceptionWhileCompletingSupProcess(execution, e4);
                throw new ProcessEngineException("Error while completing sub process of execution " + execution, e4);
            }
        }
        if (superCaseExecution != null) {
            superCaseExecution.complete();
        }
    }
    
    @Override
    public String getCanonicalName() {
        return "process-end";
    }
    
    static {
        LOG = PvmLogger.PVM_LOGGER;
    }
}
