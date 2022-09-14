// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import javax.ejb.EJBException;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;

public class EjbProcessApplicationReference implements ProcessApplicationReference
{
    private static ProcessApplicationLogger LOG;
    protected ProcessApplicationInterface selfReference;
    protected String processApplicationName;
    
    public EjbProcessApplicationReference(final ProcessApplicationInterface selfReference, final String name) {
        this.selfReference = selfReference;
        this.processApplicationName = name;
    }
    
    @Override
    public String getName() {
        return this.processApplicationName;
    }
    
    @Override
    public ProcessApplicationInterface getProcessApplication() throws ProcessApplicationUnavailableException {
        try {
            this.selfReference.getName();
        }
        catch (EJBException e) {
            throw EjbProcessApplicationReference.LOG.processApplicationUnavailableException(this.processApplicationName, (Throwable)e);
        }
        return this.selfReference;
    }
    
    public void processEngineStopping(final ProcessEngine processEngine) throws ProcessApplicationUnavailableException {
    }
    
    static {
        EjbProcessApplicationReference.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
