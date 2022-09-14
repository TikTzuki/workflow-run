// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;

public interface PvmAtomicOperation extends CoreAtomicOperation<PvmExecutionImpl>, AtomicOperation
{
    public static final PvmAtomicOperation PROCESS_START = new PvmAtomicOperationProcessStart();
    public static final PvmAtomicOperation FIRE_PROCESS_START = new PvmAtomicOperationFireProcessStart();
    public static final PvmAtomicOperation PROCESS_END = new PvmAtomicOperationProcessEnd();
    public static final PvmAtomicOperation ACTIVITY_START = new PvmAtomicOperationActivityStart();
    public static final PvmAtomicOperation ACTIVITY_START_CONCURRENT = new PvmAtomicOperationActivityStartConcurrent();
    public static final PvmAtomicOperation ACTIVITY_START_CANCEL_SCOPE = new PvmAtomicOperationActivityStartCancelScope();
    public static final PvmAtomicOperation ACTIVITY_START_INTERRUPT_SCOPE = new PvmAtomicOperationActivityStartInterruptEventScope();
    public static final PvmAtomicOperation ACTIVITY_START_CREATE_SCOPE = new PvmAtomicOperationActivityStartCreateScope();
    public static final PvmAtomicOperation ACTIVITY_INIT_STACK_NOTIFY_LISTENER_START = new PvmAtomicOperationActivityInitStackNotifyListenerStart();
    public static final PvmAtomicOperation ACTIVITY_INIT_STACK_NOTIFY_LISTENER_RETURN = new PvmAtomicOperationActivityInitStackNotifyListenerReturn();
    public static final PvmAtomicOperation ACTIVITY_INIT_STACK = new PvmAtomicOperationActivityInitStack(PvmAtomicOperation.ACTIVITY_INIT_STACK_NOTIFY_LISTENER_START);
    public static final PvmAtomicOperation ACTIVITY_INIT_STACK_AND_RETURN = new PvmAtomicOperationActivityInitStack(PvmAtomicOperation.ACTIVITY_INIT_STACK_NOTIFY_LISTENER_RETURN);
    public static final PvmAtomicOperation ACTIVITY_EXECUTE = new PvmAtomicOperationActivityExecute();
    public static final PvmAtomicOperation ACTIVITY_NOTIFY_LISTENER_END = new PvmAtomicOperationActivityNotifyListenerEnd();
    public static final PvmAtomicOperation ACTIVITY_END = new PvmAtomicOperationActivityEnd();
    public static final PvmAtomicOperation FIRE_ACTIVITY_END = new PvmAtomicOperationFireActivityEnd();
    public static final PvmAtomicOperation TRANSITION_NOTIFY_LISTENER_END = new PvmAtomicOperationTransitionNotifyListenerEnd();
    public static final PvmAtomicOperation TRANSITION_DESTROY_SCOPE = new PvmAtomicOperationTransitionDestroyScope();
    public static final PvmAtomicOperation TRANSITION_NOTIFY_LISTENER_TAKE = new PvmAtomicOperationTransitionNotifyListenerTake();
    public static final PvmAtomicOperation TRANSITION_START_NOTIFY_LISTENER_TAKE = new PvmAtomicOperationStartTransitionNotifyListenerTake();
    public static final PvmAtomicOperation TRANSITION_CREATE_SCOPE = new PvmAtomicOperationTransitionCreateScope();
    public static final PvmAtomicOperation TRANSITION_INTERRUPT_FLOW_SCOPE = new PvmAtomicOperationsTransitionInterruptFlowScope();
    public static final PvmAtomicOperation TRANSITION_NOTIFY_LISTENER_START = new PvmAtomicOperationTransitionNotifyListenerStart();
    public static final PvmAtomicOperation DELETE_CASCADE = new PvmAtomicOperationDeleteCascade();
    public static final PvmAtomicOperation DELETE_CASCADE_FIRE_ACTIVITY_END = new PvmAtomicOperationDeleteCascadeFireActivityEnd();
    public static final PvmAtomicOperation ACTIVITY_LEAVE = new PvmAtomicOperationActivityLeave();
}
