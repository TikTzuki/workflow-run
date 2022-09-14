// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.application.AbstractProcessApplication;
import java.lang.ref.WeakReference;
import org.zik.bpm.application.ProcessApplicationReference;

public class ProcessApplicationReferenceImpl implements ProcessApplicationReference
{
    private static ProcessApplicationLogger LOG;
    protected WeakReference<AbstractProcessApplication> processApplication;
    protected String name;
    
    public ProcessApplicationReferenceImpl(final AbstractProcessApplication processApplication) {
        this.processApplication = new WeakReference<AbstractProcessApplication>(processApplication);
        this.name = processApplication.getName();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public AbstractProcessApplication getProcessApplication() throws ProcessApplicationUnavailableException {
        final AbstractProcessApplication application = this.processApplication.get();
        if (application == null) {
            throw ProcessApplicationReferenceImpl.LOG.processApplicationUnavailableException(this.name);
        }
        return application;
    }
    
    public void processEngineStopping(final ProcessEngine processEngine) throws ProcessApplicationUnavailableException {
    }
    
    public void clear() {
        this.processApplication.clear();
    }
    
    static {
        ProcessApplicationReferenceImpl.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
