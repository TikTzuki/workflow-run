// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

public interface ProcessApplicationReference
{
    String getName();
    
    ProcessApplicationInterface getProcessApplication() throws ProcessApplicationUnavailableException;
}
