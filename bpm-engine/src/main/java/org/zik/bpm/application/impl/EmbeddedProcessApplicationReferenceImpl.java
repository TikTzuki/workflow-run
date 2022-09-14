// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;

public class EmbeddedProcessApplicationReferenceImpl implements ProcessApplicationReference
{
    protected EmbeddedProcessApplication application;
    
    public EmbeddedProcessApplicationReferenceImpl(final EmbeddedProcessApplication application) {
        this.application = application;
    }
    
    @Override
    public String getName() {
        return this.application.getName();
    }
    
    @Override
    public ProcessApplicationInterface getProcessApplication() throws ProcessApplicationUnavailableException {
        return this.application;
    }
}
