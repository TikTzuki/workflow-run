// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

public interface PvmTransition extends PvmProcessElement
{
    PvmActivity getSource();
    
    PvmActivity getDestination();
}
