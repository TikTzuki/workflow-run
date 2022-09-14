// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface CaseInstance extends CaseExecution
{
    String getBusinessKey();
    
    boolean isCompleted();
}
