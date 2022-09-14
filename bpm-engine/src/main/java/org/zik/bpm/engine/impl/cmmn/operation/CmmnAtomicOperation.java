// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;

public interface CmmnAtomicOperation extends CoreAtomicOperation<CmmnExecution>
{
    public static final CmmnAtomicOperation CASE_INSTANCE_CREATE = new AtomicOperationCaseInstanceCreate();
    public static final CmmnAtomicOperation CASE_INSTANCE_CLOSE = new AtomicOperationCaseInstanceClose();
    public static final CmmnAtomicOperation CASE_EXECUTION_CREATE = new AtomicOperationCaseExecutionCreate();
    public static final CmmnAtomicOperation CASE_EXECUTION_CREATED = new AtomicOperationCaseExecutionCreated();
    public static final CmmnAtomicOperation CASE_EXECUTION_ENABLE = new AtomicOperationCaseExecutionEnable();
    public static final CmmnAtomicOperation CASE_EXECUTION_RE_ENABLE = new AtomicOperationCaseExecutionReenable();
    public static final CmmnAtomicOperation CASE_EXECUTION_DISABLE = new AtomicOperationCaseExecutionDisable();
    public static final CmmnAtomicOperation CASE_EXECUTION_START = new AtomicOperationCaseExecutionStart();
    public static final CmmnAtomicOperation CASE_EXECUTION_MANUAL_START = new AtomicOperationCaseExecutionManualStart();
    public static final CmmnAtomicOperation CASE_EXECUTION_COMPLETE = new AtomicOperationCaseExecutionComplete();
    public static final CmmnAtomicOperation CASE_EXECUTION_MANUAL_COMPLETE = new AtomicOperationCaseExecutionManualComplete();
    public static final CmmnAtomicOperation CASE_EXECUTION_OCCUR = new AtomicOperationCaseExecutionOccur();
    public static final CmmnAtomicOperation CASE_EXECUTION_TERMINATE = new AtomicOperationCaseExecutionTerminate();
    public static final CmmnAtomicOperation CASE_EXECUTION_PARENT_TERMINATE = new AtomicOperationCaseExecutionParentTerminate();
    public static final CmmnAtomicOperation CASE_EXECUTION_EXIT = new AtomicOperationCaseExecutionExit();
    public static final CmmnAtomicOperation CASE_EXECUTION_SUSPEND = new AtomicOperationCaseExecutionSuspend();
    public static final CmmnAtomicOperation CASE_EXECUTION_PARENT_SUSPEND = new AtomicOperationCaseExecutionParentSuspend();
    public static final CmmnAtomicOperation CASE_EXECUTION_RESUME = new AtomicOperationCaseExecutionResume();
    public static final CmmnAtomicOperation CASE_EXECUTION_PARENT_RESUME = new AtomicOperationCaseExecutionParentResume();
    public static final CmmnAtomicOperation CASE_EXECUTION_RE_ACTIVATE = new AtomicOperationCaseExecutionReactivate();
    public static final CmmnAtomicOperation CASE_EXECUTION_TERMINATING_ON_TERMINATION = new AtomicOperationCaseExecutionTerminatingOnTermination();
    public static final CmmnAtomicOperation CASE_EXECUTION_TERMINATING_ON_PARENT_TERMINATION = new AtomicOperationCaseExecutionTerminatingOnParentTermination();
    public static final CmmnAtomicOperation CASE_EXECUTION_TERMINATING_ON_EXIT = new AtomicOperationCaseExecutionTerminatingOnExit();
    public static final CmmnAtomicOperation CASE_EXECUTION_PARENT_COMPLETE = new AtomicOperationCaseExecutionParentComplete();
    public static final CmmnAtomicOperation CASE_EXECUTION_SUSPENDING_ON_SUSPENSION = new AtomicOperationCaseExecutionSuspendingOnSuspension();
    public static final CmmnAtomicOperation CASE_EXECUTION_SUSPENDING_ON_PARENT_SUSPENSION = new AtomicOperationCaseExecutionSuspendingOnParentSuspension();
    public static final CmmnAtomicOperation CASE_EXECUTION_FIRE_ENTRY_CRITERIA = new AtomicOperationCaseExecutionFireEntryCriteria();
    public static final CmmnAtomicOperation CASE_EXECUTION_FIRE_EXIT_CRITERIA = new AtomicOperationCaseExecutionFireExitCriteria();
    public static final CmmnAtomicOperation CASE_EXECUTION_DELETE_CASCADE = new AtomicOperationCaseExecutionDeleteCascade();
    
    void execute(final CmmnExecution p0);
    
    boolean isAsync(final CmmnExecution p0);
}
