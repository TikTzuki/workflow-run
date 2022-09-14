// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;

public class AsyncAfterMessageJobDeclaration extends MessageJobDeclaration
{
    public static final String[] asyncAfterOperations;
    
    public AsyncAfterMessageJobDeclaration() {
        super(AsyncAfterMessageJobDeclaration.asyncAfterOperations);
        this.setJobConfiguration("async-after");
    }
    
    static {
        asyncAfterOperations = new String[] { PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE.getCanonicalName(), PvmAtomicOperation.ACTIVITY_END.getCanonicalName() };
    }
}
