// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;

public class ProcessApplicationIdentifier
{
    protected String name;
    protected ProcessApplicationReference reference;
    protected ProcessApplicationInterface processApplication;
    
    public ProcessApplicationIdentifier(final String name) {
        this.name = name;
    }
    
    public ProcessApplicationIdentifier(final ProcessApplicationReference reference) {
        this.reference = reference;
    }
    
    public ProcessApplicationIdentifier(final ProcessApplicationInterface processApplication) {
        this.processApplication = processApplication;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ProcessApplicationReference getReference() {
        return this.reference;
    }
    
    public ProcessApplicationInterface getProcessApplication() {
        return this.processApplication;
    }
    
    @Override
    public String toString() {
        String paName = this.name;
        if (paName == null && this.reference != null) {
            paName = this.reference.getName();
        }
        if (paName == null && this.processApplication != null) {
            paName = this.processApplication.getName();
        }
        return "ProcessApplicationIdentifier[name=" + paName + "]";
    }
}
