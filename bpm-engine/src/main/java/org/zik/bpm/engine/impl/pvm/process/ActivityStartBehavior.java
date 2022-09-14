// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

public enum ActivityStartBehavior
{
    DEFAULT, 
    INTERRUPT_FLOW_SCOPE, 
    CONCURRENT_IN_FLOW_SCOPE, 
    INTERRUPT_EVENT_SCOPE, 
    CANCEL_EVENT_SCOPE;
}
