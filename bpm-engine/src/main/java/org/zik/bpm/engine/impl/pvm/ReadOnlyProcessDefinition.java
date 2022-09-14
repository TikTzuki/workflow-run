// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

public interface ReadOnlyProcessDefinition extends PvmScope
{
    String getName();
    
    String getDescription();
    
    PvmActivity getInitial();
    
    String getDiagramResourceName();
}
