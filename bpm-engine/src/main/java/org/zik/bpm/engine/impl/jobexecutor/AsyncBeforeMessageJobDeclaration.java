// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;

public class AsyncBeforeMessageJobDeclaration extends MessageJobDeclaration
{
    private static final long serialVersionUID = 1L;
    public static final String[] asyncBeforeOperations;
    
    public AsyncBeforeMessageJobDeclaration() {
        super(AsyncBeforeMessageJobDeclaration.asyncBeforeOperations);
        this.setJobConfiguration("async-before");
    }
    
    static {
        asyncBeforeOperations = new String[] { PvmAtomicOperation.TRANSITION_CREATE_SCOPE.getCanonicalName(), PvmAtomicOperation.PROCESS_START.getCanonicalName(), PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE.getCanonicalName() };
    }
}
