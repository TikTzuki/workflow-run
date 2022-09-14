// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.List;

public interface ResourceReport
{
    String getResourceName();
    
    List<Problem> getErrors();
    
    List<Problem> getWarnings();
}
