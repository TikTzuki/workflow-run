// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public class OutgoingExecution
{
    private static final PvmLogger LOG;
    protected PvmExecutionImpl outgoingExecution;
    protected PvmTransition outgoingTransition;
    
    public OutgoingExecution(final PvmExecutionImpl outgoingExecution, final PvmTransition outgoingTransition) {
        (this.outgoingExecution = outgoingExecution).setTransition(this.outgoingTransition = outgoingTransition);
        outgoingExecution.setActivityInstanceId(null);
    }
    
    public void take() {
        if (this.outgoingExecution.getReplacedBy() != null) {
            this.outgoingExecution = this.outgoingExecution.getReplacedBy();
        }
        if (!this.outgoingExecution.isEnded()) {
            this.outgoingExecution.take();
        }
        else {
            OutgoingExecution.LOG.notTakingTranistion(this.outgoingTransition);
        }
    }
    
    public PvmExecutionImpl getOutgoingExecution() {
        return this.outgoingExecution;
    }
    
    static {
        LOG = PvmLogger.PVM_LOGGER;
    }
}
