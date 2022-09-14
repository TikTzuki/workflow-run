// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.List;

public interface Problem
{
    String getMessage();
    
    int getLine();
    
    int getColumn();
    
    String getMainElementId();
    
    List<String> getElementIds();
}
