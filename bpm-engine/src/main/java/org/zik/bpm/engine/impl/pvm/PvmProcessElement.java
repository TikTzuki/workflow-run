// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import org.zik.bpm.engine.impl.core.model.Properties;
import java.io.Serializable;

public interface PvmProcessElement extends Serializable
{
    String getId();
    
    PvmProcessDefinition getProcessDefinition();
    
    Object getProperty(final String p0);
    
    Properties getProperties();
}
