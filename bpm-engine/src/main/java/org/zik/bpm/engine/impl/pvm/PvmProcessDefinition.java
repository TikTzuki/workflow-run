// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

public interface PvmProcessDefinition extends ReadOnlyProcessDefinition
{
    String getDeploymentId();
    
    PvmProcessInstance createProcessInstance();
    
    PvmProcessInstance createProcessInstance(final String p0);
    
    PvmProcessInstance createProcessInstance(final String p0, final String p1);
}
