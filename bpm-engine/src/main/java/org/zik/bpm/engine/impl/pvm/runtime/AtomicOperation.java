// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;

@Deprecated
public interface AtomicOperation extends CoreAtomicOperation<PvmExecutionImpl>
{
    public static final AtomicOperation PROCESS_START = PvmAtomicOperation.PROCESS_START;
    public static final AtomicOperation PROCESS_START_INITIAL = PvmAtomicOperation.PROCESS_START_INITIAL;
    public static final AtomicOperation PROCESS_END = PvmAtomicOperation.PROCESS_END;
    public static final AtomicOperation ACTIVITY_START = PvmAtomicOperation.ACTIVITY_START;
    public static final AtomicOperation ACTIVITY_START_CONCURRENT = PvmAtomicOperation.ACTIVITY_START_CONCURRENT;
    public static final AtomicOperation ACTIVITY_START_CANCEL_SCOPE = PvmAtomicOperation.ACTIVITY_START_CANCEL_SCOPE;
    public static final AtomicOperation ACTIVITY_EXECUTE = PvmAtomicOperation.ACTIVITY_EXECUTE;
    public static final AtomicOperation ACTIVITY_END = PvmAtomicOperation.ACTIVITY_END;
    public static final AtomicOperation FIRE_ACTIVITY_END = PvmAtomicOperation.FIRE_ACTIVITY_END;
    public static final AtomicOperation TRANSITION_NOTIFY_LISTENER_END = PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_END;
    public static final AtomicOperation TRANSITION_DESTROY_SCOPE = PvmAtomicOperation.TRANSITION_DESTROY_SCOPE;
    public static final AtomicOperation TRANSITION_NOTIFY_LISTENER_TAKE = PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE;
    public static final AtomicOperation TRANSITION_CREATE_SCOPE = PvmAtomicOperation.TRANSITION_CREATE_SCOPE;
    public static final AtomicOperation TRANSITION_NOTIFY_LISTENER_START = PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_START;
    public static final AtomicOperation DELETE_CASCADE = PvmAtomicOperation.DELETE_CASCADE;
    public static final AtomicOperation DELETE_CASCADE_FIRE_ACTIVITY_END = PvmAtomicOperation.DELETE_CASCADE_FIRE_ACTIVITY_END;
    
    boolean isAsyncCapable();
}
